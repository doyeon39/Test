package com.team4.backend.mapper;

import com.team4.backend.model.dto.EventDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface EventMapper {


    @Insert("insert into event(estart, end, title, memberId, groupName, groupId) values(#{start},#{end},#{title}, #{memberId}, #{groupName}, #{groupId})")
    void saveEvent(EventDTO event);

    @Select("SELECT last_insert_id();")
    int selectLast();

    @Select("SELECT id, estart, end, title, memberId, groupName, groupId from event where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupId", property = "groupId")
    })
    EventDTO viewEventById(int id);

    @Select("SELECT id, estart, end, title, memberId, groupName from event WHERE (month(estart) = #{year} OR month(end) = #{year}) and memberId=#{memberId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupId", property = "groupId")
    })
    List<EventDTO> listMonthly(@Param("year") int year, @Param("memberId") int memberId);


    @Select("SELECT * from EVENT WHERE (TO_CHAR(estart,'yyyy-mm-dd') = #{date} || TO_CHAR(DATE_SUB(end, INTERVAL 1 day),'yyyy-mm-dd') =  #{date}) and memberId=#{memberId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupName", property = "groupName"),
            @Result(column = "groupId", property = "groupId")
    })
    List<EventDTO> listDaily(@Param("date") String date, @Param("memberId") int memberId);

    @Delete("delete from event where (id = #{id} or groupId = #{id}) and memberId = #{memberId}")
    void deleteEvent(@Param("id") int id, @Param("memberId") int memberId);
}
