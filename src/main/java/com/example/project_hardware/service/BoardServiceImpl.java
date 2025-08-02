package com.example.project_hardware.service;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithFile;
import com.example.project_hardware.dto.BoardWithWriter;
import com.example.project_hardware.dto.RequestList;
import com.example.project_hardware.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileService fileService;

    @Override
    public List<Board> list() {
        List<Board> result = boardMapper.viewBoard();

        return result;
    }

    @Override
    public void boardCount(int boardNo) {
        boardMapper.addBoardCount(boardNo);
    }

    @Override
    public int boardWrite(BoardWithFile boardWithFile) {
        //새로운 변수 Board 정의
        Board board = new Board();
        board.setUserNum(boardWithFile.getUserNum());
        board.setTitle(boardWithFile.getTitle());
        board.setContent(boardWithFile.getContent());

        // 게시판에 글을 추가
        boardMapper.addBoardList(board);
        // 마이바티스가 추가한 글 번호를 리턴해주고 마이바티스는 board 변수에 리턴값을 저장함.
        int boardNo = board.getBoardNo();
        // 2025-08-02
        // 업로드한 파일 유효화 - 이때 동시성 문제냐 N+1문제냐 이슈가 있음
        // 파일 업로드를 유효화하려면 파일 업로드 DB에 업로드한 파일을 조회해서 게시물 번호를 추가해주는 식으로 유효화하는데
        // 이때 게시물 번호가 없는 파일들을 조회해서 추가하면 다른 사람이 동시에 게시물 업로드 작업하고 있을때 나중에 업로드한 쪽은
        // DB와 파일의 연관관계가 깨지는 문제가 생긴다.
        // 그렇다고 업로드한 파일명을 기준으로 조회해서 게시물 번호룰 추가하는것도
        // N+1문제가 생겨나는 문제가 있다.
        // 아마 이 부분은 쿼리를 만져서 극복해야 할 것이다.
        // N+1문제 해결을 위해 마이바티스의 동적 쿼리를 쓰게 될 것이다.

        // 업로드한 파일 목록 꺼내기 (이미 삭제된 파일 목록은 제거된 상태임)
        List<String> uploadfiles = boardWithFile.getUploadfile();
        // 그중 최종 확인 전에 삭제된 파일 목록 꺼내기
        //List<String> deletedfiles = boardWithFile.getDeletedfile();
        //List<String> remainedfiles = new ArrayList<>();
        //업로드한 파일 목록에서 삭제된 파일 목록을 제거하고 남은 파일 목록 만들기
        //uploadfiles.removeAll(deletedfiles);

        // foreach 구문을 통해서 업로드한 파일들을 DB에서 유효화
        // for(String u : uploadfiles)
        // foreach 문 복습
        // for(꺼내는 자료형(String, int 등) 매개변수명 : 컬렉션명)
        // foreach 구문 쓰면 N+1문제 발생하기 떄문에 대신 동적 쿼리를 쓴다.

        // 삭제된 파일 목록 제거
        //fileService.deleteFile(deletedfiles);

        // 이때 업로드 파일을 넣을 게시글 번호도 같이 넘겨야 한다.
        fileService.validateimgDB(boardNo, uploadfiles);
        // 여기까지 해결되면 파일 업로드 구현은 DB저장까지 완벽하게 완료!

        return boardNo;
    }

    @Override
    public BoardWithWriter viewDetail(int boardNo) {
        // 이 메서드에서는 조회하려는 게시물을 조회하고
        // 이 과정에서 게시물 작성자와 닉네임을 같이 조회합쳐서 조회해야함
        BoardWithWriter board = boardMapper.viewBoardDetail(boardNo);

        return board;
    }

    @Override
    // @Transactional // 게시물 수정할떄 트랜젝션 오류 발생해서 이 어노테이션 추가
    public void boardModify(Board board) {
        boardMapper.updateBoardList(board);
    }

    @Override
    public void boardDelete(int boardNo) {
        boardMapper.boardDelete(boardNo);
    }

    // 전체 게시물을 조회하는 대신 페이징을 적용하여 게시물 작성자까지 포함하여 조회
    // Pageable API를 써보려고 시도했음 - 실패!(프론트코드 엎어야하고, DB FULL SCAN 발생)
    // 2025-07-31 Pageable API 적용 완료
    @Override
    public Page<Map<String, Object>> getListBoard(BoardWithWriter board, Pageable pageable) {

        // 빌더 패턴으로 data, pageable 파라미터에 데이터 주입
        RequestList<?> requestList = RequestList.builder()
                .data(board)
                .pageable(pageable)
                .build();

        List<Map<String, Object>> result = boardMapper.viewBoardPage(requestList);

        int total = boardMapper.viewBoardCount(board);

        //List<Map<String, Object>> page = PageImpl<>(result, pageable, total);

        return new PageImpl<>(result, pageable, total);
    }


}
