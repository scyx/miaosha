package com.miaoshaproject.controller;

import com.miaoshaproject.Util.MD5util;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.exception.GlobalException;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * @author cyx
 * @data 2019/7/27 14:29
 */
@Controller
@RequestMapping("/registe")
public class RegisteController {
    private static Logger log = LoggerFactory.getLogger(RegisteController.class);
    @Autowired
    MiaoshaUserService miaoshaUserService;
    /**
     * 去注册页面
     * @return
     */
    @RequestMapping("/to_registered")
    public String toregiste(){
        return "registe";
    }

    /**
     * 注册
     * @return
     */
    @RequestMapping(value = "/doregiste",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doregiste(@RequestParam("mobile")String mobile,
                                     @RequestParam("password")String password){

        MiaoshaUser miaoshaUser=miaoshaUserService.getByMobile(Long.parseLong(mobile));
        if(miaoshaUser!=null){
            return Result.error(CodeMsg.MOBILE_REPEAT_ERROR);
        }
        MiaoshaUser  newuser = new MiaoshaUser();
        newuser.setId(Long.parseLong(mobile));
        newuser.setNickname("user"+mobile);
        newuser.setRegisterDate(new Date());
        newuser.setSalt("1a2b3c");
        newuser.setLoginCount(1);
        String saltDb="1a2b3c";
        String dbpass= MD5util.formPassToDbPass(password,saltDb);
        newuser.setPassword(dbpass);
        int res = miaoshaUserService.registe(newuser);
        if(res>0){
            return Result.success(0);
        }
        return Result.error(CodeMsg.REGISTE_FAIL);
    }




}
