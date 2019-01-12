package com.ajk.taskman.service.impl;

import com.ajk.taskman.exception.UserNotFoundException;
import com.ajk.taskman.mapper.UserMapper;
import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.UserPojo;
import com.ajk.taskman.repository.UsersRepository;
import com.ajk.taskman.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void loadTestUser() {

        Calendar cal = Calendar.getInstance();

        User users = new User();
        users.setCreatedAt(new Date(cal.getTime().getTime()));
        users.setFirstName("FirstName1");
        users.setLastName("LastName1");
        users.setModifiedAt(new Date(cal.getTime().getTime()));
        users.setUsername("UserName1");

        usersRepository.save(users);
    }

    /**
     * Find a specific user using the userId field
     *
     * @param userId
     * @return
     */
    @Override
    public UserPojo findUser(Integer userId) {
        return userMapper.asPojo(findUserById(userId));
    }

    /**
     * Retrieve all the users in the database.
     *
     * @return A List of UserPojo objects.
     */
    @Override
    public List<UserPojo> findAllUsers() {
        List<UserPojo> userPojoList = new ArrayList<>();
        List<User> userList = usersRepository.findAll();
        userList.stream().forEach(user -> {
            userPojoList.add(userMapper.asPojo(user));
        });
        return userPojoList;
    }

    /**
     * Create a new user using the supplied information. Add the auditing field values.
     *
     * @param userPojo
     * @return
     */
    @Override
    @Transactional
    public UserPojo createUser(UserPojo userPojo) {
        User user = userMapper.asModel(userPojo);
        user.setCreatedAt(new Date());
        user.setModifiedAt(new Date());
        return userMapper.asPojo(usersRepository.save(user));
    }

    /**
     * Update the user using the userId provided.
     *
     * @param userId   The userId to retrieve the user with.
     * @param userPojo New data fields to be used.
     * @return
     */
    @Override
    @Transactional
    public UserPojo updateUser(Integer userId, UserPojo userPojo) {
        User user = findUserById(userId);
        user.setFirstName(userPojo.getFirstName());
        user.setLastName(userPojo.getLastName());
        user.setModifiedAt(new Date());
        return userMapper.asPojo(usersRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        usersRepository.delete(findUserById(userId));
    }

    /**
     * Utility method common to many requirements - will also handle throw of not found;
     *
     * @param userId
     * @return
     */
    private User findUserById(Integer userId) {
        User user = usersRepository.getOne(userId);
        if (user == null) {
            throw new UserNotFoundException("Cannot fund user for id : " + userId);
        }
        return user;
    }
}
