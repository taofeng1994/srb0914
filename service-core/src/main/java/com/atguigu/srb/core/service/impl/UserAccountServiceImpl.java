package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.atguigu.srb.core.service.UserAccountService;
import com.atguigu.srb.core.util.LendNoUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {


    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    TransFlowService transFlowService;

    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        //根据userId获取用户对象
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //判断与用户的绑定状态
        String bindCode = userInfo.getBindCode();
        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        //创建汇付宝提交表单
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID); //商户分配的唯一标识
        paramMap.put("agentBillNo", LendNoUtils.getNo()); //商户充值单号（要求唯一）
        paramMap.put("bindCode", bindCode);  //用户绑定码
        paramMap.put("chargeAmt", chargeAmt);  //用户充值金额
        paramMap.put("feeAmt", new BigDecimal("0")); //冻结资金
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);// 异步回调函数的路径
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL); //同步回调函数的路径
        paramMap.put("timestamp", RequestHelper.getTimestamp()); //时间戳
        String sign = RequestHelper.getSign(paramMap); //
        paramMap.put("sign", sign); //验签参数

        // 构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);

        return formStr;
    }

    /**
     * hfb充值异步回调函数
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String hfbNotify(Map<String, Object> map) {
        //修改用户账号的绑定协议和充值金额
        String bindCode = (String) map.get("bindCode");
        String chargeAmt = (String) map.get("chargeAmt");
        String agentBillNo = (String) map.get("agentBillNo");
        //查询用户账户是否已经有该条充值的充值单号，如果有直接返回，不做修改，解决幂等性的问题
        Boolean flag = transFlowService.isSaveTransFlow(agentBillNo);

        if(flag){
            return "success";
        }

        baseMapper.updateAccount(bindCode,new BigDecimal(chargeAmt),new BigDecimal(0));

        //增加交易流水
        TransFlowBO transFlowBO = new TransFlowBO(agentBillNo,bindCode,new BigDecimal(chargeAmt), TransTypeEnum.RECHARGE,"充值");
        transFlowService.saveTransFlow(transFlowBO);
        return "success";



    }



























}
