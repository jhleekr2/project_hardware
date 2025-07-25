package com.example.project_hardware.dto;

import java.util.Date;

public class BoardWithWriter {
    // 게시물 조회할때 작성자의 아이디와 닉네임을 같이 조회해서 그걸 합쳐서 보내는 DTO
    // 로그인안되어 있으면 그냥 null 처리하고 로그인되어 있으면 현재 로그인된 사용자 정보 포함해서 전달

    private int boardNo;
    private int userNum;
    private Date writeDate;
    private String title;
    private String content;
    private int hit;
    // 여기서부터 게시글 작성자의 아이디와 닉네임을 조회하기 위해 기존 Board DTO 대비 추가된 항목
    private String id;
    private String nick;
    // 여기서부터 로그인된 사용자와 게시글 작성자를 비교하기 위해 기존 Board DTO 대비 추가된 항목
    private int loginNum;

    public BoardWithWriter() {
    }

    public BoardWithWriter(int boardNo, int userNum, Date writeDate, String title, String content, int hit, String id, String nick, int loginNum) {
        this.boardNo = boardNo;
        this.userNum = userNum;
        this.writeDate = writeDate;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.id = id;
        this.nick = nick;
        this.loginNum = loginNum;
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
        return "BoardWithWriter{" +
                "boardNo=" + boardNo +
                ", userNum=" + userNum +
                ", writeDate=" + writeDate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hit=" + hit +
                ", id='" + id + '\'' +
                ", nick='" + nick + '\'' +
                ", loginNum=" + loginNum +
                '}';
    }
}
