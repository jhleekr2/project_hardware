package com.example.project_hardware.controller;

import com.example.project_hardware.dto.Role;
import com.example.project_hardware.dto.Users;
import com.example.project_hardware.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@ModelAttribute Users user) {
        String userPassword = user.getPassword();
        System.out.println("userPassword:"+userPassword);
        user.setRole(Role.MEMBER);
        //비밀번호를 DB에 암호화하여 저장
        String passwordEncoded = passwordEncoder.encode(userPassword);
        user.setPassword(passwordEncoded);
        userService.insertUser(user);
        return "redirect:/loginPage";
    }

}
