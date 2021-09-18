package com.atguigu.mybatis;


import com.atguigu.mybatis.entity.User;
import com.atguigu.mybatis.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWapper {

    @Resource
    UserMapper userMapper;

    /**
     * 组装查询条件:查询名字中包含n，年龄大于等于10且小于等于20，email不为空的用户
     */
    @Test
    public void test01() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","n")
                     .ge("age",10)
                      .le("age",20)
                        .isNotNull("email");


        List<User> list = userMapper.selectList(queryWrapper);
        for (User user : list) {
            System.out.println(user);
        }


    }

    /**
     *组装排序条件:按年龄降序查询用户，如果年龄相同则按id升序排列
     *
     */
    @Test
    public void test02() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("age")
                .orderByAsc("id");
        List<User> list = userMapper.selectList(wrapper);
        for (User user : list) {
            System.out.println(user);
        }

    }


    /**
     * 条件的优先级:查询名字中包含n，且（年龄小于18或email为空的用户），并将这些用户的年龄设置为18，
     *                                                      邮箱设置为 user@atguigu.com
     */
    @Test
    public void test03() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("name", "n")
                .and(i -> i.lt("age", 18).or().isNull("email"));

        User user = new User();
        user.setAge(18);
        user.setEmail("user@atguigu.com");
        int update = userMapper.update(user, queryWrapper);
        System.out.println(update);

    }

    /**
     * 组装查询，只查询部分字段:查询所有用户的用户名和年龄
     */
    @Test
    public void test04() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name","age");
        List<Map<String, Object>> list = userMapper.selectMaps(queryWrapper);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }

    }

    /**
     * 实现子查询：查询id不大于3的所有用户的id列表
     */
    @Test
    public void test05() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*queryWrapper.le("id",3);
        List<Object> list = userMapper.selectObjs(queryWrapper);
        for (Object o : list) {
            System.out.println(o);
        }*/
        queryWrapper.inSql("id","select id from user where id <= 3");

        List<Object> list = userMapper.selectObjs(queryWrapper);
        for (Object o : list) {
            System.out.println(o);

        }

    }

    /**
     * 组装删除条件:删除email为空的用户
     */
    @Test
    public void test06() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int delete = userMapper.delete(queryWrapper);
        System.out.println(delete);


    }

    /**
     * 测试动态失去了语句：查询名字中包含n，年龄大于10且小于20的用户
     */
    @Test
    public void test07() {
        String name = "n";
        Integer ageBegin = 10;
        Integer ageEnd = 20;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*if (name != null && !name.equals("")) {
            queryWrapper.like("name",name);
        }
        if (ageBegin != null && ageBegin > 0) {
            queryWrapper.ge("age",ageBegin);
        }if (ageEnd !=null && ageEnd>0){

            queryWrapper.le("age",ageEnd);

        }
        List<User> list = userMapper.selectList(queryWrapper);
        for (User user : list) {
            System.out.println(user);
        }*/
        queryWrapper.like(name != null && !name.equals(""), "naem", name)
                .ge(ageBegin != null && ageBegin > 0, "age", ageBegin)
                .le(ageEnd != null && ageEnd > 0, "age", ageEnd);
        List<User> list = userMapper.selectList(queryWrapper);
        for (User user : list) {
            System.out.println(user);       }
    }







































}
