package com.atguigu.srb.oss.controller.api;


import com.atguigu.srb.common.exception.BusinessException;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@RestController

@Api(tags = "阿里云文件保存接口")
@RequestMapping("/api/oss/file")
public class FileController {


    @Resource
   private FileService fileService;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public R upload(
            @ApiParam(value = "文件",required = true )
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块名")
            @RequestParam("module") String module) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream,module,originalFilename);
            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }


    @ApiOperation(value = "文件删除")
    @DeleteMapping("/remove")
    public R removeFile(@ApiParam(value = "文件全路径名",required = true)
                        @RequestParam("url") String url) {

        fileService.removeFile(url);
        return R.ok().message("文件删除成功");


    }




}
