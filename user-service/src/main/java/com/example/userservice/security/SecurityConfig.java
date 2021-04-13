package com.example.userservice.security;

import com.example.userservice.security.filter.AuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/users/**")
//                .permitAll()
                .hasIpAddress("")
        .and()
                .addFilter(getAuthenticationFilter())
        ;

        // H2 데이터베이스는 HTML 프레임으로 데이터가 나누어져 있다 -> 무시
        http
                .headers().frameOptions().disable();
    }

    /**
     * 인증 필터 적용
     */
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }

}
