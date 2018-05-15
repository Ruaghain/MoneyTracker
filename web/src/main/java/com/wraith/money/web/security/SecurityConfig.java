package com.wraith.money.web.security;

import com.wraith.money.web.authentication.AjaxAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.inject.Inject;

/**
 * User: rowan.massey Date: 14/08/2014 Time: 23:39
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    MoneyUserDetailsService moneyUserDetailsService;

    @Inject
    AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(moneyUserDetailsService).passwordEncoder(new StandardPasswordEncoder());
    }

    public void configure(WebSecurity web) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/accounts/**").hasRole("ADMIN")
                .antMatchers("/accountTypes/**").hasRole("ADMIN")
                .antMatchers("/addresses/**").hasRole("ADMIN")
                .antMatchers("/authorities/**").hasRole("ADMIN")
                .antMatchers("/countries/**").hasRole("ADMIN")
                .antMatchers("/currencies/**").hasRole("ADMIN")
                .antMatchers("/groups/**").hasRole("ADMIN")
                .antMatchers("/**").hasRole("USER")
                .and()
                .httpBasic()
                .realmName("MoneyTracker Realm via Basic Authentication")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable(); //Disabled CSRF, as this REST service won't exclusively be for browsers.

    }

    //Needed to expose this for use with integration tests.
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
