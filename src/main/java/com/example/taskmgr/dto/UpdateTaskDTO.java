package com.example.taskmgr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTaskDTO {
    String description;
    String deadline;
    Boolean completed;

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
