package com.atguigu.srb.core.controller;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Api(tags = "标的")
@RestController
@RequestMapping("/api/core/lend")
public class LendController {

    @Resource
    LendService lendService;


    @ApiOperation("前台前端获取标的列表")
    @GetMapping("/list")
    public R list() {
        List<Lend> lendList = lendService.getList();
        return R.ok().data("lendList",lendList);

    }

}

