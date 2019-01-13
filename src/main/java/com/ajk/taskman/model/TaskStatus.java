package com.ajk.taskman.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "task_status")
@Data
@NoArgsConstructor
public class TaskStatus implements Serializable {

    @Id
    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    //bi-directional many-to-one association to Task
    @NotAudited
    @OneToMany(mappedBy = "taskStatusBean", fetch = FetchType.LAZY)
    private List<Task> tasks;

}
