package com.ajk.taskman.web.controller;

import com.ajk.taskman.pojo.TaskPojo;
import com.ajk.taskman.pojo.UserPojo;
import com.ajk.taskman.service.TaskService;
import com.ajk.taskman.service.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.Audited;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class that will handle all the API endpoints.
 * ControllerAdvise class TaskManControllerAdvise will handle exceptions thrown from within the application. No error handling to be done here.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TaskManController {

    private UserService userService;
    private TaskService taskService;

    public TaskManController(UserService userService,
                             TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    /*
        Handle user related actions.
     */
    @Timed
    @Audited
    @PostMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserPojo> createUser(@Valid @RequestBody UserPojo userPojo) {
        UserPojo createdUser = userService.createUser(userPojo);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @PutMapping(value = "/user/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserPojo> updateUser(@Valid @PathVariable Integer userId, @Valid @RequestBody UserPojo userPojo) {
        UserPojo updatedUser = userService.updateUser(userId, userPojo);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserPojo>> getAllUsers() {
        List<UserPojo> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Timed
    @Audited
    @GetMapping(value = "/user/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserPojo> getUser(@Valid @PathVariable Integer id) {
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
    }

    /*
        Handle task related actions
     */
    @Timed
    @Audited
    @PostMapping(value = "/user/{userId}/task", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TaskPojo> createTask(@PathVariable Integer userId, @RequestBody TaskPojo taskPojo) throws Exception {

        return new ResponseEntity<>(taskService.creatTask(userId, taskPojo), HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @PutMapping(value = "/user/{userId}/task/{taskId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TaskPojo> updateTask(@Valid @PathVariable Integer userId, @Valid @PathVariable Integer taskId, @Valid @RequestBody TaskPojo taskPojo) {
        return new ResponseEntity(taskService.updateTask(userId, taskId, taskPojo), HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @DeleteMapping(value = "/user/{userId}/task/{taskId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteTask(@Valid @PathVariable Integer userId, @Valid @PathVariable Integer taskId) {
        taskService.deleteTaskForUserAndTaskIds(userId, taskId);
        return new ResponseEntity("Task deleted.", HttpStatus.ACCEPTED);
    }

    @Timed
    @Audited
    @GetMapping(value = "/user/{userId}/task/{taskId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TaskPojo> getTaskInformation(@Valid @PathVariable Integer userId, @Valid @PathVariable Integer taskId) {
        return new ResponseEntity(taskService.getTask(userId, taskId), HttpStatus.OK);
    }

    @Timed
    @Audited
    @GetMapping(value = "/user/{userId}/task", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TaskPojo>> getAllTasksForUser(@Valid @PathVariable Integer userId) {
        return new ResponseEntity(taskService.findAllTaskForUser(userId), HttpStatus.OK);
    }
}
