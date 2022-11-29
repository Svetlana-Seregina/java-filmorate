package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handlerConstraintViolationException(ConstraintViolationException ex
            , HttpServletRequest request) {
        log.warn("Ошибка валидации запроса: " + ex.getMessage() +
                "\nПуть запроса: "+ request.getServletPath());
        return new ResponseEntity<>(ex.getMessage() +"\nПуть запроса: "+ request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e
            , HttpServletRequest request) {
        log.warn("Ошибка валидации полей объекта: " + e.getFieldError().getDefaultMessage()
                +"\nПуть запроса: " + request.getServletPath());
        return new ResponseEntity<>(e.getFieldError().getDefaultMessage()
                + "\nПуть запроса: " + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.warn("Запрашиваемый объект не найден: " + e.getMessage()
                + "\nПуть запроса: " +  request.getServletPath());
        return new ErrorResponse(e.getMessage() + ". Путь запроса: " + request.getServletPath());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e, HttpServletRequest request) {
        log.warn("Произошла непредвиденная ошибка: " + e.getMessage()
                + "\nПуть запроса: " + request.getServletPath());
        return new ErrorResponse("Произошла непредвиденная ошибка по пути запроса: " + request.getServletPath());
    }
}

