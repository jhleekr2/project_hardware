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
    private List<String> uploadgeneralfile;
    private List<String> deletedgeneralfile;

    public BoardWithFile() {
    }

    public BoardWithFile(int boardNo, int userNum, Date writeDate, String title, String content, int hit, List<String> uploadfile, List<String> deletedfile, List<String> uploadgeneralfile, List<String> deletedgeneralfile) {
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.uploadfile = uploadfile;
        this.deletedfile = deletedfile;
        this.uploadgeneralfile = uploadgeneralfile;
        this.deletedgeneralfile = deletedgeneralfile;
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

    public List<String> getUploadgeneralfile() {
        return uploadgeneralfile;
    }

    public void setUploadgeneralfile(List<String> uploadgeneralfile) {
        this.uploadgeneralfile = uploadgeneralfile;
    }

    public List<String> getDeletedgeneralfile() {
        return deletedgeneralfile;
    }

    public void setDeletedgeneralfile(List<String> deletedgeneralfile) {
        this.deletedgeneralfile = deletedgeneralfile;
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
                ", uploadgeneralfile=" + uploadgeneralfile +
                ", deletedgeneralfile=" + deletedgeneralfile +
                '}';
    }
}
