package com.example.project_hardware.dto;

import java.util.Date;

public class Comment {
    private int commentNo;
    private int boardNo;
    private int userNum;
    private Date writeDate;
    private String content;

    public Comment() {
    }

    public Comment(int commentNo, int boardNo, int userNum, Date writeDate, String content) {
        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentNo=" + commentNo +
                ", boardNo=" + boardNo +
                ", userNum=" + userNum +
                ", writeDate=" + writeDate +
                ", content='" + content + '\'' +
                '}';
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
}