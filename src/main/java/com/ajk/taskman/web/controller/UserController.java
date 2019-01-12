package com.ajk.taskman.web.controller;

import com.ajk.taskman.model.Task;
import com.ajk.taskman.pojo.TaskPojo;
import com.ajk.taskman.pojo.UserPojo;
import com.ajk.taskman.service.TaskService;
import com.ajk.taskman.service.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.Audited;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private TaskService taskService;

    public UserController(UserService userService,
                          TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    /*
        Handle user related actions.
     */
    @Timed
    @Audited
    @PostMapping("/user")
    public ResponseEntity<UserPojo> createUser(@RequestBody UserPojo userPojo) {
        UserPojo createdUser = userService.createUser(userPojo);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @PutMapping("/user/{user_id}")
    public ResponseEntity<UserPojo> updateUser(@PathVariable Integer user_id, @RequestBody UserPojo userPojo) {
        UserPojo updatedUser = userService.updateUser(user_id, userPojo);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @Timed
    @Audited
    @GetMapping("/user")
    public ResponseEntity<List<UserPojo>> getAllUsers() {
        List<UserPojo> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @Timed
    @Audited
    @GetMapping("/user/{id}")
    public ResponseEntity<UserPojo> getUser(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
    }

    /*
        Handle task related actions
     */
    @Timed
    @Audited
    @PostMapping("/user/{user_id}/task")
    public ResponseEntity<Task> createTask(@PathVariable Integer user_id, @RequestBody TaskPojo taskPojo) {
        return null;
    }

    @Timed
    @Audited
    @PutMapping("/user/{user_id}/task/{task_id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer user_id, @PathVariable Integer task_Id, @RequestBody TaskPojo taskPojo) {
        return null;
    }

    @Timed
    @Audited
    @DeleteMapping("/user/{user_id}/task/{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer user_id, @PathVariable Integer task_id) {
        return null;
    }

    @Timed
    @Audited
    @GetMapping("/user/{user_id}/task/{task_id}")
    public ResponseEntity<TaskPojo> getTaskInformation(@PathVariable Integer user_id, @PathVariable Integer task_id) {
        return null;
    }

    @Timed
    @Audited
    @GetMapping("/user/{user_id}/task")
    public ResponseEntity<List<TaskPojo>> getAllTasksForUser(@PathVariable Integer user_id) {
        return null;
    }
}
