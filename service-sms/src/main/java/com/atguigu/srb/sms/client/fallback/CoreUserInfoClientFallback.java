package com.atguigu.srb.sms.client.fallback;


import com.atguigu.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {

    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，服务熔断");
        //远程调用失败后，默认该用户没有注册，返回false即默认没有注册
        return false;
    }
}
