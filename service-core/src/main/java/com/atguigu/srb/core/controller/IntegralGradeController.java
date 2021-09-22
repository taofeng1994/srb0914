package com.atguigu.srb.core.controller;


import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/api/core/integralGrade")
public class IntegralGradeController {

    @Resource
    IntegralGradeService integralGradeService;

    /**
     * 前台的查询所有
     *
     * @return
     */
    @GetMapping("/list")
    public List<IntegralGrade> list() {
        return integralGradeService.list();
    }

}

