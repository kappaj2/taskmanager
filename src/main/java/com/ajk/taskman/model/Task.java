package com.ajk.taskman.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "task")
    private String task;

    //bi-directional many-to-one association to TaskStatus
    @ManyToOne
    @JoinColumn(name = "task_status" )
    private TaskStatus taskStatusBean;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
