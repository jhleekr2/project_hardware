package com.example.project_hardware.dto;

public class UploadFile {
    private int boardNo;
    private String filenameOri;
    private String filenameSaved;

    public UploadFile() {
    }

    public UploadFile(int boardNo, String filenameOri, String filenameSaved) {
        this.boardNo = boardNo;
        this.filenameOri = filenameOri;
        this.filenameSaved = filenameSaved;
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

    public String getFilenameSaved() {
        return filenameSaved;
    }

    public void setFilenameSaved(String filenameSaved) {
        this.filenameSaved = filenameSaved;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "boardNo=" + boardNo +
                ", filenameOri='" + filenameOri + '\'' +
                ", filenameSaved='" + filenameSaved + '\'' +
                '}';
    }
}
