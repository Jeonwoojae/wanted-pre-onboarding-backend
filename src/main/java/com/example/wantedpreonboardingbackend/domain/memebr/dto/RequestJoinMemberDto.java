package com.example.wantedpreonboardingbackend.domain.memebr.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestJoinMemberDto {
    @Email(message = "check your email")
    @NotBlank(message = "password must not be blank")
    private String email;

    @Size(message = "password must be longer than 8",min = 8)
    @NotBlank(message = "password must not be blank")
    private String password;
}
