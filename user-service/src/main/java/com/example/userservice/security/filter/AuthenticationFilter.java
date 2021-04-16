package com.example.userservice.security.filter;

import com.example.userservice.user.UserService;
import com.example.userservice.user.dto.RequestLogin;
import com.example.userservice.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ModelMapper modelMapper;
    private UserService userService;

    @Value("${token.expiration_time}")
    private String tokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    public AuthenticationFilter() {

    }

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, ModelMapper modelMapper, UserService userService) {
        super.setAuthenticationManager(authenticationManager);
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // 사용자의 입력값
            RequestLogin requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 토큰 생성 후, 매니저에게 인증 위임 !
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword(), new ArrayList<>());
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        String userName = ((org.springframework.security.core.userdetails.User)authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
