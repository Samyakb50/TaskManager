package com.example.taskmgr.controllers;

import com.example.taskmgr.dto.CreateTaskDTO;
import com.example.taskmgr.dto.ErrorResponseDTO;
import com.example.taskmgr.dto.UpdateTaskDTO;
import com.example.taskmgr.entities.TaskEntity;
import com.example.taskmgr.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Show all existing tasks
     * GET /tasks
     *
     * @return List of tasks
     */
    @GetMapping("")
    public ResponseEntity<List<TaskEntity>> getTasks(){
        var tasks = taskService.getTasks();

        return ResponseEntity.ok(tasks);
    }

    /**
     * Create a new task
     * POST /tasks
     * Body:
     * <pre>
     *      {
     *          "title": "Task 4",
     *          "description": "Description 4",
     *          "dueDate": "2021-01-01"
     *      }
     *  </pre>
     *
     * @param body CreateTaskDTO object sent by client
     * @return Task object created
     */
    @PostMapping("")
    public ResponseEntity<TaskEntity> addTask(@RequestBody CreateTaskDTO body) throws ParseException {
        var newTask = taskService.addTask(body.getTitle(), body.getDescription(), body.getDeadline());

        return ResponseEntity.created(URI.create("/tasks/" + newTask.getId())).body(newTask);
    }

    /**
     * Get a task by id
     *
     * @param id
     * @return Task object
     */
    @GetMapping("/{id}")
    ResponseEntity<TaskEntity> getTask(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Delete a task by given id
     *
     * @param id Task id to delete
     * @return the deleted task
     */
    @DeleteMapping("/{id}")
    ResponseEntity<TaskEntity> deleteTask(@PathVariable("id") Integer id) {
        return ResponseEntity.accepted().body(taskService.deleteTask(id));
    }

    /**
     * Update a task by given id
     *
     * @param id   Task id to update
     * @param body UpdateTaskDTO object sent by client
     * @return the updated task
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") Integer id, @RequestBody UpdateTaskDTO body) throws ParseException{
        var updatedTask = taskService.updateTask(
                id,
                body.getDescription(),
                body.getDeadline(),
                body.getCompleted());

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();   // good
        }

        return ResponseEntity.accepted().body(updatedTask);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception e){
        if (e instanceof ParseException){
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid Date format"));
        }
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Internal Server Error"));
    }
}
