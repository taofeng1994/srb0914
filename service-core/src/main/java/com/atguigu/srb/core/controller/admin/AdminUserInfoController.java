package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "后台会员管理")
@RestController
@RequestMapping("/admin/core/userInfo")

public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("后台获取会员分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(value = "查询对象", required = false)
                    UserInfoQuery userInfoQuery)
     {
         Page<UserInfo> pageParam = new Page<>(page,limit);
         IPage<UserInfo> pageModel = userInfoService.listPage(pageParam,userInfoQuery);
         return R.ok().data("pageModel",pageModel);


     }


    @PutMapping("/lock/{id}/{status}")
    @ApiOperation("后台会员的锁定和解锁")
    public R lock(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long id,

            @ApiParam(value = "锁定状态（0：锁定 1：解锁）", required = true)
            @PathVariable("status") Integer status
    ){
        userInfoService.lock(id,status);
        return R.ok().message(status == 1 ? "解锁成功" : "锁定成功");

    }

}
