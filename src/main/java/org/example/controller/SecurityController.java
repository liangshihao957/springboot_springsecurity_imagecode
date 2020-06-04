package org.example.controller;


import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SecurityController {

    //为Spring Security提供的用于缓存请求的对象，通过调用它的getRequest方法可以获取到本次请求的HTTP信息
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    //要将登录的路径 设置为 .loginPage("/authentication/require")  (试了好久。。。。)
    @GetMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String url = savedRequest.getRedirectUrl();
            //上面代码获取了引发跳转的请求，根据请求是否以.html为结尾来对应不同的处理方法。如果是以.html结尾，那么重定向到登录页面，
            // 否则返回”访问的资源需要身份认证！”信息，并且HTTP状态码为401（HttpStatus.UNAUTHORIZED）。
            if (StringUtils.endsWithIgnoreCase(url, ".html")) {
                //DefaultRedirectStrategy的sendRedirect为Spring Security提供的用于处理重定向的方法。
                redirectStrategy.sendRedirect(request, response, "/login.html");
            }
            //这样的话，访问时/hello 会跳转到/authentication/require，并且输出"访问的资源需要身份认证！"
            //访问 /hello.html时，会跳转到登陆页面
        }
        return "访问资源需要身份认证";
    }
}