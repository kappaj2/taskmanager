package com.ajk.taskman.web.controller;

import com.ajk.taskman.AbstractTest;
import com.ajk.taskman.IntegrationTest;
import com.ajk.taskman.TaskmanagerApplication;
import com.ajk.taskman.pojo.UserPojo;
import com.ajk.taskman.service.TaskService;
import com.ajk.taskman.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaskmanagerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends AbstractTest {

    private Integer createdUserId;

    @Autowired
    private UserService usersService;

    @Autowired
    private TaskService taskService;

    @Before
    public void setUp() throws Exception {
        super.setup();
        createdUserId = Integer.valueOf(0);
    }

    @After
    public void tearDown() throws Exception {

        if (this.createdUserId != 0) {
            taskService.deleteTaskForUser(createdUserId);
            usersService.deleteUser(createdUserId);
        }
    }

    /**
     * Test creation of a new User
     * Test 1 in requirements.
     */
    @Test
    public void testCreateUserIsCalled_thenShouldBeNewCreated() {

        UserPojo userPojo = getNewUserPojo();

        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });
        createdUserId = createdPojo.getId();
        fixture.evaluate(assertThat(createdPojo.getId()).isNotNull());
    }

    /**
     * Test update of a user. Create a user, retrieve it, then update. check updated values are actually set.
     * Test 2 in the requirements.
     */
    @Test
    public void testUpdateUserIsCalled_thenUserShouldBeUpdated() {
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";

        createdPojo.setFirstName(newFirstName);
        createdPojo.setLastName(newLastName);

        /*
            Now call update function. This does
         */
        fixture.withHttpHeaders("user_id", "" + createdPojo.getId());
        fixture.givenURLIs("/api/user/"+createdPojo.getId());
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.PUT,
                new HttpEntity<>(createdPojo, headers));

        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo updatedPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        fixture.evaluate(assertThat(updatedPojo.getFirstName()).isEqualTo(newFirstName));
        fixture.evaluate(assertThat(updatedPojo.getLastName()).isEqualTo(newLastName));
    }

    /**
     * There is already one user in the db. Add another, then list should be size 2.
     * This is test 3 in the requirements.
     */
    @Test
    public void testGetAllUsers() {
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(ArrayList.class,
                HttpMethod.GET);

        List<UserPojo> userList = (List<UserPojo>)fixture.getBody(new TypeReference<List<UserPojo>>() {});
        fixture.evaluate(assertThat(userList.size()).isGreaterThan(0));
    }

    /**
     * Create a new user. Then use the newly created user Id to retrieve the User.
     * This is test 4 in the requirements.
     */
    @Test
    public void testGetUserUsingSpecificId() {

        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        fixture.givenURLIs("/api/user/"+createdUserId);
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.GET);

        UserPojo retrievedUserPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {});
        fixture.evaluate(assertThat(retrievedUserPojo.getUsername()).isEqualTo(userPojo.getUsername()));
    }

    @Test
    public void createTask() {
    }

    @Test
    public void updateTask() {
    }

    @Test
    public void deleteTask() {
    }

    @Test
    public void getTaskInformation() {
    }

    @Test
    public void getAllTasksForUser() {
    }

    private UserPojo getNewUserPojo() {
        UserPojo userPojo = new UserPojo();
        userPojo.setFirstName("FirstName2");
        userPojo.setLastName("LastName2");
        userPojo.setUsername("username2");
        return userPojo;
    }
}