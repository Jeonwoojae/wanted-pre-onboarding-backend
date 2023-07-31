package com.example.wantedpreonboardingbackend.domain.memebr.dto;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;

    // 수동 mapper용 생성자
    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getEncryptedPassword();
    }
}
