package com.example.project_hardware.dto;

import java.util.Date;
import java.util.List;

public class BoardWithFile {
    private int boardNo;
    private int userNum;
    private Date writeDate;
    private String title;
    private String content;
    private int hit;
    private List<String> uploadfile;
    private List<String> deletedfile;

    public BoardWithFile() {
    }

    public BoardWithFile(int boardNo, int userNum, Date writeDate, String title, String content, int hit, List<String> uploadfile, List<String> deletedfile) {
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.uploadfile = uploadfile;
        this.deletedfile = deletedfile;
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

    public List<String> getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(List<String> uploadfile) {
        this.uploadfile = uploadfile;
    }

    public List<String> getDeletedfile() {
        return deletedfile;
    }

    public void setDeletedfile(List<String> deletedfile) {
        this.deletedfile = deletedfile;
    }

    @Override
    public String toString() {
        return "BoardWithFile{" +
                "boardNo=" + boardNo +
                ", userNum=" + userNum +
                ", writeDate=" + writeDate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hit=" + hit +
                ", uploadfile=" + uploadfile +
                ", deletedfile=" + deletedfile +
                '}';
    }
}
