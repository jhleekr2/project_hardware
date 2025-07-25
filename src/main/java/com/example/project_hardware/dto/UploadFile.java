package com.example.project_hardware.dto;

public class UploadFile {
    private int boardNo;
    private String filenameOri;
    private String filenameSav;

    public UploadFile() {
    }

    public UploadFile(int boardNo, String filenameOri, String filenameSav) {
        this.boardNo = boardNo;
        this.filenameOri = filenameOri;
        this.filenameSav = filenameSav;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getFilenameOri() {
        return filenameOri;
    }

    public void setFilenameOri(String filenameOri) {
        this.filenameOri = filenameOri;
    }

    public String getFilenameSav() {
        return filenameSav;
    }

    public void setFilenameSav(String filenameSav) {
        this.filenameSav = filenameSav;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "boardNo=" + boardNo +
                ", filenameOri='" + filenameOri + '\'' +
                ", filenameSav='" + filenameSav + '\'' +
                '}';
    }
}
