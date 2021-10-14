package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Api(tags = "标的管理")
@RestController
@RequestMapping("/admin/core/lend")
public class AdminLendController {

    @Resource
    LendService lendService;


    @ApiOperation("标的列表")
    @GetMapping("/list")
    public R list(){
     List<Lend> lendList = lendService.getList();
        return R.ok().data("list", lendList);

    }


    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public R show(@PathVariable("id") Long id){
        Map<String,Object> lendDetail = lendService.getLendDetail(id);
        return R.ok().data("lendDetail", lendDetail);

    }




}

