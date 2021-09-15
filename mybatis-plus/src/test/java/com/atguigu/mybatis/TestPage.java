package com.atguigu.mybatis;


import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.mapper.UserMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPage {

    @Resource
    UserMapper userMapper;

    /**
     * 测试查询的分页
     */
    @Test
    public void test01(){
        //创建分页
        IPage<User> page = new Page<>(2,5);
        //执行分页查询
        IPage<User> userIPage = userMapper.selectPage(page, null);
        System.out.println(userIPage.getCurrent());
        System.out.println(userIPage.getPages());
        System.out.println(userIPage.getTotal());
        System.out.println(userIPage.getSize());
        List<User> records = userIPage.getRecords();
        for (User record : records) {
            System.out.println(record);
        }

    }

    /**
     * 测试自定义方法的分页功能
     */
    @Test
    public void test02(){

        Page<User> page = new Page<>(2,5);
        IPage<User> userIPage = userMapper.selectPageByPage(page,17);
        System.out.println(userIPage.getTotal());
        System.out.println(userIPage.getSize());


    }



























}
