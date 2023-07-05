package com.team4.backend.service;

import com.team4.backend.mapper.EventMapper;
import com.team4.backend.model.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;


    public List<EventDTO> listMonthly(int year, int memberId) {
        System.out.println(memberId);
        System.out.println(eventMapper.listMonthly(year, memberId));
        return eventMapper.listMonthly(year, memberId);
    }

    public int saveEvent(EventDTO event) {
        eventMapper.saveEvent(event);
        return eventMapper.selectLast();
    }

    public EventDTO viewEventById(int id){
        return eventMapper.viewEventById(id);
    }

    public void deleteEvent(int id, int memberId) {
        eventMapper.deleteEvent(id,memberId);
    }

    public List<EventDTO> listDaily(int year,int month, int date, int memberId) {
        String sMonth = "";
        if(month<10){
            sMonth = "0"+Integer.toString(month);
        }else{
            sMonth = Integer.toString(month);
        }
        String sDate = "";
        if(date<10){
            sDate = "0"+Integer.toString(date);
        }else{
            sDate = Integer.toString(date);
        }
        String fullDate = Integer.toString(year)+"-"+sMonth+"-"+sDate;
        return eventMapper.listDaily(fullDate,memberId);
    }
}
