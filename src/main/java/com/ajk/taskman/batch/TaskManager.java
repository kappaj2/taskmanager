package com.ajk.taskman.batch;

import com.ajk.taskman.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Component class that will run at a fixed interval to update tasks that has a date_time older than now.
 * Using a fixed interval defined in the application.yml file.
 * The profile annotation will only allow this method to run when using the profile default. This stops it from running during test profile
 * usage for unit and intergration tests.
 */
@Slf4j
@Component
@Profile("default")
public class TaskManager {

    private TaskService taskService;

    public TaskManager(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Find all the task that are past due date and update status to done.
     */
    @Scheduled(fixedRateString = "${spring.batch.taskmanager.fixedrate}")
    public void processPendingTasks() {
        log.info("Processing pending tasks:");
        taskService.findAndUpdateExpiredTasks();
    }
}
