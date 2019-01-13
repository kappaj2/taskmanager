package com.ajk.taskman.web.controller;

import com.ajk.taskman.AbstractTest;
import com.ajk.taskman.IntegrationTest;
import com.ajk.taskman.TaskmanagerApplication;
import com.ajk.taskman.config.H2TestProfileJPAConfig;
import com.ajk.taskman.pojo.TaskPojo;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaskmanagerApplication.class, H2TestProfileJPAConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManControllerIntTest extends AbstractTest {

    private Integer createdUserId;
    private Integer createdTaskId;

    @Autowired
    private UserService usersService;

    @Autowired
    private TaskService taskService;

    @Before
    public void setUp() {
        super.setup();
        createdUserId = Integer.valueOf(0);
        createdTaskId = Integer.valueOf(0);
    }

    @After
    public void tearDown() {
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
        fixture.givenURLIs("/api/user/" + createdPojo.getId());
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

        List<UserPojo> userList = (List<UserPojo>) fixture.getBody(new TypeReference<List<UserPojo>>() {
        });
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

        fixture.givenURLIs("/api/user/" + createdUserId);
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.GET);

        UserPojo retrievedUserPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });
        fixture.evaluate(assertThat(retrievedUserPojo.getUsername()).isEqualTo(userPojo.getUsername()));
    }

    /*
        Test all the task related API's
     */
    @Test
    public void testCreateTask_alsoCreateNewUser() {

        /*
            Create new user to user for reference.
         */
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        /*
            Now create new Task
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        createdTaskId = createdTaskPojo.getId();

        fixture.evaluate(assertThat(createdTaskPojo.getId()).isGreaterThan(0));
        fixture.evaluate(assertThat(createdTaskPojo.getName()).isEqualTo(newTaskPojo.getName()));
    }

    /**
     * Test creation of a new task. The user id is invalid, so a UserNotFoundException should be thrown.
     */
    @Test
    public void testCreateTask_noUserExistsSoExpectException() {
        /*
            Now create new Task
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/99999999999999/task");

        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));

        fixture.thenHttpResponseCodeMustBe(HttpStatus.BAD_REQUEST);
    }

    /**
     * Create a new user, then create a new task.
     * Then do an update of the task.
     */
    @Test
    public void testUpdateOfAnExistingTask() {

        /*
            Create new user to user for reference.
         */
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        /*
            Now create new Task
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        createdTaskId = createdTaskPojo.getId();

        /*
            Now update the task.
         */
        createdTaskPojo.setDescription("UpdatedDescription");
        createdTaskPojo.setName("UpdatedName");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task/" + createdTaskId);
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.PUT,
                new HttpEntity<>(createdTaskPojo, headers));

        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo updatedTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        fixture.evaluate(assertThat(updatedTaskPojo.getName()).isEqualTo(createdTaskPojo.getName()));
        fixture.evaluate(assertThat(updatedTaskPojo.getDescription()).isEqualTo(createdTaskPojo.getDescription()));
    }

    /**
     * Create a user, then create a task and then delete the task.
     */
    @Test
    public void testDeleteTask() {

        /*
            Create new user to user for reference.
         */
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        /*
            Now create new Task
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        createdTaskId = createdTaskPojo.getId();

        /*
            Now delete the task.
         */
        fixture.givenURLIs("/api/user/" + createdUserId + "/task/" + createdTaskId);
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.DELETE,
                new HttpEntity<>(Void.class, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.ACCEPTED);
        String respMessage = (String) fixture.getBody(new TypeReference<String>() {
        });
        fixture.evaluate(assertThat(respMessage).isEqualTo("Task deleted."));
    }

    /**
     * Test get information for a specific task.
     */
    @Test
    public void testGetTaskInformation() {

        /*
            Create new user to user for reference.
         */
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        /*
            Now create new Task
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        createdTaskId = createdTaskPojo.getId();

        /*
            Now retrieve the task.
         */
        fixture.givenURLIs("/api/user/" + createdUserId + "/task/" + createdTaskId);
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.GET);

        TaskPojo retrievedTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });
        fixture.evaluate(assertThat(retrievedTaskPojo.getName()).isEqualTo(newTaskPojo.getName()));
        fixture.evaluate(assertThat(retrievedTaskPojo.getDescription()).isEqualTo(newTaskPojo.getDescription()));
    }

    /**
     * Get all the tasks for a specific user.
     */
    @Test
    public void testGetAllTasksForSpecificUser() {

        /*
            Create new user to user for reference.
         */
        UserPojo userPojo = getNewUserPojo();
        fixture.givenURLIs("/api/user");
        fixture.whenExecuteHttpRequest(UserPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(userPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        UserPojo createdPojo = (UserPojo) fixture.getBody(new TypeReference<UserPojo>() {
        });

        createdUserId = createdPojo.getId();

        /*
            Now create two new Tasks
         */
        TaskPojo newTaskPojo = new TaskPojo();

        /*
            Make the due date for the task 5 hours from now.
         */
        Instant instant = Instant.now();
        instant.plus(5, ChronoUnit.HOURS);

        newTaskPojo.setDateTime(Date.from(instant));
        newTaskPojo.setDescription("Please fix the bug now.");
        newTaskPojo.setName("TSK-001");

        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        createdTaskId = createdTaskPojo.getId();

        newTaskPojo.setName("TSK-002");
        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(TaskPojo.class,
                HttpMethod.POST,
                new HttpEntity<>(newTaskPojo, headers));
        fixture.thenHttpResponseCodeMustBe(HttpStatus.CREATED);
        TaskPojo createdTaskPojo2 = (TaskPojo) fixture.getBody(new TypeReference<TaskPojo>() {
        });

        /*
            Now retrieve all the tasks for a user.
         */
        fixture.givenURLIs("/api/user/" + createdUserId + "/task");
        fixture.whenExecuteHttpRequest(ArrayList.class,
                HttpMethod.GET);
        fixture.thenHttpResponseCodeMustBe(HttpStatus.OK);
        List<TaskPojo> taskPojoList = (List<TaskPojo>) fixture.getBody(new TypeReference<List<TaskPojo>>() {
        });

        fixture.evaluate(assertThat(taskPojoList.size()).isGreaterThanOrEqualTo(2));
    }

    private UserPojo getNewUserPojo() {
        UserPojo userPojo = new UserPojo();
        userPojo.setFirstName("FirstName2");
        userPojo.setLastName("LastName2");
        userPojo.setUsername("username2");
        return userPojo;
    }
}