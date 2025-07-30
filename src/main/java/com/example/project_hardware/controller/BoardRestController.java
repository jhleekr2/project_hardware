package com.example.project_hardware.controller;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithWriter;
import com.example.project_hardware.dto.Users;
import com.example.project_hardware.service.BoardService;
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
    private String uploadPath;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

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
    public ResponseEntity<String> boardWrite(Authentication authentication, @RequestBody Board board) {
        // @RequestBody Board board를 통해 프론트의 입력 Form에서 값이 전달되어 입력
        // 스프링 프레임워크에 의해 전달되는 것
        // 현재 로그인된 사용자 정보를 본래 세션의 HttpSession을 통해 전달하려 하였으나 대신 스프링 시큐리티를 활용해보자
        // 알고보니 프론트단에서 로그인된 사용자 정보가 미리 입력되어 넘어오므로 필요없다.
        // 일단 혹시나 대비해서 Authentication을 남겨둔다

        // https://congsong.tistory.com/68 참고하여 스프링 부트 파일 업로드 구현해본다

        boardService.boardWrite(board);

        return ResponseEntity.ok("게시물 작성 성공");
    }

    @PostMapping("/api/v1/uploadimg")
    public String uploadImg(@RequestParam("image") MultipartFile image) {
        //에디터를 통한 이미지 파일 업로드 API(앞으로 첨부파일 업로드 로직도 비슷하게 만들 것으로 예상)
        //저장 폴더 객체 생성
        //final String uploadDir = uploadPath;
        File uploaddir = new File(uploadPath);

        //저장할 폴더가 없으면 생성한다
        if(!uploaddir.exists()) {
            uploaddir.mkdirs(); //상위 폴더까지 모두 생성
            // mkdir()은 부모 없으면 실해, mkdirs()는 부모까지 생성
        }

        // 원본 파일명
        String orgFilename = image.getOriginalFilename();
        // 확장자
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String uuid;
        String saveFilename;
        String uploadFile;
        File dest = null;

        try {

            // UUID 중복파일이 있을때 처리하는 로직
            // 이미 파일이 존재하는지 확인하고 존재한다면 uuid를 새로 만든다
            do {
                // 32자리 랜덤 문자열
                uuid = UUID.randomUUID().toString().replaceAll("-", "");
                // 디스크에 저장할 파일명
                saveFilename = uuid + "." + extension;
                // 저장되는 파일명
                uploadFile = Paths.get(uploadPath, saveFilename).toString();
                // 저장 파일 객체 생성
                dest = new File(uploadFile);
            } while (dest.exists());

            // 목적지에 파일 저장
            image.transferTo(dest);
            // API로서 저장된 파일이름을 반환하고 클라이언트에서 서버에 저장된 파일명을 그대로 받음
            return saveFilename;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        System.out.println(boardNo);
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
        return ResponseEntity.ok(board); // HTTP 상태메시지가 200대인지 확인하여 그렇다면 데이터를 백에서 프론트로 넘김
    }

    @PutMapping("/api/v1/modify/{boardNo}")
    public ResponseEntity<String> getBoardModify(
            @PathVariable("boardNo") int boardNo, //게시물 수정에 따라 주솟값이 바뀜
            Authentication authentication, //작성자와 수정자가 같은지 확인
            @RequestBody Board board) {

        board.setBoardNo(boardNo);
        boardService.boardModify(board);

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
        boardService.boardDelete(boardNo);
        return ResponseEntity.ok("게시물 삭제 완료!");
    }

}
