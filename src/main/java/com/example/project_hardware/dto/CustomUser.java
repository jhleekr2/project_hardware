package com.example.project_hardware.dto;

import org.springframework.security.core.authority.AuthorityUtils;

public class CustomUser extends org.springframework.security.core.userdetails.User {
    private Users user;

    public CustomUser(Users user) {
//기존의 우리가 만들었던 user클래스와 시큐리티에 있는 user 클래스를 합침
        super(user.getId(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
    }

}
