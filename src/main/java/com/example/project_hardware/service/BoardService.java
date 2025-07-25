package com.example.project_hardware.service;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithWriter;

import java.util.List;

public interface BoardService {

    // 전체 게시물 조회
    public List<Board> list();

    // 게시물 조회할때 조회수 추가
    public void boardCount(int boardNo);

    // 게시물 쓰기
    public void boardWrite(Board board);

    // 특정 게시물 조회
    public BoardWithWriter viewDetail(int boardNo);

    public void boardModify(Board board);

    public void boardDelete(int boardNo);
}
