package com.atguigu.srb.sms.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.util.SmsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "短信管理")
@CrossOrigin
@RequestMapping("/api/sms")
public class ApiSmsController {

    @Resource
    SmsService smsService;


    @GetMapping("/send/{moblie}")
    @ApiOperation("验证码发送")
    public R sendSms(@PathVariable("moblie") String mobile){

        smsService.send(mobile);
        return R.ok().message("验证码短信发送成功");
    }


}
