package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.mapper.BorrowerAttachMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    /**
     * 借款人积分等级申请
     * @param borrowerVO
     * @param userId
     */
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        //根据用户id查询用户信息
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setName(userInfo.getName());
        borrower.setMobile(userInfo.getMobile());
        //认证中
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        baseMapper.insert(borrower);

        //保存借款人附件

        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();

        for (BorrowerAttach borrowerAttach : borrowerAttachList) {
            borrowerAttach.setBorrowerId(userId);
            borrowerAttachMapper.insert(borrowerAttach);
        }
        //更新会员状态为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);

    }

























}
