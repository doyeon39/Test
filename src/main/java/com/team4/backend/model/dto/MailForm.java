package com.team4.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
}
