package com.example.project_hardware.dto;

import java.util.Date;

public class Users {
    //DB에서는 _형식으로 들어가고 DTO에서는 camel case 형식
    private int userNum;
    private String id;
    private String nick;
    private String password;
    private String email;
    private String ban;
    private Date banDate;
    private Role role;

    public Users() {
    }

    public Users(int userNum, String id, String nick, String password, String email, String ban, Date banDate, Role role) {
        this.userNum = userNum;
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.email = email;
        this.ban = ban;
        this.banDate = banDate;
        this.role = role;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public Date getBanDate() {
        return banDate;
    }

    public void setBanDate(Date banDate) {
        this.banDate = banDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userNum=" + userNum +
                ", id='" + id + '\'' +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", ban='" + ban + '\'' +
                ", banDate=" + banDate +
                ", role=" + role +
                '}';
    }
}
