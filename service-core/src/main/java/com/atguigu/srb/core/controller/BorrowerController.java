package com.atguigu.srb.core.controller;


import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@RestController
@Api(tags = "借款人")
@RequestMapping("/api/core/borrower")
public class BorrowerController {

    @Resource
    BorrowerService borrowerService;


    @ApiOperation("借款人积分等级申请接口")
    @PostMapping("/save")
    public R save(@RequestBody BorrowerVO borrowerVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Assert.notNull(token, ResponseEnum.LOGIN_AUTH_ERROR);
        Long userId = JwtUtils.getUserId(token);
        borrowerService.saveBorrowerVOByUserId(borrowerVO,userId);
        return R.ok().message("信息提交成功");

    }


    @ApiOperation("借款人积分等级申请状态接口")
    @GetMapping("/auth/getBorrowerStatus")
    public R getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Assert.notNull(token,ResponseEnum.LOGIN_AUTH_ERROR);
        Long userId = JwtUtils.getUserId(token);
        Integer borrowerStatus = borrowerService.getBorrowerStatus(userId);
        return R.ok().data("borrowerStatus",borrowerStatus);


    }
























}

