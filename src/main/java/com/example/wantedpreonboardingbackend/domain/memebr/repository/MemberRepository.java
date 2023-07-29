package com.example.wantedpreonboardingbackend.domain.memebr.repository;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
