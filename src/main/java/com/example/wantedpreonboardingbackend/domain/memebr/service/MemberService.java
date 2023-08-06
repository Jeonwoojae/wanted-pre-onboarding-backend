package com.example.wantedpreonboardingbackend.domain.memebr.service;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.MemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.memebr.repository.MemberRepository;
import com.example.wantedpreonboardingbackend.global.error.exception.ConversionFailedException;
import com.example.wantedpreonboardingbackend.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void createNewMember(RequestJoinMemberDto dto) {
        if(memberRepository.existsByEmail(dto.getEmail())){
            // 추가적인 유효성 검사를 피하기 위해 중복 이메일일 경우 pass
            return;
        }

        Member newMember = Member.builder()
                .email(dto.getEmail())
                .encryptedPassword(passwordEncoder.encode(dto.getPassword()))
                .build();
        memberRepository.save(newMember);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByEmail(username)
                .orElseThrow(()->new EntityNotFoundException("사용자를 찾을 수 없음."));
        return new User(memberEntity.getEmail(), memberEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public MemberDto getUserByEmail(String userEmail) {
        Member memberEntity = memberRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("사용자를 찾을 수 없음."));

        MemberDto response = Optional.ofNullable(memberEntity)
                .map(MemberDto::new)
                .orElseThrow(()->new ConversionFailedException("entity -> dto 변환 에러"));

        return response;
    }
}
