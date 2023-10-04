package com.example.backend;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    TaskRepository taskRepository;

    @Test
    void addTask() {
        Task task = new Task();
        task.setTitle("Test");

        Task newtask = taskRepository.save(task);

        System.out.println(newtask.getId());
        System.out.println(newtask.isDone());
        System.out.println((newtask.toString()));
    }



}
