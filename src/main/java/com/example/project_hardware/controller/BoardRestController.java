package com.example.project_hardware.controller;

import com.example.project_hardware.dto.*;
import com.example.project_hardware.service.BoardService;
import com.example.project_hardware.service.FileService;
import com.example.project_hardware.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BoardRestController {

    //설정 파일로부터 저장 경로 참조(현대 웹 개발의 경우는 파일 자체를 배포 폴더안에 넣지 않는다)
    //대신 설정 파일을 통해 파일을 외부에 저장하고(이때 배포시마다 환경에 맞게 저장 경로 설정 파일을 변경한다)
    //URL과 실제 파일 위치를 연결(매핑)하는 식으로 파일을 관리한다.
    // application.properties 에서 설정한 업로드 폴더 경로 주입하여 uploadPath에 넣음
    @Value("${board.imgdir}")
    private String uploadPathImg;

    @Value("${board.uploaddir}")
    private String uploadPathFile;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    //게시글 CRUD 관련 API (백엔드 서버는 앞으로 RESTAPI 방식으로 게시글을 조회하고 생성, 조회, 수정, 삭제를 할 것이다.)
    //하드웨어 커뮤니티 CRUD API v1.0 (2025.07.13-)

    @GetMapping("/api/v1e/board")
    public ResponseEntity<List<Board>> getAllBoard() {
        // 전체 게시물 조회
        List<Board> list = boardService.list();
        if(list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list); // HTTP 상태메시지가 200대인지 확인하여 그렇다면 데이터를 백에서 프론트로 넘김
        } else {
            return ResponseEntity.noContent().build();//상태코드 204번 NoContent 반환
        }
    }

    //
    @GetMapping("/api/v1/board")
    public ResponseEntity<?> getPagingBoard(BoardWithWriter board, @RequestParam(required = false, defaultValue = "0") int page) {
        // 페이징 기본 설정 @PageableDefault(size = 10)

        // Page index must not be less than zero 에러 처리위한 로직(page가 0보다 작을 경우에는 자동으로 0페이지로 간주한다)
        if(page <= 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("boardNo").descending());

        // 페이징 게시물 조회
        Page<Map<String, Object>> list = boardService.getListBoard(board, pageable);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/api/v1/write")
    @Transactional //게시글 쓰기 로직 전체를 하나의 트랜잭션으로 묶음
    public ResponseEntity<String> boardWritewithoutfile(Authentication authentication, @RequestBody BoardWithFile boardWithFile) {
        // @RequestBody Board board를 통해 프론트의 입력 Form에서 값이 전달되어 입력
        // 스프링 프레임워크에 의해 전달되는 것
        // 현재 로그인된 사용자 정보를 본래 세션의 HttpSession을 통해 전달하려 하였으나 대신 스프링 시큐리티를 활용해보자
        // 알고보니 프론트단에서 로그인된 사용자 정보가 미리 입력되어 넘어오므로 필요없다.
        // 일단 혹시나 대비해서 Authentication을 남겨둔다

        // https://congsong.tistory.com/68 참고하여 스프링 부트 파일 업로드 구현해본다

        // 게시물 업로드 전에 최종 확인 전 삭제된 파일 처리부터 우선시한다.

        // 업로드한 이미지파일 목록 꺼내기
        List<String> uploadimgfiles = boardWithFile.getUploadfile();
        // 그중 최종 확인 전에 삭제된 이미지파일 목록 꺼내기
        List<String> deletedimgfiles = boardWithFile.getDeletedfile();
        // 업로드한 이미지파일 목록에서 삭제된 이미지파일 목록을 제거하고 남은 파일 목록 만들기
        uploadimgfiles.removeAll(deletedimgfiles);
        // 남은 이미지파일 목록을 기존의 boardWithFile에 대입
        boardWithFile.setUploadfile(uploadimgfiles);

        // foreach 구문을 통해서 업로드한 파일들을 DB에서 유효화
        // for(String u : uploadfiles)
        // foreach 문 복습
        // for(꺼내는 자료형(String, int 등) 매개변수명 : 컬렉션명)
        // foreach 구문 쓰면 N+1문제 발생하기 떄문에 대신 동적 쿼리를 쓴다.

        // 삭제된 이미지파일 제거 - 저장소에서 삭제하고, DB에서도 제거한다.
        fileService.deleteFile(FileRole.IMAGE, uploadPathImg, deletedimgfiles);

        // 위의 논리를 이미지파일이 아닌 일반 파일에도 그대로 적용한다.

        // 업로드한 파일 목록 꺼내기
        List<String> uploadgeneralfiles = boardWithFile.getUploadgeneralfile();
        // 그중 최종 확인 전에 삭제된 파일 목록 꺼내기
        List<String> deletedgeneralfiles = boardWithFile.getDeletedgeneralfile();
        // 업로드한 파일 목록에서 삭제된 파일 목록을 제거하고 남은 파일 목록 만들기
        if(deletedgeneralfiles != null) {
            uploadgeneralfiles.removeAll(deletedgeneralfiles);
        }
        // 남은 파일 목록을 기존의 boardWithFile에 대입
        // 디버깅 결과 이 부분에서 로직이 꼬였음. 여기서 setter 함수 이름을 변경해야 함
        boardWithFile.setUploadgeneralfile(uploadgeneralfiles);

        // 삭제된 파일 제거 - 저장소에서 삭제하고, DB에서도 제거한다.
        fileService.deleteFile(FileRole.FILE, uploadPathFile, deletedgeneralfiles);

        // 게시글 작성하여 DB에 반영
        int boardNo = boardService.boardWrite(boardWithFile);

        return ResponseEntity.ok("게시물 작성 성공");
    }

//    @PostMapping("/api/v1/write")
//    @Transactional //게시글 쓰기 로직 전체를 하나의 트랜잭션으로 묶음
//    public ResponseEntity<String> boardWrite(
//            Authentication authentication,
//            @RequestBody BoardWithFile boardWithFile,
//            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
//        // @RequestBody Board board를 통해 프론트의 입력 Form에서 값이 전달되어 입력
//        // 스프링 프레임워크에 의해 전달되는 것
//        // 현재 로그인된 사용자 정보를 본래 세션의 HttpSession을 통해 전달하려 하였으나 대신 스프링 시큐리티를 활용해보자
//        // 알고보니 프론트단에서 로그인된 사용자 정보가 미리 입력되어 넘어오므로 필요없다.
//        // 일단 혹시나 대비해서 Authentication을 남겨둔다
//
//        // https://congsong.tistory.com/68 참고하여 스프링 부트 파일 업로드 구현해본다
//
//        // 게시물 업로드 전에 최종 확인 전 삭제된 파일 처리부터 우선시한다.
//
//        // 업로드한 파일 목록 꺼내기
//        List<String> uploadfiles = boardWithFile.getUploadfile();
//        // 그중 최종 확인 전에 삭제된 파일 목록 꺼내기
//        List<String> deletedfiles = boardWithFile.getDeletedfile();
//        //List<String> remainedfiles = new ArrayList<>();
//        //업로드한 파일 목록에서 삭제된 파일 목록을 제거하고 남은 파일 목록 만들기
//        uploadfiles.removeAll(deletedfiles);
//        // 남은 파일 목록을 기존의 boardWithFile에 대입
//        boardWithFile.setUploadfile(uploadfiles);
//
//        // foreach 구문을 통해서 업로드한 파일들을 DB에서 유효화
//        // for(String u : uploadfiles)
//        // foreach 문 복습
//        // for(꺼내는 자료형(String, int 등) 매개변수명 : 컬렉션명)
//        // foreach 구문 쓰면 N+1문제 발생하기 떄문에 대신 동적 쿼리를 쓴다.
//
//        // 삭제된 파일 제거 - 저장소에서 삭제하고, DB에서도 제거한다.
//        fileService.deleteFile(FileRole.IMAGE, uploadPathImg, deletedfiles);
//
//        // 게시글 작성하여 DB에 반영
//        int boardNo = boardService.boardWrite(boardWithFile);
//
//        return ResponseEntity.ok("게시물 작성 성공");
//    }

    @PostMapping("/api/v1/uploadimg")
    public String uploadImg(@RequestParam("image") MultipartFile image) {
        //에디터를 통한 이미지 파일 업로드 API(앞으로 첨부파일 업로드 로직도 비슷하게 만들 것으로 예상)
        //더 나아가서 공통의 코드를 이용하여 이미지와 첨부파일 모두를 업로드하도록 설계를 변경할 생각

        //파일 업로드 모듈 호출(업로드하고자 하는 폴더, 업로드하고자 하는 파일)
        //반환은 저장 파일 이름
        //업로드가 실패하면 대신 RuntimeException 반환된다.
        String savefilename = fileService.uploadFile(FileRole.IMAGE, uploadPathImg, image);

        return savefilename;
    }

    @PostMapping("/api/v1/uploadfile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        //첨부파일 업로드 API

        //파일 업로드 모듈 호출(업로드하고자 파는 폴더, 업로드하고자 하는 파일)
        //반환은 저장 파일 이름
        //업로드가 실패하면 대신 RuntimeException 반환된다.
        String savefilename = fileService.uploadFile(FileRole.FILE, uploadPathFile, file);

        return savefilename;
    }

    @PostMapping("/api/v1/rollback")
    public ResponseEntity<String> rollbackImgUpload(@RequestBody BoardWithFile boardWithFile) {
        // 이 메서드는 게시물 작성을 취소할때 미리 업로드되었던 이미지 파일의 업로드 정보를 삭제하는 로직을 작성한다.
        // 기본적으로 게시물 작성 로직에서 에디터에서 삭제한 파일을 처리하는 코드를 그대로 사용하기만 하면 된다.

        // 롤백 코드는 로그아웃 상태에서도 접근할 수 있도록 스프링 시큐리티 설정에서 개방(로그인이 풀려서 게시물 작성이 안될때
        // 파일을 깨끗이 청소할 수 있도록 하기위함)

        // 업로드한 파일 목록 꺼내기
        List<String> uploadimgfiles = boardWithFile.getUploadfile();
        // 업로드한 파일 목록 꺼내기
        List<String> uploadgeneralfiles = boardWithFile.getUploadgeneralfile();

        // 업로드한 파일 전부 제거 - 저장소에서 삭제하고, DB에서도 제거한다.
        // NullPointerException 때문에 예외 처리를 해야만 함이 증명되었다.
        if(uploadimgfiles != null) {
            fileService.deleteFile(FileRole.IMAGE, uploadPathImg, uploadimgfiles);
        }

        if(uploadgeneralfiles != null) {
            fileService.deleteFile(FileRole.FILE, uploadPathFile, uploadgeneralfiles);
        }

        return ResponseEntity.ok("게시물 작성 취소 성공");
    }


    @PutMapping("/api/v1/count/{boardNo}")
    public void incrementBoardCount(@PathVariable("boardNo") int boardNo) {
        //전달인자로 전달받은 게시물 번호에 대해 조회수 증가
        boardService.boardCount(boardNo);
    }

    @GetMapping("/api/v1/boardView/{boardNo}")
    public ResponseEntity<BoardWithWriter> getBoardView(Authentication authentication, @PathVariable("boardNo") int boardNo) {
        //전달인자로 전달받은 게시물 번호에 대해 게시물 조회
        //이때 작성글 사용자 정보와 합치기 위해 별도의 DTO를 사용하고 조인을 한다

        // 누가 로그인했는지, 로그인이 되어있는지 여부에 따라 게시물 수정으로 접근할 수 있도록 한다
        // 스프링 시큐리티의 Authentication을 통해 현재 로그인된 사용자 확인
        // 일치할때만 프론트에서 게시물 수정 버튼을 띄움

        // 사용자 정보 보내기 위해 새로운 변수 정의
        Users user = new Users();
        // 로그인되어 있는지 확인하고 로그인되어 있다면
        // 게시물 조회 페이지로 갈때 로그인된 사용자가 누군지 전송
        if(authentication != null) {
            user = userService.findUserNum(authentication.getName());
        }

        BoardWithWriter board = new BoardWithWriter();

        //게시물 번호 대입
        board.setBoardNo(boardNo);
        //게시물 정보 조회하여 대입
        board = boardService.viewDetail(boardNo);

        // 여기서 전송된 로그인된 사용자 정보는 댓글 기능에도 사용되어야만 한다.
        // BoardWithWriter에 있는 변수 중 loginNum에 로그인된 사용자 정보를 담아야 함.
        // 로그인된 사용자 정보를 담음. 여기서 담긴 로그인된 사용자 정보는 프론트로 옮겨진 후 CSR기반의 댓글 CRUD 로직에 쓰일 예정임.
        board.setLoginNum(user.getUserNum());

        return ResponseEntity.ok(board); // HTTP 상태메시지가 200대인지 확인하여 그렇다면 데이터를 백에서 프론트로 넘김
    }

    @GetMapping("/api/v1/boardFileView/{boardNo}")
    public List<UploadFile> getBoardFileView(@PathVariable("boardNo") int boardNo) {
        // 게시물 첨부파일 조회
        List<UploadFile> uploadfile = fileService.selectUploadFileBoardNo(boardNo);

        return uploadfile;
    }


//    @PutMapping("/api/v1/modify/{boardNo}")
//    public ResponseEntity<String> getBoardModify(
//            @PathVariable("boardNo") int boardNo, //게시물 수정에 따라 주솟값이 바뀜
//            Authentication authentication, //작성자와 수정자가 같은지 확인
//            @RequestBody Board board) {
//
//        board.setBoardNo(boardNo);
//        boardService.boardModify(board);
//
//        return ResponseEntity.ok("게시물 수정 성공");
//    }

    @PutMapping("/api/v1/modify/{boardNo}")
    @Transactional //게시글 수정 로직 전체를 하나의 트랜잭션으로 묶음
    public ResponseEntity<String> getBoardModify(
            @PathVariable("boardNo") int boardNo, //게시물 수정에 따라 주솟값이 바뀜
            Authentication authentication, //작성자와 수정자가 같은지 확인
            @RequestBody BoardWithFile boardWithFile) {

        boardWithFile.setBoardNo(boardNo);

        // 업로드한 파일 목록 꺼내기
        List<String> uploadfiles = boardWithFile.getUploadfile();
        // 그중 최종 확인 전에 삭제된 파일 목록 꺼내기
        List<String> deletedfiles = boardWithFile.getDeletedfile();
        // 업로드한 일반 파일 목록 꺼내기
        List<String> uploadgeneralfiles = boardWithFile.getUploadgeneralfile();
        // 그중 최종 확인 전에 삭제된 일반 파일 목록 꺼내기
        List<String> deletedgeneralfiles = boardWithFile.getDeletedgeneralfile();


        //List<String> remainedfiles = new ArrayList<>();
        //업로드한 파일 목록에서 삭제된 파일 목록을 제거하고 남은 파일 목록 만들기
        uploadfiles.removeAll(deletedfiles);
        // 남은 파일 목록을 기존의 boardWithFile에 대입
        boardWithFile.setUploadfile(uploadfiles);

        //업로드한 파일 목록에서 삭제된 파일 목록을 제거하고 남은 파일 목록 만들기
        uploadgeneralfiles.removeAll(deletedgeneralfiles);
        // 남은 파일 목록을 기존의 boardWithFile에 대입
        boardWithFile.setUploadgeneralfile(uploadgeneralfiles);

        // 삭제된 파일 제거 - 저장소에서 삭제하고, DB에서도 제거한다.
        fileService.deleteFile(FileRole.IMAGE, uploadPathImg, deletedfiles);
        fileService.deleteFile(FileRole.FILE, uploadPathFile, deletedgeneralfiles);

        // 게시글 수정하여 DB에 반영
        boardService.boardModify(boardWithFile);

        return ResponseEntity.ok("게시물 수정 성공");
    }

    @DeleteMapping("/api/v1/delete/{boardNo}")
    public ResponseEntity<String> getBoardDelete(Authentication authentication, @PathVariable("boardNo") int boardNo) {
        // 사용자 정보 보내기 위해 새로운 변수 정의
        Users user = new Users();
        // 로그인되어 있는지 확인하고 로그인되어 있다면
        // 게시물 조회 페이지로 갈때 로그인된 사용자가 누군지 전송
        if(authentication != null) {
            user = userService.findUserNum(authentication.getName());
        }

        //게시물 정보 조회하여 대입
        BoardWithWriter board = boardService.viewDetail(boardNo);

        //게시물이 없으면 에러 반환
        if(board == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 없습니다.");
        }

        //게시물 조회했을때 로그인된 사용자와 게시물의 작성자가 일치하지 않으면 삭제하지 않음
        if(board.getUserNum() != user.getUserNum()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자가 아닙니다.");
        }

        //게시물 삭제
        //삭제 코드에 게시물번호에 맞는 업로드된 파일들도 삭제하는 로직을 추가해야 함
        
        boardService.boardDelete(boardNo);
        return ResponseEntity.ok("게시물 삭제 완료!");
    }

    // 여기서부터는 댓글관련 API
    @GetMapping("/api/v1/comment/view/{boardNo}")
    public ResponseEntity<List<CommentWithWriter>> commentView(Authentication authentication, @PathVariable int boardNo) {
        // 댓글 리스트 조회(댓글은 한개가 아니기 떄문에 List<Comment> 사용)
        List<CommentWithWriter> comments = boardService.listComment(boardNo);
        if(comments != null && !comments.isEmpty()) {
            return ResponseEntity.ok(comments); // HTTP 상태메시지가 200대인지 확인하여 그렇다면 데이터를 백에서 프론트로 넘김
        } else {
            return ResponseEntity.noContent().build();//상태코드 204번 NoContent 반환
        }
    }

    // 댓글 쓰기 API
    @PostMapping("/api/v1/comment/write/{boardNo}")
    public ResponseEntity<String> commentWrite(Authentication authentication, @RequestBody Comment comment) {
        // Authentication 사실은 필요가 크게 없지만(프론트단에서 로그인 회원 정보 입력되어 넘어올거라)
        // 일단은 그냥 남겨둔다
        boardService.writeComment(comment);
        return null;
    }

}
