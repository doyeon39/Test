package com.team4.backend.controller;

import com.team4.backend.mapper.MemberMapper;
import com.team4.backend.model.dto.EventDTO;
import com.team4.backend.model.dto.ResultDtoProperties;
import com.team4.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class EventController {

    private final EventService eventService;
    private final MemberMapper memberMapper;

    @Autowired
    public EventController(EventService eventService, MemberMapper memberMapper) {
        this.eventService = eventService;
        this.memberMapper = memberMapper;
    }

    @GetMapping("/friend/{id}")
    public int friendTest(@PathVariable("id") int id){
        return id;
    }

    @PostMapping("/event/saveFriendEvent")
    public int saveFriend(@RequestBody EventDTO eventOrigin, HttpServletRequest request) throws Exception {
        EventDTO event = eventService.viewEventById(eventOrigin.getId());
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        int memberId = event.getMemberId();
        event.setMemberId(memberUID);
        event.setGroupId(memberId);
        int id = eventService.saveEvent(event);
        return id;
    }

    @PostMapping("/event/saveEvent")
    public int save(@RequestBody EventDTO event, HttpServletRequest request) throws Exception {
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        event.setGroupName(memberMapper.findMemberByUID(memberUID).getUsername());
        event.setMemberId(memberUID);
        int id = eventService.saveEvent(event);
        return id;
    }

    @PostMapping("/event/deleteEvent")
    public void deleteEvent(@RequestBody EventDTO event,HttpServletRequest request) throws Exception {
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        eventService.deleteEvent(event.getId(),memberUID);
    }

    @ResponseBody
    @PostMapping("/event/listMonthly")
    public ResponseEntity<List<EventDTO>> listMonthly(@RequestBody String date, HttpServletRequest request){
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        String year = date.substring(14,16);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(year), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyFriend")
    public ResponseEntity<List<EventDTO>> listMonthlyFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =Integer.parseInt(params.get("id"));
        String year = params.get("date");
        String date = year.substring(5,7);
        System.out.println(date);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(date), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listByDate")
    public ResponseEntity<List<EventDTO>> listByDate(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        List<EventDTO> events= eventService.listDaily(Integer.parseInt(params.get("year")),Integer.parseInt(params.get("month")), Integer.parseInt(params.get("date")), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listByDateFriend")
    public ResponseEntity<List<EventDTO>> listByDateFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID = Integer.parseInt(params.get("id"));
        List<EventDTO> events= eventService.listDaily(Integer.parseInt(params.get("year")),Integer.parseInt(params.get("month")), Integer.parseInt(params.get("date")), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyBtn")
    public ResponseEntity<List<EventDTO>> listMonthlyNext(@RequestBody String date, HttpServletRequest request){
        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
        String year = date.substring(14,16);
        System.out.println(Integer.parseInt(year)+1);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(year)+1, memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyBtnFriend")
    public ResponseEntity<List<EventDTO>> listMonthlyNextFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =Integer.parseInt(params.get("id"));
        String year = params.get("date");
        String date = year.substring(5,7);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(date)+1, memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


}
