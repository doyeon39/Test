package com.team4.backend.controller;

import com.team4.backend.mapper.MemberMapper;
import com.team4.backend.model.ResultContent;
import com.team4.backend.model.dto.ContentDTO;
import com.team4.backend.model.dto.ResultDtoProperties;
import com.team4.backend.service.ContentService;
import com.team4.backend.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class ContentController {

    @Autowired
    public ContentController(ContentService contentService, MemberMapper memberMapper){this.contentService = contentService;
        this.memberMapper = memberMapper;
    }

    private final ContentService contentService;
    private final MemberMapper memberMapper;

    //글 불러오기(페이징)
    @ResponseBody
    @PostMapping("/content/listByPage")
    public List<ResultContent> listContent(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        int pageNum=Integer.parseInt(params.get("lastPosting"));
        if(pageNum==0){
            pageNum = 100000000;
        }
        List<ContentDTO> contents = contentService.listContent(pageNum, memberUID);
        List<ResultContent> returnContents = new ArrayList<>();
        for (ContentDTO content: contents) {
            returnContents.add(contentToReturn(content));
        }
        return returnContents;
    }

    @ResponseBody
    @PostMapping("/content/listByPageFriend")
    public List<ResultContent> listContentFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID = Integer.parseInt(params.get("id"));
        int pageNum=Integer.parseInt(params.get("lastPosting"));
        if(pageNum==0){
            pageNum = 100000000;
        }
        List<ContentDTO> contents = contentService.listContent(pageNum, memberUID);
        List<ResultContent> returnContents = new ArrayList<>();
        for (ContentDTO content: contents) {
            returnContents.add(contentToReturn(content));
        }
        return returnContents;
    }

    //컨텐츠반환용으로 변환
    public ResultContent contentToReturn(ContentDTO content){
        ResultContent RContent = new ResultContent();
        RContent.setId(content.getId());
        if(content.isImgIn()){
            RContent.setContentIMG(UserUtil.pathToBytes(content.getContentIMG()));
        }
        RContent.setTitle(content.getTitle());
        RContent.setVisible(content.isVisible());
        if(content.isImgIn()){
            RContent.setIsImgIn(1);
        }else{
            RContent.setIsImgIn(0);
        }
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        RContent.setUploadDate(transFormat.format(content.getUploadDate()));
        RContent.setWriter_id(content.getWriter_id());
        RContent.setContent(content.getContent());
        RContent.setUsername(memberMapper.findMemberByUID(content.getWriter_id()).getUsername());
        if(!memberMapper.findMemberByUID(content.getWriter_id()).getUser_icon_url().equals("")){
            RContent.setUserIcon(UserUtil.pathToBytes(memberMapper.findMemberByUID(content.getWriter_id()).getUser_icon_url()));
        }
        RContent.setSharingCode(content.getSharingCode());
        return RContent;
    }

    //글 저장
    @ResponseBody
    @PostMapping("/content/saveContent")
    public void saveQuill(@RequestBody Map<String,String> params, HttpServletRequest request) throws Exception{
        ContentDTO content = new ContentDTO();
        content.setTitle(params.get("title"));
        System.out.println(params.get("isImgIn").substring(0,1));
        if(params.get("isImgIn").substring(0,1).equals("t")){
            content.setImgIn(true);
            String base64Data=params.get("contentIMG");
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
            String uploadFolder = "C:\\upload\\images\\users\\"+ UserUtil.getEmail().substring(0,UserUtil.getEmail().lastIndexOf("@"))+"\\contents\\";
            new File(uploadFolder).mkdirs();
            File outputfile = new File(uploadFolder+fileName);
            ImageIO.write(image, "png", outputfile);
            content.setContentIMG(uploadFolder+fileName);
        }else {
            content.setImgIn(false);
        }
        content.setContent(params.get("context"));
        content.setWriter_id((int) request.getAttribute(ResultDtoProperties.USER_UID));
        contentService.saveContent(content);
    }


    //이미지 글쓰기창에 불러오기
    @ResponseBody
    @GetMapping(value = "api/v1/home/display")
    public ResponseEntity<byte[]> showImageGET(
            @RequestParam("fileName") String fileName
    ) {
        File file = new File("C:\\upload\\images\\" + fileName);

        ResponseEntity<byte[]> result = null;

        try {

            HttpHeaders header = new HttpHeaders();

        /*
        Files.probeContentType() 해당 파일의 Content 타입을 인식(image, text/plain ...)
        없으면 null 반환

        file.toPath() -> file 객체를 Path객체로 변환

        */
            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
