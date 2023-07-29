package com.example.wantedpreonboardingbackend.domain.memebr.api;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberService memberService;
    @PostMapping("/members")
    public ResponseEntity<Void> joinNewMember(@Valid @RequestBody RequestJoinMemberDto requestJoinMemberDto){
        memberService.createNewMember(requestJoinMemberDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
