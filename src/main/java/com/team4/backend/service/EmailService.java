package com.team4.backend.service;

import com.team4.backend.model.dto.EmailAuthenticationDTO;
import com.team4.backend.model.dto.MailForm;
import com.team4.backend.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.springframework.boot.autoconfigure.thymeleaf")
public class EmailService {

    private final EmailMapper emailMapper;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine; //인텔리제이 버그터진거 정상적으로 잘나오면 진행하면된다.

    public void save(EmailAuthenticationDTO auth){
        System.out.println(auth);
        boolean result = emailMapper.findEmail(auth.getEmail()).isPresent();
        System.out.println(result);
        if (result){
            emailMapper.update(auth);
        }else{
            emailMapper.save(auth);
        }
    }

    public Boolean isExist(EmailAuthenticationDTO auth){
        return emailMapper.findAuth(auth).isPresent();
    }
    public Boolean isExist(String email, String auth){
        System.out.println("email: "+ email+" auth: "+auth);
        System.out.println(emailMapper.findAuth2(email,auth));
        return emailMapper.findAuth2(email,auth).isPresent();
    }

    @Async("signUpEmailSender")
    public void sendMail(MailForm form) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(form.getFrom(),"meatTeam"));
        message.addRecipients(MimeMessage.RecipientType.TO,form.getTo()); //보낼 사람
        message.setSubject(form.getSubject()); //타이틀
        message.setText(setContext(form.getText()),"utf-8","html"); //내용
        mailSender.send(message);
    }

    public String setContext(String authCode){
        Context context = new Context();
        context.setVariable("authCode",authCode); //데이터
        return templateEngine.process("email/signup_templates",context);
    }
}
