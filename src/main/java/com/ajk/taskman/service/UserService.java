package com.ajk.taskman.service;

import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.UserPojo;

import java.util.List;

public interface UserService {

    UserPojo findUser(Integer userId);

    List<UserPojo> findAllUsers();

    UserPojo createUser(UserPojo userPojo);

    UserPojo updateUser(Integer userId, UserPojo userPojo);

    void deleteUser(Integer userId);
}
