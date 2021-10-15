package com.atguigu.srb.core.controller;


import com.alibaba.fastjson.JSON;
import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Api("会员账户")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class UserAccountController {

    @Resource
    UserAccountService userAccountService;


    @ApiOperation("充值接口")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(
            @ApiParam("充值金额")
            @PathVariable BigDecimal chargeAmt, HttpServletRequest request
            ){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt,userId);
        return R.ok().data("formStr",formStr);
    }


    @PostMapping("/notify")
    @ApiOperation("账户充值异步回调")
    public String hfbNotify(HttpServletRequest request){
        Map<String, Object> map = RequestHelper.switchMap(request.getParameterMap());
        //校验签名，看是否绑定成功
        if (!RequestHelper.isSignEquals(map)){
            //校验签名失败
            return "fail";
        }

        //校验签名成功
        if("0001".equals(map.get("resultCode"))) {
            return userAccountService.hfbNotify(map);
        } else {
            log.info("用户充值异步回调充值失败：" + JSON.toJSONString(map));
            return "false";
        }
    }









































}

