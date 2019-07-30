package com.miaoshaproject.config;

import com.miaoshaproject.domain.MiaoshaUser;
import com.miaoshaproject.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cyx
 * @data 2019/4/3 10:19
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz= methodParameter.getParameterType();
        return clazz== MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request  = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken=request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken=getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);

        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return null;
        }

        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        MiaoshaUser user=miaoshaUserService.getByToken(response,token);
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length<=0){
            return  null;
        }
        for(Cookie cookie :cookies){
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
