package com.example.TodoListBackend.RequestDtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    private String description;
    private LocalDateTime deadLine;


    //getters and setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }
}
