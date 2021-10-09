package com.atguigu.srb.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.parser.Token;
import org.junit.Test;

import java.security.PrivateKey;
import java.util.*;

public class TestJwt {

    //密钥
    private static String tokenSignKey = "taofeng1994";
    //动态盐值
     private String ip = "127001";

     private String token1 ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSGVsZW4iLCJhZ2UiOjE4fQ.bSyHwzbEEMEeXX0ZvuWs5dCglG4ZYv-S-sLqVOQyfj4";
     private String token2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSGVsZW4iLCJhZ2UiOjE4LCJwYXNzd29yZCI6MTIzNDU2Nzg5fQ.IJ_NzvZuqr6LdsluUSWJrMD8hRrcZKQ44QyL91TVGD8";
     private String token3 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSGVsZW4iLCJhZ2UiOjE4LCJwYXNzd29yZCI6MTIzNDU2Nzg5fQ.-KhYYkUQ5H603dzDtebbeC4Mnm5n98VkAWMm22ha-5Q";
    @Test
    public void test01() {
        String token = Jwts.builder() //token由3部分组成，头部信息、有效荷载、以及防伪签名；
                .setHeaderParam("typ", "JWT")//设置头部信息,固定写法
                .setHeaderParam("alg", "HS256")//设置头部信息,固定写法

                .claim("name", "Helen")//设置有效荷载信息
                .claim("age", 18)//设置有效荷载信息
                .claim("password",123456789)

                .signWith(SignatureAlgorithm.HS256, tokenSignKey+ip)
                .compact();//将结果转成字符串
        System.out.println(token);


    }


    @Test
    public void test02() {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey+ip).parseClaimsJws(token3);
        System.out.println(claimsJws);
    }




    @Test
    public void test03() {

    Map<String,String> map = new HashMap<>();
        map.put("1", "2");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }


    }



    







}
