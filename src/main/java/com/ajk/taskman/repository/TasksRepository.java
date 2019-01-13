package com.ajk.taskman.repository;

import com.ajk.taskman.model.Task;
import com.ajk.taskman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT * FROM tasks WHERE date_time < now() and task_status = ?1", nativeQuery = true)
    List<Task> findExpiredTasks(String transStatus);

    List<Task> findAllByUser(User user);

    @Query(value = "SELECT * FROM tasks WHERE users_id = ?1 and  id = ?2", nativeQuery = true)
    Task findByUserAndTaskId(Integer userId, Integer taskId);

    Task findByTask(String taskName);
}
