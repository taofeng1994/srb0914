package com.atguigu.mybatis;



import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMapper {

    @Autowired
    UserMapper userMapper;

    /**
     * 查询所有
     */
    @Test
    public void test01() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }


    /**
     * 根据id批量查询
     */
    @Test
    public void test02() {
        List<Long> list = new ArrayList<>();
        for (long i = 1l; i <=5l ; i++) {
            list.add(i);
        }
        List<User> users = userMapper.selectBatchIds(list);
        for (User user : users) {
            System.out.println(user);
        }
    }


    /**
     * 添加一个数据
     *
     */

    @Test
    public void test03() {
        User user = new User();
        user.setName("helen");
        user.setAge(18);
        int insert = userMapper.insert(user);
        System.out.println(insert);

    }

    /**
     * 根据id查询
     */

    @Test
    public void test04() {
        User user = userMapper.selectById(2);
        System.out.println(user);

    }

    /**
     * 根据条件查询
     */

    @Test
    public void test05() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "helen");
        map.put("age",18);
        List<User> users = userMapper.selectByMap(map);
        for (User user : users) {
            System.out.println(user);
        }

    }

    /**
     * 更新数据
     */
    @Test
    public void test06() {
        User user = new User();
        user.setId(1l);
        user.setAge(20);
        int i = userMapper.updateById(user);
        System.out.println(i);

    }

    /**
     * 删除数据
     */
    @Test
    public void test07() {
        int i = userMapper.deleteById(1437753985455898625l);
        System.out.println(i);
    }





































}