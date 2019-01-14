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


    }
}
