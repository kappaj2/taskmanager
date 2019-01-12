package com.ajk.taskman.config;

import com.ajk.taskman.model.Task;
import com.ajk.taskman.model.TaskStatus;
import com.ajk.taskman.model.User;
import com.ajk.taskman.repository.TaskStatusRepository;
import com.ajk.taskman.repository.TasksRepository;
import com.ajk.taskman.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Utility class than can initialize data, setup caches, etc.
 * Initial setup data is loaded via the FlyWay scripts.
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private UsersRepository usersRepository;
    private TasksRepository tasksRepository;
    private TaskStatusRepository taskStatusRepository;

    public DataInitializer(UsersRepository usersRepository,
                           TasksRepository tasksRepository,
                           TaskStatusRepository taskStatusRepository) {
        this.usersRepository = usersRepository;
        this.tasksRepository = tasksRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        /*
            Clean all the old tasks for the test user and then create a new one.
         */
        String userName = "testuser";
        User testUser = usersRepository.findUsersByUsername(userName);

        log.info("Found test user with id : " + testUser.getId());

        List<Task> userTaskList = tasksRepository.findAllByUser(testUser);
        userTaskList.stream().forEach(task -> {
            tasksRepository.delete(task);
        });
        tasksRepository.flush();

        /*
            Now create test tasks with status pending ...
         */
        TaskStatus pendingStatus = taskStatusRepository.getOne("pending");

        Task pendingTask = new Task();
        pendingTask.setCreatedAt(new Date());
        pendingTask.setModifiedAt(new Date());
        pendingTask.setDateTime(new Date());
        pendingTask.setDescription("Test task number 1");
        pendingTask.setTask("Fix bug in query");
        pendingTask.setTaskStatusBean(pendingStatus);
        pendingTask.setUser(testUser);

        tasksRepository.save(pendingTask);

        log.info("Success doing initial data insert. All seems to work fine on application startup.");
    }
}
