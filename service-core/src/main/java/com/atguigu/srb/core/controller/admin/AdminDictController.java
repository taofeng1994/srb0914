package com.atguigu.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.common.exception.BusinessException;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Api(tags = "数据字典管理接口")
@RestController
@RequestMapping("/admin/core/dict")

@Slf4j
public class AdminDictController {


    @Resource
    private DictService dictService;


    @ApiOperation("批量导入Excel数据字典")
    @PostMapping("/import")
    public R batchImport(@RequestParam("file") MultipartFile file) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return R.ok().message("文件上传成功");
        } catch (IOException e) {

            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }


    @ApiOperation("批量导出Excel数据字典")
    @GetMapping("/export")
    public void batchImport(HttpServletResponse response) {
        try {
        //告诉浏览器这是excel文件

        response.setContentType("application/vnd.ms-excel");
        //解决响应乱码
        response.setCharacterEncoding("utf-8");
           List<ExcelDictDTO> list =  dictService.listDictData();
            //设置文件名编码等
            String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("字典数据").doWrite(list);

        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.EXPORT_DATA_ERROR, e);
        }


    }


    @GetMapping("/listByParentId/{parentId}")
    @ApiOperation("根据上级id获取子节点的数据列表")

    public R listByParentId( @PathVariable("parentId") Long parentId){
        List<Dict> dictList = dictService.listByParentId(parentId);
        return R.ok().data("list",dictList);

    }































}