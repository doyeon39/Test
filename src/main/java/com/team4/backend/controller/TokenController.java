package com.team4.backend.controller;

import com.team4.backend.model.dto.ResultDTO;
import com.team4.backend.model.dto.ResultDtoProperties;
import com.team4.backend.service.JwtService;
import com.team4.backend.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerProperties.API_VERSION)
public class TokenController {

    private final JwtService jwtService;
    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken() {
        return new ResponseEntity<>(jwtService.getResult(true, "accessJwt 인증성공"), HttpStatus.OK);
    }

    @PostMapping("/check-refreshToken")
    public ResponseEntity<?> checkRefreshToken() {
        return new ResponseEntity<>(jwtService.getResult(true, "토큰 재발급 성공"), HttpStatus.OK);
    }
}
