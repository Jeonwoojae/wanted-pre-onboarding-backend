package com.example.wantedpreonboardingbackend.domain.memebr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDto {
    @Email(message = "check your email")
    @NotBlank(message = "password must not be blank")
    private String email;

    @Size(message = "password must be longer than 8",min = 8)
    @NotBlank(message = "password must not be blank")
    private String password;
}
