package com.example.backend.security.service.tasks.primaryTasks;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.TaskNotBelongToUser;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskAddDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import com.example.backend.repository.SubTaskRepository;
import com.example.backend.repository.TaskRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.jwt.JwtUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImp implements TaskService{

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImp.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional
    public TaskAddDTO addTask(TaskAddDTO dto) {
        Task task = new Task();
        task.setUser(userRepository.getReferenceById(getIdFromToken()));
        task.setTitle(dto.getTitle());
        
        // Find the maximum position for the user's tasks
        int minPosition = taskRepository.getMinPositionForUser(getIdFromToken());

        // Set the position for the new task
        task.setPosition(minPosition);

        Task savedTask = taskRepository.save(task);

        // Increment the position of existing tasks
        taskRepository.incrementPositionsForUser(getIdFromToken(), task.getPosition(), task.getId());
            
        return new TaskAddDTO(savedTask.getId(), savedTask.getTitle(), savedTask.getPosition(), savedTask.getUser().getId());
    }

    @Override
    public Task updateTask(Task task, String title) {
        task.setTitle(title);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteDoneTaskByUserId(Long userId) {
        List<Task> doneTasks = taskRepository.getDoneTaskByUserId(userId);

        if(!doneTasks.isEmpty()){
            //dele subTask
            doneTasks.forEach(task -> subTaskRepository.deleteAll(task.getSubTasks()));

            //get position of the tasks
            List<Integer> positionToDele = doneTasks.stream()
                    .map(Task::getPosition)
                    .distinct()
                    .collect(Collectors.toList());

            //dele
            taskRepository.deleteAll(doneTasks);

            //update positions
            for (Integer position : positionToDele) {
                taskRepository.decrementPositionsForUser(userId, position);
            }
        }
    }

    @Override
    @Transactional
    public void deleteAllTasksByUserId(Long userId) {
        List<Task> taskToDele = taskRepository.findTaskByUserId(userId);

        if(!taskToDele.isEmpty()){
            //dele subtask
            taskToDele.forEach(task -> subTaskRepository.deleteAll(task.getSubTasks()));

            taskRepository.deleteAll(taskToDele);
        }
    }

    @Override
    @Transactional
    public void deleteTaskByIdAndUserId(Long id, Long userId) {
        Optional<Task> task = taskRepository.findById(id);

        if(task.isPresent()){
            Task taskToDele = task.get();
            if(taskToDele.getUser().getId().equals(userId)){
                int positionToDele = taskToDele.getPosition();

                //dele subtask
                subTaskRepository.deleteAll(taskToDele.getSubTasks());

                //dele
                taskRepository.delete(taskToDele);

                //update position
                taskRepository.decrementPositionsForUser(userId, positionToDele);
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
    @Transactional
    public void updateTaskPosition(List<TaskDTO> updatedTasks) {
        for (int i = 0; i < updatedTasks.size(); i++) {
            TaskDTO updatedTask = updatedTasks.get(i);
            //logger.info("Received task update: " + updatedTask.getId() + ", " + updatedTask.getPosition());
            taskRepository.updateTaskPosition(updatedTask.getId(), updatedTask.getPosition());

            // Flush the Hibernate session to ensure changes are committed
            entityManager.flush();
        }
    }                                                               

    @Override
    public List<Task> getTaskById(Long id) {
        return taskRepository.getListByUserId(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.getAllTask();
    }

    @Override
    public List<Task> getTaskAndSub(Long id) {
        return taskRepository.getAllWithSubTaskByUserId(id);
    }

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

