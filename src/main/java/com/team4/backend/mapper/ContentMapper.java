package com.team4.backend.mapper;

import com.team4.backend.model.dto.ContentDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentMapper {

    @Select("select * from content where writer_id = #{userUID} and id < #{pageNum} order by id desc limit 10")
    List<ContentDTO> listContent(@Param("pageNum") int pageNum, @Param("userUID") int userUID);

    @Insert("insert into content(writer_id ,title,  content, contentIMG, isImgIn, sharingCode) values(#{content.writer_id},#{content.title}, #{content.content}, #{content.contentIMG}, #{content.isImgIn}, #{code})")
    void saveContent(@Param("content") ContentDTO content, @Param("code") String code);

    @Select("select * from content ")
    List<ContentDTO> findAll();
}
