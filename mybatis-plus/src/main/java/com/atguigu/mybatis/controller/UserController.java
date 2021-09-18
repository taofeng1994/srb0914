package com.atguigu.mybatis.controller;

import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.service.UserService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/list")
    public List<User> list(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = request.getHeader("token");
        String fugui = request.getHeader("fugui");

        return userService.list();

    }



}
