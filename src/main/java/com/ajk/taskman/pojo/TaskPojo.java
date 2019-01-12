package com.ajk.taskman.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPojo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date_time")
    private Date dateTime;

}
