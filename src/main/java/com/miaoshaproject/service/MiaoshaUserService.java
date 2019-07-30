package com.miaoshaproject.service;

import com.miaoshaproject.Util.MD5util;
import com.miaoshaproject.Util.UUIDUtil;
import com.miaoshaproject.Vo.LoginVo;
import com.miaoshaproject.dao.MiaoshaUserDao;
import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.exception.GlobalException;
import com.miaoshaproject.redis.MiaoshaUserKey;
import com.miaoshaproject.redis.RedisService;
import com.miaoshaproject.result.CodeMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.CertPathParameters;

/**
 * @author cyx
 * @data 2019/3/15 14:36
 */
@Service
public class MiaoshaUserService {
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN="token";

    public MiaoshaUser getByMobile(long id){
        //取缓存
        MiaoshaUser user=redisService.get(MiaoshaUserKey.getByid,""+id,MiaoshaUser.class);
        //取到了就返回
        if(user!=null){
            return user;
        }
        //没取到 去数据库取
        user=miaoshaUserDao.getById(id);
        //取完了存缓存
        if(user!=null){
            redisService.set(MiaoshaUserKey.getByid,""+id,user);
        }
        return user;
    }

    /**
     * 登录
     * @param response
     * @param loginVo
     * @return
     */
    public String login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formpass=loginVo.getPassword();
        MiaoshaUser user=getByMobile(Long.parseLong(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbpass=user.getPassword();
        String saltDB=user.getSalt();
        String calcpass= MD5util.formPassToDbPass(formpass,saltDB);
        if(!calcpass.equals(dbpass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成UUID作为token
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return token;
    }

    /**
     * 根据Token取用户
     * @param response
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
           MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
            if(user!=null){
                addCookie(response,token,user);
            }
            return user;
    }

    /**
     * 添加cookie
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie=new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 注册
     * @param newuser
     * @return
     */
    public int  registe(MiaoshaUser newuser){
        return miaoshaUserDao.insertUser(newuser);
    }
}
