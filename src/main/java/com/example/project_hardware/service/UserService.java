package com.example.project_hardware.service;

import com.example.project_hardware.dto.Users;

public interface UserService {

    public void insertUser(Users user);

    public Users findWriter(String name);

    // 로그인된 사용자 아이디에서 사용자의 회원번호 찾기
    public Users findUserNum(String id);

}
