package com.ajk.taskman.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    private String username;

    //bi-directional many-to-one association to Task
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Task> tasks;
}
