package com.team4.backend.service;

import com.team4.backend.mapper.MemberMapper;
import com.team4.backend.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberMapper memberMapper;


    public void updateProfile(Member member,int memberUID) {

        memberMapper.updateProfile(member,memberUID);
    }



}
