package com.wraith.money.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: rowan.massey
 * Date: 06/10/13
 * Time: 18:59
 */
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private AuthenticationSuccessHandler defaultHandler;

    public AjaxAuthenticationSuccessHandler() {
    }

    public AjaxAuthenticationSuccessHandler(AuthenticationSuccessHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (request.getHeader("x-ajax-call") != null && request.getHeader("x-ajax-call").equalsIgnoreCase("true")) {
            response.getWriter().print("ok");
            response.getWriter().flush();
        } else {
            defaultHandler.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
