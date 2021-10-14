package com.atguigu.srb.core.controller;


import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/api/core/userBind")
@Api(tags = "会员账户绑定")
public class UserBindController {

    @Resource
    private UserBindService userBindService;


    @PostMapping("/auth/bind")
    @ApiOperation(value = "账户绑定提交接口")
    public R bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request){
        //从token中拿到用户的信息
        String token = request.getHeader("token");
        Long userId = 0l;
        userId = JwtUtils.getUserId(token);

        String formStr = userBindService.commitBindUser(userBindVO,userId);

        return R.ok().data("formStr", formStr);

    }



    @PostMapping("/notify")
    @ApiOperation("账户绑定异步回调")
    public String hfbNotify(HttpServletRequest request){
        Map<String, Object> map = RequestHelper.switchMap(request.getParameterMap());
        //校验签名，看是否绑定成功
        if (!RequestHelper.isSignEquals(map)){
            //校验签名失败
            return "fail";
        }

        //校验签名成功
        String agentUserId = (String) map.get("agentUserId");
        boolean flag = userBindService.isBind(agentUserId);
        if (!flag) {
            userBindService.hfbNotify(map);
        }

        return "success";
    }


    @GetMapping("/auth/getbind")
    @ApiOperation("获取用户是否绑定接口")
    public R getBind(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = 0l;
        userId = JwtUtils.getUserId(token);
        boolean bind = userBindService.isBind(userId+"");
        return R.ok().data("status",bind);

    }































}

