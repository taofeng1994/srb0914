package com.atguigu.mybatis.service.impl;


import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.mapper.UserMapper;
import com.atguigu.mybatis.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> listAllByName(String name) {
        return userMapper.findAllByName(name);
    }
}
