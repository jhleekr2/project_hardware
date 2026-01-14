package com.example.project_hardware.service;

import com.example.project_hardware.dto.Board;
import com.example.project_hardware.dto.BoardWithFile;
import com.example.project_hardware.dto.BoardWithWriter;
import com.example.project_hardware.dto.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BoardService {

    // 전체 게시물 조회
    public List<Board> list();

    // 게시물 조회할때 조회수 추가
    public void boardCount(int boardNo);

    // 게시물 쓰기
    public int boardWrite(BoardWithFile board);

    // 특정 게시물 조회
    public BoardWithWriter viewDetail(int boardNo);

    public void boardModify(BoardWithFile board);

    public void boardDelete(int boardNo);

    // 페이징을 적용하여 조회
    public Page<Map<String, Object>> getListBoard(BoardWithWriter board, Pageable pageable);

    // 게시물의 댓글을 조회
    public List<Comment> listComment(int boardNo);

    // 게시물의 댓글 쓰기
    public void writeComment(Comment comment);
}
