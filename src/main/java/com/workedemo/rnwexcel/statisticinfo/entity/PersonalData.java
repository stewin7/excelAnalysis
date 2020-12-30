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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason;
    }
}
