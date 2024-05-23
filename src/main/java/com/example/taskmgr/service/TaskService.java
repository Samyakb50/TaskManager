package com.example.taskmgr.service;

import com.example.taskmgr.entities.TaskEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskService {

    private ArrayList<TaskEntity> taskList;
    private AtomicInteger taskId = new AtomicInteger(0);

    public static class TaskNotFoundException extends IllegalArgumentException {
        public TaskNotFoundException(Integer id) {
            super("Task with id " + id + " not found");
        }
    }

    public TaskService() {
        taskList = new ArrayList<>();
        taskList.add(new TaskEntity(taskId.incrementAndGet(), "Task 1", "Description 1", "2021-01-01"));
        taskList.add(new TaskEntity(taskId.incrementAndGet(), "Task 2", "Description 2", "2021-01-01"));
        taskList.add(new TaskEntity(taskId.incrementAndGet(), "Task 3", "Description 3", "2021-01-01"));
    }
    private final SimpleDateFormat deadlineFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public TaskEntity addTask(String title, String description , String deadline) throws ParseException {
        var task = new TaskEntity(taskId.incrementAndGet(), title, description, deadline);
        taskList.add(task);
        return task;
    }

    public ArrayList<TaskEntity> getTasks() {
        return taskList;
    }

    public TaskEntity getTaskById(int id) {
        for (TaskEntity task : taskList) {
            if (task.getId() == id) {
                return task;
            }
        }
        throw new TaskNotFoundException(id);
    }

    public TaskEntity updateTask(int id, String description, String deadline, Boolean completed) throws ParseException {
        TaskEntity task = getTaskById(id);
        if (task == null) {
            return null;
        }
        if (description != null) {
            task.setDescription(description);
        }
        if (deadline != null) {
            task.setDeadline(deadline);
        }
        if (completed != null) {
            task.setCompleted(completed);
        }
        return task;
    }

    public TaskEntity deleteTask(Integer id) {
        var task = getTaskById(id);
        taskList.remove(task);
        return task;
    }

}
