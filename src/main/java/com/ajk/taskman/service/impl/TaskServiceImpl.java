package com.ajk.taskman.service.impl;

import com.ajk.taskman.enums.TaskStatusEnum;
import com.ajk.taskman.exception.TaskAlreadyExistsException;
import com.ajk.taskman.exception.TaskNotFoundException;
import com.ajk.taskman.exception.TaskStatusNotFoundException;
import com.ajk.taskman.exception.UserNotFoundException;
import com.ajk.taskman.mapper.TaskMapper;
import com.ajk.taskman.model.Task;
import com.ajk.taskman.model.TaskStatus;
import com.ajk.taskman.model.User;
import com.ajk.taskman.pojo.TaskPojo;
import com.ajk.taskman.repository.TaskStatusRepository;
import com.ajk.taskman.repository.TasksRepository;
import com.ajk.taskman.repository.UsersRepository;
import com.ajk.taskman.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private static TaskStatus doneTaskStatus = null;

    private TasksRepository tasksRepository;
    private UsersRepository usersRepository;
    private TaskStatusRepository taskStatusRepository;

    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    public TaskServiceImpl(TasksRepository tasksRepository,
                           UsersRepository usersRepository,
                           TaskStatusRepository taskStatusRepository) {
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    /**
     * Create a new task for the user. The new task status will be pending.
     *
     * @param userId   for the user to save the task against.
     * @param taskPojo task information to save for user.
     * @return TaskPojo
     */
    @Transactional
    public TaskPojo creatTask(Integer userId, TaskPojo taskPojo) {

        Task t = tasksRepository.findByTask(taskPojo.getName());

        if (t != null) {
            throw new TaskAlreadyExistsException(("Task already exists with name : " + taskPojo.getName()));
        }

        User user = findUser(userId);

        /*
            Now tasks are assigned status pending.
         */
        TaskStatus taskStatus = getTaskStatus("pending");

        Task newTask = new Task();
        newTask.setTask(taskPojo.getName());
        newTask.setDateTime(taskPojo.getDateTime());
        newTask.setDescription(taskPojo.getDescription());
        newTask.setTaskStatusBean(taskStatus);
        newTask.setUser(user);

        Task createdTask = tasksRepository.save(newTask);
        tasksRepository.flush();
        return taskMapper.asPojo(createdTask);
    }

    /**
     * Retrieve a task using the userId and taskId fields. Update the name, description and modifiedAt fields.
     *
     * @param userId
     * @param taskId
     * @param taskPojo
     * @return
     */
    @Transactional
    public TaskPojo updateTask(Integer userId, Integer taskId, TaskPojo taskPojo) {

        Task task = tasksRepository.findByUserAndTaskId(userId, taskId);

        if (task == null) {
            throw new TaskNotFoundException(("Cannot find task for userId : " + userId + " and taskId : " + taskId));
        }
        if (taskPojo.getName() != null) {
            task.setTask(taskPojo.getName());
        }
        if (taskPojo.getDescription() != null) {
            task.setDescription(taskPojo.getDescription());
        }

        Task updateTask = tasksRepository.save(task);
        tasksRepository.flush();
        return taskMapper.asPojo(updateTask);
    }

    /**
     * Retrieve a task using the userId and taskId fields.
     *
     * @param userId to search for
     * @param taskId to search for
     * @return the Task found.
     */
    public TaskPojo getTask(Integer userId, Integer taskId) {
        Task task = tasksRepository.findByUserAndTaskId(userId, taskId);
        if (task == null) {
            throw new TaskNotFoundException(("Cannot find task for userId : " + userId + " and taskId : " + taskId));
        }
        return taskMapper.asPojo(task);
    }

    /**
     * Retrieve all the tasks for a user.
     *
     * @param userId to search for.
     * @return List of TaskPojo records.
     */
    public List<TaskPojo> findAllTaskForUser(Integer userId) {

        User user = findUser(userId);

        List<Task> taskList = tasksRepository.findAllByUser(user);
        List<TaskPojo> taskPojoList = new ArrayList<>();
        taskList.stream().forEach(task -> {
            taskPojoList.add(taskMapper.asPojo(task));
        });
        return taskPojoList;
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
            tasksRepository.save(task);
            tasksRepository.flush();
        });
    }

    /**
     * Utiltiy method to find a user by Id.
     *
     * @param userId to search for.
     * @return User
     */
    private User findUser(Integer userId) {
        User user = usersRepository.getOne(userId);
        if (user == null) {
            throw new UserNotFoundException("Cannot find user for id : " + userId);
        }
        return user;
    }

    /**
     * Utility method to find a task status by String.
     *
     * @param statusCode to search for.
     * @return TaskStatus
     */
    private TaskStatus getTaskStatus(String statusCode) {
        TaskStatus taskStatus = taskStatusRepository.getOne(statusCode);
        if (taskStatus == null) {
            throw new TaskStatusNotFoundException("Cannot find task status for code : " + statusCode);
        }
        return taskStatus;
    }
}
