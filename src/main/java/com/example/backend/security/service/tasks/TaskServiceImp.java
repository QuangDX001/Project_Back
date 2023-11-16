package com.example.backend.security.service.tasks;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.TaskNotBelongToUser;
import com.example.backend.model.Role;
import com.example.backend.model.Task;
import com.example.backend.model.User;
import com.example.backend.payload.dto.task.TaskAddDTO;
import com.example.backend.payload.dto.task.TaskUpdateDTO;
import com.example.backend.repository.TaskRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskServiceImp implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

//    @Override
//    public Page<Task> getTasksByStatusAndId(boolean status, Long userId, Pageable pageable) {
//        return taskRepository.getListByStatusAndId(userId, pageable,  status);
//    }

    @Override
    public List<Task> getTasksByStatusAndId(boolean status, Long userId) {
        return taskRepository.getListByStatusAndId(userId,  status);
    }

    @Override
    public Task addTask(TaskAddDTO dto) {
        Task task = new Task();
        task.setUser(userRepository.getReferenceById(getIdFromToken()));
        task.setTitle(dto.getTitle());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task, String title) {
        task.setTitle(title);
        return taskRepository.save(task);
    }

    @Override
    public void deleteDoneTaskByUserId(Long userId) {
        taskRepository.deleteDoneTaskByUserId(userId);
    }

    @Override
    public void deleteAllTasksByUserId(Long userId) {
        List<Task> taskToDele = taskRepository.findTaskByUserId(userId);

        if(!taskToDele.isEmpty()){
            taskRepository.deleteAll(taskToDele);
        }
    }

    @Override
    public void deleteTaskByIdAndUserId(Long id, Long userId) {
        Optional<Task> task = taskRepository.findById(id);

        if(task.isPresent()){
            Task taskToDele = task.get();
            if(taskToDele.getUser().getId().equals(userId)){
                taskRepository.delete(taskToDele);
            } else {
                throw new TaskNotBelongToUser("Tasks does not belong to user");
            }
        } else {
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Override
    public void changeStatusTask(Task task) {
        if (task.isDone()) {
            task.setDone(false);
        } else {
            task.setDone(true);
        }
        taskRepository.save(task);
    }

    @Override
    public List<Task> getTaskById(Long id) {
        return taskRepository.getListByUserId(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.getAllTask();
    }

//    @Override
//    public Page<Task> getTaskById(Long id, Pageable pageable) {
//        return taskRepository.getListByUserId(id, pageable);
//    }
//
//    @Override
//    public Page<Task> getAllTasks(Pageable pageable) {
//        return taskRepository.getAllTask(pageable);
//    }

    public long getIdFromToken() {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        }
        long id = jwtUtils.getIdFromJwtToken(jwtToken);
        return id;
    }
}
