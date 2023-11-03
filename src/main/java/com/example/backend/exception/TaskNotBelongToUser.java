package com.example.backend.exception;

/**
 * Created by Admin on 11/2/2023
 */
public class TaskNotBelongToUser extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public TaskNotBelongToUser(String mess){
        super(mess);
    }
}
