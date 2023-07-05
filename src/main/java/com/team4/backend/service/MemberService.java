package com.team4.backend.service;

import com.team4.backend.model.Member;
import com.team4.backend.model.dto.MemberDTO;
import com.team4.backend.model.dto.MyChannelsDTO;
import com.team4.backend.model.dto.ResultDTO;
import com.team4.backend.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberMapper memberMapper,BCryptPasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResultDTO join(MemberDTO memberDTO) {

        if (isMemberExist(memberDTO.getEmail())){
            return ResultDTO.builder()
                    .status(false)
                    .message("이미 존재하는 유저입니다.")
                    .build();
        }

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberMapper.save(memberDTO);

        return ResultDTO.builder()
                .status(true)
                .message("회원가입 되었습니다.")
                .build();
    }

    public boolean isMemberExist(String isEmail) {
        return memberMapper.findMemberByEmail(isEmail).isPresent();
    }

    public Member getLobbyInfoByMemberUID(int memberUID) {
        return memberMapper.findMemberByUID(memberUID);
    }
}
