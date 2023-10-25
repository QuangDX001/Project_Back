package com.example.backend.controller;


import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import com.example.backend.security.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/v1/")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    @PostMapping("/tasks/addTask")
    public Task addTask(@RequestBody Task task){
        return taskRepository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id : " + id));
        return ResponseEntity.ok(task);
    }

    @PutMapping("/tasks/updateTask/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

        if (updates.containsKey("title")) {
            String title = (String) updates.get("title");
            existingTask.setTitle(title);
        }

        Task editTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(editTask);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> changeStatus(@PathVariable Long id){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

        if (existingTask.isDone()) {
            existingTask.setDone(false);
        } else {
            existingTask.setDone(true);
        }

        Task editTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(editTask);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Map<String, Boolean>> deleTaskById(@PathVariable Long id){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

        taskRepository.delete(existingTask);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllTasks() {
        taskRepository.deleteAll();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/completed")
    public List<Task> getCompletedTasks() {
        return taskService.getTasksByStatus(true);
    }

    @GetMapping("/incomplete")
    public List<Task> getIncompleteTasks() {
        return taskService.getTasksByStatus(false);
    }

    @DeleteMapping("/completed")
    public ResponseEntity<String> deleteCompletedTasks() {
        taskService.deleteTasksByStatus(true);
        return ResponseEntity.ok("Deleted completed tasks.");
    }

    @DeleteMapping("/incomplete")
    public ResponseEntity<String> deleteIncompleteTasks() {
        taskService.deleteTasksByStatus(false);
        return ResponseEntity.ok("Deleted incomplete tasks.");
    }
}
