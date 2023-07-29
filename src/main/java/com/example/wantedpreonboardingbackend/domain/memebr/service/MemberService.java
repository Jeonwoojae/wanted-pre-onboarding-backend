package com.example.wantedpreonboardingbackend.domain.memebr.service;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.memebr.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createNewMember(RequestJoinMemberDto dto) {
        Member newMember = Member.builder()
                .email(dto.getEmail())
                .encrypted_password(passwordEncoder.encode(dto.getPassword()))
                .build();
        memberRepository.save(newMember);
    }
}
