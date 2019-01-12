package com.ajk.taskman.batch;

import com.ajk.taskman.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component class that will run at a fixed interval to update tasks that has a date_time older than now.
 * Using a fixed interval defined in the application.yml file.
 */
@Slf4j
@Component
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
