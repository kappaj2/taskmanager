package com.ajk.taskman.enums;

public enum TaskStatusEnum {

    PENDING("pending", "Task is pending processing"),
    DONE("done", "Task be completed."),
    INPROGRESS("in-progress", "Task is in progress");

    private String status;
    private String description;

    private TaskStatusEnum(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
