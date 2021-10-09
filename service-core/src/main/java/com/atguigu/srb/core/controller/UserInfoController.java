package com.atguigu.srb.core.controller;


import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.RegexValidateUtils;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/api/core/userInfo")
@Api(tags = "会员接口")
@CrossOrigin
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {

        String code = registerVO.getCode();
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();

        //检查手机号码是否正确
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        //判断验证码是否为空
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);

        //检查验证吗是否正确
        //1、获取redis中的验证码
        String redisCode  = (String) redisTemplate.opsForValue().get("srb:sms:code" + mobile);
        //2、判断验证码是是否正确
        Assert.equals(code,redisCode,ResponseEnum.CODE_ERROR);

        //密码不能为空
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        //注册
        userInfoService.register(registerVO);

        return R.ok().message("注册成功");


    }


    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        //判断手机号和密码是否为空
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);
        //UserInfoVO userInfoVO = new UserInfoVO();

        return R.ok().data("userInfo",userInfoVO);

    }



    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
       boolean b = userInfoService.checkMobile(mobile);
       return b;
    }














}

