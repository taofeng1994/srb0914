package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.enums.IntegralEnum;
import com.atguigu.srb.core.mapper.*;
import com.atguigu.srb.core.pojo.entity.*;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.implementation.InvokeDynamic;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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

    @Resource
    DictMapper dictMapper;

    @Resource
    UserIntegralMapper userIntegralMapper;



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
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        }
        //更新会员状态为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);

    }

    @Override
    public Integer getBorrowerStatus(Long userId) {
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("user_id",userId);
        Borrower borrower = baseMapper.selectOne(queryWrapper);
        if (borrower == null) {
            borrower = new Borrower();
            borrower.setStatus(0);
        }
        return borrower.getStatus();

    }



    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParams, String keyword) {
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(keyword),"name",keyword).or()
                    .like(!StringUtils.isEmpty(keyword),"id_card",keyword).or()
                    .like(!StringUtils.isEmpty(keyword),"mobile",keyword);

        Page<Borrower> borrowerPage = baseMapper.selectPage(pageParams, queryWrapper);
        return borrowerPage;
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        Borrower borrower = baseMapper.selectById(id);
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower,borrowerDetailVO);
        borrowerDetailVO.setSex(borrower.getSex()==1?"男":"女");
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        //设置下拉框学历、行业等信息
        String education = getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = getNameByParentDictCodeAndValue("industry", borrower.getIndustry());
        String returnSource = getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String relation = getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());
        String income = getNameByParentDictCodeAndValue("income", borrower.getIncome());
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(relation);
        borrowerDetailVO.setIncome(income);

        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);
        QueryWrapper<BorrowerAttach> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("borrower_id", id);
        List<BorrowerAttachVO> list = new ArrayList<>();

        //设置borrowerAttachVOList
        List<BorrowerAttach> borrowerAttachesList = borrowerAttachMapper.selectList(queryWrapper);
        for (BorrowerAttach borrowerAttach : borrowerAttachesList) {
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            BeanUtils.copyProperties(borrowerAttach,borrowerAttachVO);
            list.add(borrowerAttachVO);
        }
        borrowerDetailVO.setBorrowerAttachVOList(list);

        return borrowerDetailVO;
    }

    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        //修改借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        BigDecimal totalInteger = new BigDecimal("0");

        //添加积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);
        totalInteger = totalInteger.add(new BigDecimal(borrowerApprovalVO.getInfoIntegral()));

        if (borrowerApprovalVO.getIsIdCardOk()){
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
            totalInteger = totalInteger.add(new BigDecimal(IntegralEnum.BORROWER_IDCARD.getIntegral()));
        }
        if (borrowerApprovalVO.getIsCarOk()){
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
            totalInteger = totalInteger.add(new BigDecimal(IntegralEnum.BORROWER_CAR.getIntegral()));
        }
        if (borrowerApprovalVO.getIsHouseOk()){
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
            totalInteger = totalInteger.add(new BigDecimal(IntegralEnum.BORROWER_HOUSE.getIntegral()));
        }


        //修改审核状态
        userInfo.setIntegral(totalInteger.intValue());
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateById(userInfo);

    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = dictMapper.selectOne(queryWrapper);
        if (dict == null) {
            return "";
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",dict.getId())
                    .eq("value",value);
        Dict dict1 = dictMapper.selectOne(queryWrapper);
        if (dict1 == null){
            return "";
        }
        return dict1.getName();
    }































}
