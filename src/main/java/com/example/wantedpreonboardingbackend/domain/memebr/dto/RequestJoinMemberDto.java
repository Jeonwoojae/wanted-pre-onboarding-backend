package com.example.wantedpreonboardingbackend.domain.memebr.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestJoinMemberDto {
    @Email(message = "check your email")
    @NotBlank
    private String email;

    @Min(message = "password must be longer than 8",value = 8)
    private String password;
}
