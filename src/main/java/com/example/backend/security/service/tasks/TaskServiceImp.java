package com.example.backend.security.service.tasks;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImp implements TaskService{

    @Autowired
    private TaskRepository taskRepository;


    @Override
    public List<Task> getTasksByStatus(boolean status) {
        return taskRepository.findByIsDone(status);
    }

    @Override
    public void deleteTasksByStatus(boolean status) {
        List<Task> tasksToDelete = taskRepository.findByIsDone(status);
        taskRepository.deleteAllInBatch(tasksToDelete);
    }



//    public void changeEnableAccount(Task task) {
//        if (task.isDone()) {
//            task.setDone(false);
//        } else {
//            task.setDone(true);
//        }
//        taskRepository.save(task);
//    }
}
