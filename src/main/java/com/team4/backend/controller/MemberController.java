package com.team4.backend.controller;


import com.team4.backend.model.dto.MemberDTO;
import com.team4.backend.model.dto.ResultDTO;
import com.team4.backend.service.EmailService;
import com.team4.backend.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class MemberController {

    ResultDTO resultDTO;

    private final EmailService emailService;
    private final MemberService memberService;

    public MemberController(EmailService emailService, MemberService memberService) {
        this.emailService = emailService;
        this.memberService = memberService;
    }
    @GetMapping("/join")
    public ResponseEntity<?> getJoin(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/login")
    public ResponseEntity<?> getLogin(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/join/signUp")
    public ResponseEntity<?> join(@RequestBody Map<String, String> params) {
        if (emailService.isExist(params.get("email"), params.get("emailAuthCode"))) {
            MemberDTO memberDTO = MemberDTO.builder()
                    .email(params.get("email"))
                    .username(params.get("username"))
                    .password(params.get("password"))
                    .build();
            resultDTO = memberService.join(memberDTO);
        } else {
            resultDTO = ResultDTO.builder()
                    .status(false)
                    .message("이메일 인증된 유저정보가 아닙니다. 다시 입력해주세요")
                    .build();
        }
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
