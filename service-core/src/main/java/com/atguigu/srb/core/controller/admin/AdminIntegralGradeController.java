package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/core/integralGrade")
@Api(tags = "积分等级管理")
public class AdminIntegralGradeController {

    @Resource
    IntegralGradeService integralGradeService;

    /**
     * 后台查询所有
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看所有积分列表")
    public R list() {
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list",list).message("获取列表成功");
    }

    /**
     * 根据id删除积分等级
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除积分列表",notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public R removeById(@PathVariable Long id){
        boolean b = integralGradeService.removeById(id);
        return R.ok().message("删除成功");

    }

    /**
     * 根据id查询积分等级
     * @return
     */

    @GetMapping("/get/{id}")
    @ApiOperation("根据id查询积分等级")
    public R getById(@PathVariable Long id){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        return R.ok().data("integralGrade", integralGrade);
    }


    /**
     * 根据id更新积分等级
     * @param integralGrade
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("根据id更新积分等级")
    public R updateById(@RequestBody IntegralGrade integralGrade){
        BigDecimal borrowAmount = integralGrade.getBorrowAmount();
        Assert.notNull(borrowAmount,ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        boolean b = integralGradeService.updateById(integralGrade);
        return R.ok().message("更新成功");

    }


    /**
     * 新增积分等级
     * @param integralGrade
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("新增积分等级")
    public R save(@RequestBody IntegralGrade integralGrade){
        BigDecimal borrowAmount = integralGrade.getBorrowAmount();
        Assert.notNull(borrowAmount,ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean b = integralGradeService.save(integralGrade);
        return R.ok().message("新增成功");


    }






















































}
