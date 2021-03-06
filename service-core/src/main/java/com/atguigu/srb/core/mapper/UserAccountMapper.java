package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    void updateAccount(@Param("bindCode") String bindCode, @Param("amount") BigDecimal bigDecimal, @Param("freezeAmount") BigDecimal bigDecimal1);
}
