package com.ajk.taskman.service;

import com.ajk.taskman.model.Task;
import com.ajk.taskman.pojo.TaskPojo;

import java.util.List;

public interface TaskService {

    TaskPojo creatTask(Integer userId, TaskPojo taskPojo);

    TaskPojo updateTask(Integer userId, Integer taskId, TaskPojo taskPojo);

    TaskPojo getTask(Integer userId, Integer taskId);

    List<TaskPojo> findAllTaskForUser(Integer userId);

    void deleteTaskForUser(Integer userId);

    void deleteTaskForUserAndTaskIds(Integer userId, Integer taskId);

    void findAndUpdateExpiredTasks();

}
