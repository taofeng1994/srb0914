package com.atguigu.srb.oss.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.srb.oss.service.FileService;
import com.atguigu.srb.oss.util.OssProperties;

import org.springframework.stereotype.Service;

import java.io.InputStream;


import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class FileServiceImpl implements FileService {





    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        //创建客户端实例
        OSS client = new OSSClientBuilder().build(OssProperties.ENDPOINT,
                            OssProperties.KEY_ID, OssProperties.KEY_SECRET);

        //判断ducket桶实例是否存在：如果不存在则创建，如果存在则获取
        if (!client.doesBucketExist(OssProperties.BUCKET_NAME)) {
            //创建ducket桶实例
            client.createBucket(OssProperties.BUCKET_NAME);
            //设置ducket的访问权限为公共读
            client.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

        //构建日期路径：avatar/2019/02/26/文件名
        //String folder = new DateTime().toString("yyyy/MM/dd");
        Date date = new Date();
        String folder = new SimpleDateFormat("yyyy/MM/dd").format(date);
        //文件名：uuid.扩展名
        int index = originalFilename.lastIndexOf(".");
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(index);

        //文件根路径
        String key = module + "/" + folder + "/" + fileName;

        //文件上传到阿里云
        client.putObject(OssProperties.BUCKET_NAME,key,inputStream);

        //关闭client
        client.shutdown();

        return "https://" +OssProperties.BUCKET_NAME + "." +OssProperties.ENDPOINT + "/" +key ;


    }


    @Override
    public void removeFile(String url) {
        //创建客户端实例
        OSS client = new OSSClientBuilder().build(OssProperties.ENDPOINT,
                                                  OssProperties.KEY_ID, OssProperties.KEY_SECRET);

        //文件名
        String host = "https://" +OssProperties.BUCKET_NAME + "." +OssProperties.ENDPOINT + "/";

        String substring = url.substring(host.length());

        //删除文件
        client.deleteObject(OssProperties.BUCKET_NAME,substring);

        //关闭client
        client.shutdown();

    }





}
