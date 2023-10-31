package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Can't disable yourself!")
public class StaffSelfDisableException extends RestClientException {
    public StaffSelfDisableException(String msg) {
        super(msg);
    }
}
