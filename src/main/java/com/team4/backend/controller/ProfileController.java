package com.team4.backend.controller;

import com.team4.backend.model.Member;
import com.team4.backend.model.dto.ResultDtoProperties;
import com.team4.backend.service.ProfileService;
import com.team4.backend.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class ProfileController {

    @Autowired
    public ProfileController(ProfileService profileService){this.profileService = profileService;}

    private final ProfileService profileService;

   @PostMapping("/profile/updateProfile")
    public void updateProfile(@RequestBody Map<String,String> params, HttpServletRequest request) throws IOException {
        //img 파일로 변환 및 저장
       String base64Data=params.get("user_icon_url");
       String base64 = base64Data.substring(base64Data.lastIndexOf(",")+1);
       BufferedImage image = null;
       byte[] imageByte;

       Base64.Decoder decoder =  Base64.getDecoder();
       imageByte = decoder.decode(base64);
       ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
       image = ImageIO.read(bis);
       bis.close();

       //파일명
       String fileName = base64.substring(30,50)+".png";

       String uploadFolder = "C:\\upload\\images\\users\\"+UserUtil.getEmail().substring(0,UserUtil.getEmail().lastIndexOf("@"))+"\\icons\\";
       File folder = new File(uploadFolder);
       folder.mkdirs();
       File outputfile = new File(uploadFolder+fileName);
       ImageIO.write(image, "png", outputfile);
       //저장완료

       Member member = new Member();

       member.setUsername(params.get("username"));
       member.setUser_description(params.get("user_description"));
       member.setUser_icon_url(uploadFolder+fileName);
       int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
       profileService.updateProfile(member, memberUID);

    }


}
