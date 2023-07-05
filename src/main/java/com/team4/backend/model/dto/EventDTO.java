package com.team4.backend.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private int id;
    private Date start;
    private Date end;
    private String title;
    private int memberId;
    private String groupName;
    private int groupId;
}
