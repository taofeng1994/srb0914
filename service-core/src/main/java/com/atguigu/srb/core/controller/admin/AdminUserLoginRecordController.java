package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "后台会员登录日志接口")
@RequestMapping("/admin/core/userLoginRecord")
@CrossOrigin
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;


    @GetMapping("/listTop50/{userId}")
    @ApiOperation("获取会员登录日志列表")
    public R listTop50(
            @ApiParam(value = "用户id", required = true)
            @PathVariable Long userId
    ) {
     List<UserLoginRecord> userLoginRecordList = userLoginRecordService.listTop50(userId);
     return R.ok().data("list",userLoginRecordList);



    }


































}
