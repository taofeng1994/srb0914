package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@Api(tags = "后台借款人管理")
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {

    @Resource
    BorrowerService borrowerService;


    @GetMapping("/list/{page}/{limit}")
    @ApiOperation("获取借款人分页列表")
    public R listPage(
            @ApiParam( "当前页码")
            @PathVariable Long page,
            @ApiParam("每页条数")
                @PathVariable Long limit,
            @ApiParam("查询的关键字")
                String keyword
    ){
        Page<Borrower> pageParams = new Page<>(page,limit);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParams,keyword);

        return R.ok().data("pageModel",pageModel);

    }



    @GetMapping("/show/{id}")
    @ApiOperation("获取借款人信息")
    public R show(@ApiParam("借款然id")@PathVariable Long id){
     BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(id);
     return R.ok().data("borrowerDetailVO",borrowerDetailVO);

    }



    @PostMapping("/approval")
    @ApiOperation("借款额度审批")
    public R approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO){

        borrowerService.approval(borrowerApprovalVO);
        return R.ok().message("审核成功");
    }


















}
