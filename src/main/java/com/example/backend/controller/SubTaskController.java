package com.example.backend.controller;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.SubTask;
import com.example.backend.payload.dto.tasks.subTasks.ChangeStatusDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskAddDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskUpdateDTO;
import com.example.backend.payload.response.MessageResponse;
import com.example.backend.repository.SubTaskRepository;
import com.example.backend.security.service.tasks.subTasks.SubTaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 11/29/2023
 */
@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/v1/")
public class SubTaskController {
    @Autowired
    private SubTaskService subTaskService;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @PutMapping("/subtasks/updateOrder")                                
    public ResponseEntity<?> updateTaskOrder(@RequestBody List<TaskDTO> updatedTask) {
        try {
            subTaskService.updateTaskPosition(updatedTask);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("subtasks/addTask")
    public ResponseEntity<?> addTask(@Valid @RequestBody SubTaskAddDTO task) {
        try {
            SubTaskAddDTO addTask = subTaskService.addTask(task);
            return ResponseEntity.ok().body(addTask);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/subtasks/updateTask/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody SubTaskUpdateDTO dto) {
        try {
            SubTask existingTask = subTaskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

            SubTask updatedSubTask = subTaskService.updateTask(existingTask, dto.getTitle());

            // Build the response with the primaryTask field
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedSubTask.getId());
            response.put("title", updatedSubTask.getTitle());
            response.put("position", updatedSubTask.getPosition());
            response.put("done", updatedSubTask.isDone());
            response.put("primaryTask", updatedSubTask.getTask().getId());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Something goes wrong"));
        }
    }

    @PutMapping("/subtasks/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        try {
            SubTask existingTask = subTaskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));
            ChangeStatusDTO responseDTO = subTaskService.changeStatusTask(existingTask);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/subtasks/{id}")
    public ResponseEntity<?> deleTaskById(@PathVariable Long id) {
        try {
            Long primaryTaskId = subTaskService.deleteTaskById(id);
            return new ResponseEntity<>(primaryTaskId, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }   
    }
}
