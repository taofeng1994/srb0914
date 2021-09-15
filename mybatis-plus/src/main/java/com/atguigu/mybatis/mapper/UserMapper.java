package com.atguigu.mybatis.mapper;


import com.atguigu.mybatis.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> findAllByName(String name);


    IPage<User> selectPageByPage(Page<?> page, Integer age);
}


