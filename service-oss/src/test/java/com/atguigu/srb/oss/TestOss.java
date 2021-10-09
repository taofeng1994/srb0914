package com.atguigu.srb.oss;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestOss {


    @Test
    public void a(){

        OSS client = new OSSClientBuilder().build("oss-cn-shenzhen.aliyuncs.com", "LTAI5tRb4YGPDqRjuCEazQkL",
                "s0wh0QRH5izJQ79PPeyAEGgAK7Xmzp");

        boolean srb0521 = client.doesBucketExist("srb0521");
        System.out.println(srb0521);
        client.shutdown();




    }





}
