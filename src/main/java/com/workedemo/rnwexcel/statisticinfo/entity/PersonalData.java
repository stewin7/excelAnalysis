package com.workedemo.rnwexcel.statisticinfo.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PersonalData {
    private String name;
    private String isOut;
    private String location;
    private String outReason;
}
