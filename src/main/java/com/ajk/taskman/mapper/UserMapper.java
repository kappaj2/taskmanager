package com.ajk.taskman.mapper;

import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.UserPojo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends AbstractMapper {

    @Mappings({
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "id", target = "id")
    })
    public abstract User asModel(UserPojo userPojo);

    @Mappings({
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "id", target = "id")
    })
    public abstract UserPojo asPojo(User user);

}
