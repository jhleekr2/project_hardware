package com.example.project_hardware.service;

import com.example.project_hardware.dto.CustomUser;
import com.example.project_hardware.dto.Users;
import com.example.project_hardware.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    //ctrl+space 로 자동완성
    //Spring Secirity가 정해놓은 로그인 기능 사용하기 위한 공간
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Users user = userMapper.findByUsername(id);
        if(user == null) {
            //사용자 데이터 없을시
            throw new UsernameNotFoundException(id+"존재하지 않습니다.");
        }
        //로그인했을때 DB에 로그인 데이터(유저정보)가 존재할 시에는
        return new CustomUser(user);
    }
}
