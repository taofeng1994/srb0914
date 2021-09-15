package com.atguigu.mybatis.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "product")
public class Product {

    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "price")
    private Integer price;

    @Version
    @TableField(value = "version")
    private Integer version;


}
