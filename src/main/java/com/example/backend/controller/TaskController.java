package com.example.backend.controller;


import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.TaskNotBelongToUser;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.task.TaskAddDTO;
import com.example.backend.payload.dto.task.TaskDTO;
import com.example.backend.payload.dto.mapper.TaskMapper;
import com.example.backend.payload.dto.task.TaskUpdateDTO;
import com.example.backend.payload.response.MessageResponse;
import com.example.backend.repository.TaskRepository;
import com.example.backend.security.config.AppConstants;
import com.example.backend.security.service.tasks.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 36000)
@RestController
@RequestMapping("/api/v1/")
//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
//    public List<Task> getAllTasks(){ return taskRepository.findAll();}
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "userId", defaultValue = AppConstants.DEFAULT_USER_ID, required = false) long id,
            @RequestParam(value = "filter", defaultValue = "all") String filter) {
        try {
            List<Task> list = new ArrayList<>();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Task> taskPage;
            if (id != 0) {
                if ("completed".equals(filter)) {
                    taskPage = taskService.getTasksByStatusAndId(true, id, pageable);
                } else if ("incomplete".equals(filter)) {
                    taskPage = taskService.getTasksByStatusAndId(false, id, pageable);
                } else {
                    taskPage = taskService.getTaskById(id, pageable);
                }
            } else {
                taskPage = taskService.getAllTasks(pageable);
            }

            list = taskPage.getContent();
            
            List<TaskDTO> listDto = list.stream().
                    map(TaskMapper::convertEntityToDTO).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("list", listDto);
            response.put("currentPage", taskPage.getNumber());
            response.put("allTasks", taskPage.getTotalElements());
            response.put("allPages", taskPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("exception", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tasks/addTask")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskAddDTO task) {
        try{
            taskService.addTask(task);
            return new ResponseEntity<>("Add task successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id : " + id));
        return ResponseEntity.ok(task);
    }

    @PutMapping("/tasks/updateTask/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO dto) {
        try{
            Task existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

            taskService.updateTask(existingTask, dto.getTitle());
            return ResponseEntity.ok().body(existingTask);
        } catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Something goes wrong"));
        }
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        try{
            Task existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));
            taskService.changeStatusTask(existingTask);
            return new ResponseEntity<>("Change status successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleTaskById(@PathVariable Long id, @RequestParam Long userId) {
        try{
            taskService.deleteTaskByIdAndUserId(id, userId);
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        } catch (TaskNotBelongToUser e){
            return new ResponseEntity<>("Task does not belong to the user", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/all/{id}")
    public ResponseEntity<?> deleteAllTasks(@PathVariable Long id) {
        try{
            taskService.deleteAllTasksByUserId(id);
            return new ResponseEntity<>("Delete all successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/completed/{id}")
    public ResponseEntity<String> deleteCompletedTasks(@PathVariable Long id) {
        try {
            taskService.deleteDoneTaskByUserId(id);
            return ResponseEntity.ok("Deleted completed tasks");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting done tasks" + e.getMessage());
        }
    }
}
