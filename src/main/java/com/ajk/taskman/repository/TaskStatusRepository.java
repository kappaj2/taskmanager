package com.ajk.taskman.repository;

import com.ajk.taskman.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, String> {
}
