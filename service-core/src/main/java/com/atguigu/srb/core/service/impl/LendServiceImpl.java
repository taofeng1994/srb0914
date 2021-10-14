package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.atguigu.srb.core.service.LendService;
import com.atguigu.srb.core.util.LendNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Service
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Resource
    BorrowerService borrowerService;

    @Resource
    BorrowerMapper borrowerMapper;



    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {
        Lend lend = new Lend();


        BeanUtils.copyProperties(borrowInfo,lend);
        BeanUtils.copyProperties(borrowInfoApprovalVO,lend);
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal("100")));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal("100")));
        //标的最低投资金额；
        lend.setLowestAmount(new BigDecimal("100"));
        //标的已经投资金额
        lend.setInvestAmount(new BigDecimal("0"));
        //标的已经投资人数
        lend.setInvestNum(0);
        lend.setPublishDate(LocalDateTime.now());
        //设置起息日
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dateTimeFormatter);
        lend.setLendStartDate(lendStartDate);
        //设置结束日期，起息日往后推还款期数
        LocalDate lendEndtDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndtDate);

        //平台预期收益   平台收益 = 标的金额 * 月年化 * 期数
        //        月年化 = 年化 / 12
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal("12"),8,BigDecimal.ROUND_DOWN);
        BigDecimal expectAmount = lend.getAmount().multiply(new BigDecimal(lend.getPeriod())).multiply(monthRate);
        lend.setExpectAmount(expectAmount);

        //设置平台的已得收益
        lend.setRealAmount(new BigDecimal("0"));

        //设置标的状态为募资中
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //设置审核时间
        lend.setCheckTime(LocalDateTime.now());
        //设置审核人
        lend.setCheckAdminId(1L);
        baseMapper.insert(lend);


    }

    @Override
    public List<Lend> getList() {
        List<Lend> lendList = baseMapper.selectList(null);
        for (Lend lend : lendList) {
            String returnMethod = borrowerService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
            lend.getParam().put("returnMethod",returnMethod);
            lend.getParam().put("status", status);
        }
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        HashMap<String, Object> lendDetailMap = new HashMap<>();
        //查询标的对象
        Lend lend = baseMapper.selectById(id);
        //组装数据
        String returnMethod = borrowerService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod",returnMethod);
        lend.getParam().put("status", status);

        //获取借款人对象
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", lend.getUserId());
        Borrower borrower = borrowerMapper.selectOne(queryWrapper);
        //组装借款人对象
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

        lendDetailMap.put("lend",lend);
        lendDetailMap.put("borrower",borrowerDetailVO);
        return lendDetailMap;

    }






}
