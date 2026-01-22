package com.example.project_hardware.dto;

import java.util.Date;

public class CommentWithWriter {
    // 댓글 조회할때 작성자의 아이디와 닉네임을 같이 조회해서 그걸 합쳐서 보내는 DTO
    // 로그인안되어 있으면 그냥 null 처리하고 로그인되어 있으면 현재 로그인된 사용자 정보 포함해서 전달

    private int commentNo;
    private int boardNo;
    private int userNum;
    private Date writeDate;
    private String content;
    // 여기서부터 게시글 작성자의 아이디와 닉네임을 조회하기 위해 기존 Board DTO 대비 추가된 항목
    private String id;
    private String nick;
    // 여기서부터 로그인된 사용자와 게시글 작성자를 비교하기 위해 기존 Board DTO 대비 추가된 항목
    private int loginNum;

    public CommentWithWriter() {
    }

    public CommentWithWriter(int commentNo, int boardNo, int userNum, Date writeDate, String content, String id, String nick, int loginNum) {
        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.content = content;
        this.id = id;
        this.nick = nick;
        this.loginNum = loginNum;
    }

    public int getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    @Override
    public String toString() {
        return "CommentWithWriter{" +
                "commentNo=" + commentNo +
                ", boardNo=" + boardNo +
                ", userNum=" + userNum +
                ", writeDate=" + writeDate +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", nick='" + nick + '\'' +
                ", loginNum=" + loginNum +
                '}';
    }
}