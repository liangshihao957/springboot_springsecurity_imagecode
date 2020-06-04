package org.example.exception;

import org.springframework.security.core.AuthenticationException;




//校验验证码
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 2672899097153524723L;



    public ValidateCodeException(String explanation) {
        super(explanation);
    }


}