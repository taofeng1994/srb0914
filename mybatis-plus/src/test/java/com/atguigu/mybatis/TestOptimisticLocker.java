package com.atguigu.mybatis;


import com.atguigu.mybatis.entity.Product;
import com.atguigu.mybatis.mapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOptimisticLocker {

    @Resource
    ProductMapper productMapper;

    /**
     * 测试乐观锁
     */
    @Test
    public void test01(){

        Product productLi = productMapper.selectById(1);
        Product productWang = productMapper.selectById(1);


        productLi.setPrice(productLi.getPrice() +50);
        productWang.setPrice(productWang.getPrice() -30);


        int insert = productMapper.updateById(productLi);
        int insert1 = productMapper.updateById(productWang);
        System.out.println(insert);
        System.out.println(insert1);
        if (insert1 == 0) {
            System.out.println("小王重试");
            Product productWang1 = productMapper.selectById(1);
            productWang1.setPrice(productWang.getPrice() -30);
            int insert2 = productMapper.updateById(productWang);
            System.out.println(insert2);
        }


    }




}
