package com.example.project_hardware.dto;

import java.util.Date;

public class Comment {
    private int boardNo;
    private int userNum;
    private Date writeDate;
    private String title;
    private String content;
    private int hit;

    public Comment() {
    }

    public Comment(int boardNo, int userNum, Date writeDate, String title, String content, int hit) {
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.title = title;
        this.content = content;
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "boardNo=" + boardNo +
                ", userNum=" + userNum +
                ", writeDate=" + writeDate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hit=" + hit +
                '}';
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }
}
