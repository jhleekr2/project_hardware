package com.example.project_hardware.service;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithWriter;
import com.example.project_hardware.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardMapper boardMapper;

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
    public void boardWrite(Board board) {


        boardMapper.addBoardList(board);
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


}
