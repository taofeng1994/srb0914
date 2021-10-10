package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.mapper.UserBindMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {


    @Resource
    UserInfoMapper userInfoMapper;


    @Override
    public String commitBindUser(UserBindVO userBindVO, Long userId) {
        //1、检查用户身份证在尚融宝是否已经绑定
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id_card",userBindVO.getIdCard())
                .ne("user_id",userId); //ne表示不等于

        UserBind userBind = baseMapper.selectOne(queryWrapper);
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        //查询用户绑定信息
         queryWrapper = new QueryWrapper<>();
         queryWrapper.eq("user_id",userId);
         userBind = baseMapper.selectOne(queryWrapper);

         //判断用户是否有绑定信息
        if (userBind == null) {
            //用户未绑定，创建一条绑定记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO,userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());//只是创建一条绑定记录，所以status是未板吊顶状态
            baseMapper.insert(userBind);
        }else {
            //如果有绑定记录，则更新绑定记录
            BeanUtils.copyProperties(userBindVO,userBind);
            baseMapper.updateById(userBind);
        }

        //创建汇付宝提交表单

        HashMap<String, Object> paramMap = new HashMap<>();

        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard",userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);

        return formStr;


    }

    /**
     * 绑定异步回调
     * @param map
     */
    @Override
    public void hfbNotify(Map<String, Object> map) {

        //获取会员id和绑定码
        String agentUserId = (String) map.get("agentUserId");
        String bindCode = (String) map.get("bindCode");

        //更新用户绑定表
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",agentUserId);

        UserBind userBind = baseMapper.selectOne(queryWrapper);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        userBind.setBindCode(bindCode);
        baseMapper.updateById(userBind);

        //更新用户信息表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);


    }

    @Override
    public boolean isBind(String agentUserId) {
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("user_id",agentUserId);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        if (userBind.getStatus() == 1) {
            return true;
        }

            return false;
    }




































































}
