package com.atguigu.srb.sms;


import com.atguigu.srb.sms.util.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSms {

    @Resource
    RedisTemplate redisTemplate;

   @Test
    public void a(){

       System.out.println(SmsProperties.REGION_Id);

       redisTemplate.opsForValue().set("k1", "v1");
   }

}
