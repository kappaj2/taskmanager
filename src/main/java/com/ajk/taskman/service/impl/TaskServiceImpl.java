package com.ajk.taskman.service.impl;

import com.ajk.taskman.enums.TaskStatusEnum;
import com.ajk.taskman.exception.TaskNotFoundException;
import com.ajk.taskman.exception.UserNotFoundException;
import com.ajk.taskman.model.Task;
import com.ajk.taskman.model.TaskStatus;
import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.TaskPojo;
import com.ajk.taskman.repository.TaskStatusRepository;
import com.ajk.taskman.repository.TasksRepository;
import com.ajk.taskman.repository.UsersRepository;
import com.ajk.taskman.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private static TaskStatus doneTaskStatus = null;

    private TasksRepository tasksRepository;
    private UsersRepository usersRepository;
    private TaskStatusRepository taskStatusRepository;

    public TaskServiceImpl(TasksRepository tasksRepository,
                           UsersRepository usersRepository,
                           TaskStatusRepository taskStatusRepository) {
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    @Transactional
    public TaskPojo creatTask(Integer userId, TaskPojo taskPojo) {
        return null;
    }

    @Transactional
    public TaskPojo updateTask(Integer userId, TaskPojo taskPojo) {
        return null;
    }

    public TaskPojo getTask(Integer userId, Integer taskId) {
        return null;
    }

    public List<TaskPojo> findAllTaskForUser(Integer userId) {
        return null;
    }

    /**
     * Delete all tasks for a user.
     */
    @Transactional
    public void deleteTaskForUser(Integer userId) {
        User user = usersRepository.getOne(userId);
        if (user == null) {
            throw new UserNotFoundException("Cannot find user for id : " + userId);
        }
        List<Task> taskList = tasksRepository.findAllByUser(user);
        taskList.stream().forEach(task -> {
            tasksRepository.delete(task);
        });
    }

    /**
     * Delete a task linked to a user and task id as passed in as parameters.
     */
    @Transactional
    public void deleteTaskForUserAndTaskIds(Integer userId, Integer taskId) {
        Task userTask = tasksRepository.findByUserAndTaskId(userId, taskId);
        if (userTask == null) {
            throw new TaskNotFoundException("Task not found for userid : " + userId + " and taskId : " + taskId);
        }
        tasksRepository.delete(userTask);
    }

    /**
     * Find all the tasks that are past due date and status pending.
     */
    @Override
    @Transactional
    public void findAndUpdateExpiredTasks() {
        List<Task> pendingTaskList = tasksRepository.findExpiredTasks(TaskStatusEnum.PENDING.getStatus().toLowerCase());
        log.info("Size for pending task is : " + pendingTaskList.size());

        if (doneTaskStatus == null) {
            doneTaskStatus = taskStatusRepository.getOne(TaskStatusEnum.DONE.getStatus().toLowerCase());
        }
        pendingTaskList.stream().forEach(task -> {
            System.out.println("Updating pending task with id : " + task.getId() + " to status done. Passed date_time check and status is still pending.");
            log.info("Pending task : " + task.getTask());

            task.setTaskStatusBean(doneTaskStatus);
            task.setModifiedAt(new Date());
            tasksRepository.save(task);
            tasksRepository.flush();
        });
    }
}
