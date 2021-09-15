package com.atguigu.mybatis.service;

import com.atguigu.mybatis.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface UserService extends IService<User> {

    List<User> listAllByName(String name);
}
