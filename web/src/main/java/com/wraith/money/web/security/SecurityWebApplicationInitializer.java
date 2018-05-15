package com.wraith.money.web.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * User: rowan.massey Date: 19/08/2014 Time: 20:11
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    //This is needed for the session management, maximum sessions.
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }

}