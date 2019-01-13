package com.ajk.taskman.mapper;

import com.ajk.taskman.model.Task;
import com.ajk.taskman.pojo.TaskPojo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class TaskMapper extends AbstractMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "task", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "dateTime", target = "dateTime")
    })
    public abstract TaskPojo asPojo(Task task);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "task"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "dateTime", target = "dateTime")
    })
    public abstract Task asModel(TaskPojo taskPojo);

}
