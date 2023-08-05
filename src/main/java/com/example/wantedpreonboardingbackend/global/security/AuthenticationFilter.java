package com.example.wantedpreonboardingbackend.global.security;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.MemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestLogin;
import com.example.wantedpreonboardingbackend.domain.memebr.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    private final Validator validator;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                MemberService memberService,
                                TokenProvider tokenProvider,
                                Validator validator) {
        super.setAuthenticationManager(authenticationManager);
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.validator = validator;
    }
    /**
     * 로그인하면 제일 먼저 실행
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            BindingResult bindingResult = new BeanPropertyBindingResult(creds, "requestLogin");
            validator.validate(creds, bindingResult);

            if (bindingResult.hasErrors()) {
                throw new InvalidParameterException("로그인 양식에 맞지 않습니다.");
            }

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                            // 역할 정보
                    )
            );
            //사용자로부터 입력 받은 정보를 토큰으로 바꿔서 매니저로 넘기면
            // 아이디와 패스워드를 비교하겠다는 것
        } catch (IOException e){
            throw new InvalidParameterException("로그인 폼에 맞지 않습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String userEmail = (((User)authResult.getPrincipal()).getUsername());
        MemberDto userDetails = memberService.getUserByEmail(userEmail);
        log.debug(userDetails.toString());

        String accessToken = tokenProvider.createToken(userDetails.getId().toString());

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
