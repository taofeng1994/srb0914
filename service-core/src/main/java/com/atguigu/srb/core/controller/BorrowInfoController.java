package com.atguigu.srb.core.controller;


import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/api/core/borrowInfo")
@Api(tags = "借款信息")
public class BorrowInfoController {

    @Resource
    BorrowInfoService borrowInfoService;

    @GetMapping("/auth/getBorrowAmount")
    @ApiOperation("获取借款额度")
    public R getBorrowAmount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return R.ok().data("borrowAmount",borrowAmount);

    }


    @PostMapping("/auth/save")
    @ApiOperation("提交借款申请")
    public R save(@RequestBody BorrowInfo borrowInfo,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(userId, ResponseEnum.LOGIN_AUTH_ERROR);
        borrowInfo.setUserId(userId);
        borrowInfoService.saveBorrowInfo(borrowInfo);
        return R.ok().message("提交成功");

    }


    @GetMapping("/auth/getBorrowInfoStatus")
    @ApiOperation("获取借款申请审批状态")
    public R getBorrowInfoStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(userId, ResponseEnum.LOGIN_AUTH_ERROR);
         Integer borrowInfoStatus = borrowInfoService.getStatusByUserId(userId);
        return R.ok().data("borrowInfoStatus",borrowInfoStatus);

    }





















































}

