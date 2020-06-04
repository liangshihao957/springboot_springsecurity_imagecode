package org.example.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义成功的逻辑
//要改变默认的处理成功逻辑很简单，只需要实现org.springframework.security.web.authentication.AuthenticationSuccessHandler接口的onAuthenticationSuccess方法即可：
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 默认打印出登陆信息
        //httpServletResponse.setContentType("application/json;charset=utf-8");
        //httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
        // 跳转访问页面
        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
        //访问login.html 时，saverequest为 null
        if(savedRequest==null){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse,"/hello" );
            return;
        }
        //直接访问 login的时候，就跳转到hello页面（或者自定义一个主页面皆可）
        if(savedRequest.getRedirectUrl().endsWith("login")){
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse,"/hello" );
            return;
        }
        //直接访问 .html 资源时 ，查看是否有指定的请求路径，重定向过去
        if(savedRequest.getRedirectUrl().endsWith("html")){
            String str=savedRequest.getRedirectUrl().replace(".html","");
            int legth=str.lastIndexOf("/");
            str=str.substring(legth,str.length());
            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse,str );
            return;
        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, savedRequest.getRedirectUrl());
        // 跳转制定页面
//        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
//        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "index.html");
    }
}