package com.ajk.taskman.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "description")
    private String description;

    @Column(name = "task")
    private String task;

    //bi-directional many-to-one association to TaskStatus
    @ManyToOne
    @JoinColumn(name = "task_status")
    private TaskStatus taskStatusBean;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    /*
        Auditing columns
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Instant modifiedDate;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", task='" + task + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
