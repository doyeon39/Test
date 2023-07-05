package com.team4.backend.controller;

import com.team4.backend.model.dto.EmailAuthenticationDTO;
import com.team4.backend.model.dto.MailForm;
import com.team4.backend.model.dto.ResultDTO;
import com.team4.backend.service.EmailService;
import com.team4.backend.service.MemberService;
import com.team4.backend.util.CodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class EmailController {

    private final EmailService emailService;
    private final MemberService memberService;
    ResultDTO resultDTO;

    public EmailController(EmailService emailService,MemberService memberService) {
        this.emailService = emailService;
        this.memberService = memberService;
    }

    @PostMapping("/join/sendMail")
    public ResponseEntity<ResultDTO> sendMail(@RequestBody Map<String,String> params) throws UnsupportedEncodingException {
        System.out.println("isMemberExist:"+memberService.isMemberExist(params.get("isEmail")));

        if (memberService.isMemberExist(params.get("isEmail"))){
            resultDTO = ResultDTO.builder()
                    .status(false)
                    .message("이미 존재하는 유저입니다.")
                    .build();
            return new ResponseEntity<>(resultDTO,HttpStatus.OK);
        }
        String authCode = CodeGenerator.createCode();
        EmailAuthenticationDTO auth = new EmailAuthenticationDTO();
        auth.setEmail(params.get("isEmail"));
        auth.setAuth(authCode);
        emailService.save(auth);

        MailForm form = new MailForm();
        form.setFrom("meatTeam@gmail.com");
        form.setTo(params.get("isEmail"));
        form.setSubject("회원 가입 인증 이메일 입니다.");
        form.setText(authCode);
        System.out.println(form);
        try {
            emailService.sendMail(form);
            resultDTO = ResultDTO.builder()
                    .result(auth)
                    .message("인증번호가 발송되었습니다.")
                    .status(true)
                    .build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/join/checkMail")
    public ResponseEntity<?> checkMail(@RequestBody Map<String,String> params)throws UnsupportedEncodingException  {

        EmailAuthenticationDTO auth = new EmailAuthenticationDTO();
        auth.setAuth(params.get("isEmailAuthCode"));
        auth.setEmail(params.get("isEmail"));
        System.out.println("auth: "+ auth);
        return new ResponseEntity<>(emailService.isExist(auth),HttpStatus.OK);
    }
}