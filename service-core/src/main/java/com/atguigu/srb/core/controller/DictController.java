package com.atguigu.srb.core.controller;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("/api/core/dict")
public class DictController {

    @Resource
    DictService dictService;

    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public R fidByDictCode(@PathVariable String dictCode){
     List<Dict> list = dictService.fidByDictCode(dictCode);
     return R.ok().data("list",list);

    }

}

