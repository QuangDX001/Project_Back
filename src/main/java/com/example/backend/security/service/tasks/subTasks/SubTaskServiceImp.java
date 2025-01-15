package com.example.backend.security.service.tasks.subTasks;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.SubTask;
import com.example.backend.model.Task;
import com.example.backend.payload.dto.tasks.subTasks.ChangeStatusDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskAddDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskDTO;
import com.example.backend.payload.dto.tasks.primaryTasks.TaskDTO;
import com.example.backend.payload.dto.tasks.subTasks.SubTaskUpdateDTO;
import com.example.backend.repository.SubTaskRepository;
import com.example.backend.repository.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Admin on 11/29/2023
 */
@Service
public class SubTaskServiceImp implements SubTaskService {
    private static final Logger logger = LoggerFactory.getLogger(SubTaskServiceImp.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public SubTaskAddDTO addTask(SubTaskAddDTO dto) {
        SubTask subTask = new SubTask();
        subTask.setTitle(dto.getTitle());

        // Set the position for the new task
        subTask.setPosition(1);

        // Set the relationship with the primary task
        Task primaryTask = taskRepository.getReferenceById(dto.getTaskId());

        subTask.setTask(primaryTask);

        SubTask savedTask = subTaskRepository.save(subTask);

        // Increment the positions of existing subtasks
        subTaskRepository.incrementPositions(primaryTask.getId(), savedTask.getPosition(), savedTask.getId());

        return new SubTaskAddDTO(savedTask.getId(), savedTask.getTitle(), savedTask.getPosition(), savedTask.getTask().getId());
    }

    @Override
    public SubTask updateTask(SubTask subTask, String title) {
        subTask.setTitle(title);
        return subTaskRepository.save(subTask);
    }

    @Override
    @Transactional
    public Long deleteTaskById(Long id) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found with id: " + id));

        Long primaryTaskId = subTask.getTask().getId();  // Retrieve the primary task ID before deletion

        //update position
        subTaskRepository.decrementPositions(subTask.getTask().getId(), subTask.getPosition());

        //dele
        subTaskRepository.delete(subTask);

        return primaryTaskId;  // Return the primary task ID
    }

    @Override
    public ChangeStatusDTO changeStatusTask(SubTask task) {
        if (task.isDone()) {
            task.setDone(false);
        } else {
            task.setDone(true);
        }
        SubTask updatedTask = subTaskRepository.save(task);

        Long primaryTaskId = updatedTask.getTask().getId();
        return new ChangeStatusDTO("Change status successfully", primaryTaskId);
    }

    @Override
    @Transactional
    public void updateTaskPosition(List<TaskDTO> task) {
        for (TaskDTO taskDTO : task) {
            for (int i = 0; i < taskDTO.getSubTasks().size(); i++) {
                SubTaskDTO subTaskDTO = taskDTO.getSubTasks().get(i);
                logger.info("Received task update: " + subTaskDTO.getId() + ", " + subTaskDTO.getPosition());
                subTaskRepository.updateTaskPosition(subTaskDTO.getId(), subTaskDTO.getPosition());

                entityManager.flush();
            }
        }
    }
}
