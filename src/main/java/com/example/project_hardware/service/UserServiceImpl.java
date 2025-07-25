package com.example.project_hardware.service;

import com.example.project_hardware.dto.Users;
import com.example.project_hardware.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//스프링 프레임워크의 서비스 어노테이션은 인터페이스에 써서는 안된다
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(Users user) {
        userMapper.insertUser(user);
    }

    @Override
    public Users findWriter(String name) {
        return userMapper.findWriter(name);
    }

    @Override
    public Users findUserNum(String id) {
        return userMapper.findWriterNum(id);
    }

}
