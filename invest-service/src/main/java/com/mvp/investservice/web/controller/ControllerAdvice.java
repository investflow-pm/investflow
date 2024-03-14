package com.mvp.investservice.web.controller;

import com.mvp.investservice.domain.exception.CannotProceedApiRequestException;
import com.mvp.investservice.domain.exception.ExceptionBody;
import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.domain.exception.UpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionBody handleIllegalStateException(ResourceNotFoundException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(UpdateException.class)
    public ExceptionBody handleUpdateException(UpdateException exception) {
        return new ExceptionBody(exception.getMessage());
    }

    @ExceptionHandler(CannotProceedApiRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleApiRequestException(CannotProceedApiRequestException exception) {
        return new ExceptionBody(exception.getMessage());
    }

}
