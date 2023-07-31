package com.example.wantedpreonboardingbackend.domain.memebr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogin {
    private String email;
    private String password;
}
