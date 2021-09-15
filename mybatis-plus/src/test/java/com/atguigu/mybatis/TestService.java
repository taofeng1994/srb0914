package com.atguigu.mybatis;


import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {

    @Autowired
    UserService userService;


    /**
     * 查询总记录数
     */

    @Test
    public void test01(){
        int count = userService.count();
        System.out.println(count);

    }

    /**
     * 批量插入
     */
    @Test
    public void test02() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("helen"+i);
            user.setAge(20+i);
            list.add(user);
        }
        boolean b = userService.saveBatch(list);
        System.out.println(b);

    }

    /**
     * 查询所有
     */
    @Test
    public void test03() {
        List<User> list = userService.list();
        for (User user : list) {
            System.out.println(user);

        }

    }

    /**
     * 自定义查询，根据名字查询
     */
    @Test
    public void test04() {
        List<User> list = userService.listAllByName("helen");
        for (User user : list) {
            System.out.println(user);

        }

    }


































}
