package com.atguigu.srb.sms.service.impl;


import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.RandomUtils;
import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    RedisTemplate redisTemplate;




    @SneakyThrows //将异常抛出
    @Override
    public void send(String mobile) {

        //创建阿里云的远程客户端对象需要的参数
        DefaultProfile profile = DefaultProfile.getProfile(SmsProperties.REGION_Id,SmsProperties.KEY_ID,
                                                            SmsProperties.KEY_SECRET);
        //创建远程客户端对象
        DefaultAcsClient defaultAcsClient = new DefaultAcsClient(profile);

        //创建远程连接的请求,并设置他的请求参数
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setSysMethod(MethodType.POST);//发送POST请求
        commonRequest.setSysDomain("dysmsapi.aliyuncs.com");
        commonRequest.setSysVersion("2017-05-25");
        commonRequest.setSysAction("SendSms");
        commonRequest.putQueryParameter("RegionId", SmsProperties.REGION_Id);
        commonRequest.putQueryParameter("PhoneNumbers", mobile);
        commonRequest.putQueryParameter("SignName", "北京课时教育");
        commonRequest.putQueryParameter("TemplateCode", SmsProperties.TEMPLATE_CODE);

        //生成验证码，并将验证码通过远程客户端发送到阿里云短信
        String code = RandomUtils.getSixBitRandom();
        Map<String, String> map = new HashMap<>();
        map.put("code",code);

        String json = JSON.toJSONString(map);
        commonRequest.putQueryParameter("TemplateParam",json);

        //使用客户端对像发送请求，并得到响应结果
        CommonResponse response = defaultAcsClient.getCommonResponse(commonRequest);
        //根据响应判断验证码是否发送成功；
        boolean b = response.getHttpResponse().isSuccess();
        Assert.isTrue(b,ResponseEnum.ALIYUN_SMS_ERROR);

        //将验证码存入redis缓存
        redisTemplate.opsForValue().set("srb:sms:code" + mobile,code,3, TimeUnit.MINUTES);



    }





















































}
