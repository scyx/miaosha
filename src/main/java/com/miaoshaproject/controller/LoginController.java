package com.miaoshaproject.controller;

import com.miaoshaproject.Vo.LoginVo;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import com.miaoshaproject.service.MiaoshaUserService;
import com.miaoshaproject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author cyx
 * @data 2019/3/15 13:25
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;
    private static Logger log = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping("/to_login")
    public String toLogin(){

        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public  Result<String> doLogin(@Valid LoginVo loginvo, HttpServletResponse response){
        log.info(loginvo.toString());
        String token= miaoshaUserService.login(response,loginvo);
        return Result.success(token);
    }




}
