package com.example.backend.controller;


import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.TaskNotBelongToUser;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.account.AccountByTaskIdDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskAddDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import com.example.backend.payload.dto.mapper.TaskMapper;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskUpdateDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskUserAssignDTO;
import com.example.backend.payload.dto.user.PagingDTO;
import com.example.backend.payload.response.MessageResponse;
import com.example.backend.repository.TaskRepository;
import com.example.backend.security.config.AppConstants;
import com.example.backend.security.service.tasks.primaryTasks.TaskPrintService;
import com.example.backend.security.service.tasks.primaryTasks.TaskService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private TaskPrintService taskPrintService;

    @GetMapping("/generate-pdf/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        try {
            // Assuming a method to fetch SuppliesDTO by id
            Task dto = taskRepository.getReferenceById(id);
            byte[] pdfBytes = taskPrintService.generatePdfWithQrCode(dto);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=supplies_" + id + ".pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksNoPaging(
            @RequestParam(value = "userId", defaultValue = AppConstants.DEFAULT_USER_ID, required = false) long id) {
        try {
            List<Task> tasks;

            if (id != 0) {
                tasks = taskService.getTaskAndSub(id);
            } else {
                tasks = taskService.getAllTasks();
            }

            List<TaskDTO> listDto = tasks.stream()
                    .map(TaskMapper::convertEntityToDTO)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(listDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tasksForMods")
//    public List<Task> getAllTasks(){ return taskRepository.findAll();}
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        try {
            //List<Task> list = new ArrayList<>();
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<Task> taskPage;

            taskPage = taskService.getTasksForMod(pageable);

            List<Task> list = taskPage.getContent();

            List<TaskDTO> listDto = list.stream()
                    .map(TaskMapper::convertEntityToDTO)
                    .collect(Collectors.toList());

            Map<String, Object> respone = new HashMap<>();
            respone.put("list", listDto);                                                
            respone.put("currentPage", taskPage.getNumber());
            respone.put("allTasks", taskPage.getTotalElements());
            respone.put("allPages", taskPage.getTotalPages());
            return new ResponseEntity<>(respone, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("exception", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/tasks/updateOrder")
    public ResponseEntity<?> updateTaskOrder(@RequestBody List<TaskDTO> updatedTask) {
        try {
//            logger.info("Received task update request: " + updatedTask);
//            for (TaskDTO task : updatedTask) {
//                logger.info("Update detail - Task ID: {}, Position: {}", task.getId(), task.getPosition());
//            }
            taskService.updateTaskPosition(updatedTask);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tasks/addTask")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskAddDTO task) {
        try {
            TaskAddDTO addedTask = taskService.addTask(task);
            return ResponseEntity.ok().body(addedTask);
        } catch (Exception e) {
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
        try {
            Task existingTask = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not exist with id: " + id));

            taskService.updateTask(existingTask, dto.getTitle());
            return ResponseEntity.ok().body(existingTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Something goes wrong"));
        }
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        try {
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
        try {
            taskService.deleteTaskByIdAndUserId(id, userId);
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        } catch (TaskNotBelongToUser e) {
            return new ResponseEntity<>("Task does not belong to the user", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/all/{id}")
    public ResponseEntity<?> deleteAllTasks(@PathVariable Long id) {
        try {
            taskService.deleteAllTasksByUserId(id);
            return new ResponseEntity<>("Delete all successfully", HttpStatus.OK);
        } catch (Exception e) {
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

    @PostMapping("/assignTaskToUser")
    public ResponseEntity<?> assignTaskToUser(@RequestBody TaskUserAssignDTO dto) {
        try {
            if (dto.getUserId() == 0) {
                return new ResponseEntity<>("Must pick user", HttpStatus.BAD_REQUEST);
            } else {
                taskService.assignTaskToUser(dto);
            }
            return new ResponseEntity<>("Assign success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/withdraw/{taskId}")
    public ResponseEntity<String> withdrawTaskFromUser(@PathVariable Long taskId) {
        try {
            taskService.withdrawTaskFromUser(taskId);
            return new ResponseEntity<>("Task withdrawn successfully from the user", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAccountByTaskId/{taskId}")
    public ResponseEntity<AccountByTaskIdDTO> getAccountByTaskId(@PathVariable Long taskId) {
        try {
            AccountByTaskIdDTO accountByTaskIdDTO = taskService.getAccountByTaskId(taskId);
            return ResponseEntity.ok(accountByTaskIdDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
