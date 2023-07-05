package com.team4.backend.util;

import com.team4.backend.model.Member;
import com.team4.backend.model.ResultMember;
import com.team4.backend.model.dto.ResultDtoProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class UserUtil {

    public static String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    public static boolean jwtStatus(HttpServletRequest request) {
        return (boolean) request.getAttribute(ResultDtoProperties.STATUS);
    }

    public static String jwtMessage(HttpServletRequest request) {
        return (String) request.getAttribute(ResultDtoProperties.MESSAGE);
    }

    //파일경로를 base64로 변환
    public static byte[] pathToBytes(String filePath){
        File file = new File(filePath);
        byte[] result = null;
        try {
            result = FileCopyUtils.copyToByteArray(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ResultMember memberToReturn(Member member){
        ResultMember resultMember = new ResultMember();
        if(!member.getUser_icon_url().equals("")){
            resultMember.setUser_icon_url(pathToBytes(member.getUser_icon_url()));
        }
        resultMember.setRole(member.getRole());
        resultMember.setUsername(member.getUsername());
        resultMember.setEmail(member.getEmail());
        resultMember.setJoin_date(member.getJoin_date());
        resultMember.setUser_description(member.getUser_description());
        return resultMember;
    }


}
