package com.example.project_hardware.mapper;

import com.example.project_hardware.dto.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    //MVC 패턴에서 DAO부분
    @Insert("INSERT INTO users (id, nick, password, email, role) " +
            "VALUES( #{id}, #{nick}, #{password}, #{email}, #{role})")
    void insertUser(Users user);

    @Select("SELECT id, nick, password, role FROM users WHERE id=#{id}")
    Users findByUsername(String id);

    @Select("SELECT user_num as userNum, id, nick FROM users WHERE id=#{name}")
    Users findWriter(String name);

    @Select("SELECT user_num as userNum, id, nick FROM users WHERE id=#{id}")
    Users findWriterNum(String id);
}
