package com.hotel.exception;

import com.hotel.exception.exceptions.BadRequestExc;
import com.hotel.exception.exceptions.ForbiddenExc;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.exception.exceptions.UnauthorizedExc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    // handle bad request exception
    @ExceptionHandler(BadRequestExc.class)
    public ResponseEntity<Object> handleBadRequestException(Exception exc, WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return handleExceptionInternal(exc, error, null, BAD_REQUEST, request);

    }

    // handle not found exception
    @ExceptionHandler(NotFoundExc.class)
    public ResponseEntity<Object> handleNotFoundException(Exception exc, WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return handleExceptionInternal(exc, error, null, NOT_FOUND, request);
    }

    // handle forbidden exception
    @ExceptionHandler(ForbiddenExc.class)
    public ResponseEntity<Object> handleForbiddenException(Exception exc, WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(FORBIDDEN.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return handleExceptionInternal(exc, error, null, FORBIDDEN, request);
    }

    // Unauthorized 401 exception
    @ExceptionHandler(value = UnauthorizedExc.class)
    public ResponseEntity<Object> handleUnauthorizedException(Exception exc, WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(UNAUTHORIZED.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return handleExceptionInternal(exc, error, null, UNAUTHORIZED, request);
    }

    // handling global errors
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exc) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    // validation handler
    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                           @Nullable HttpHeaders headers,
                                                                           @Nullable HttpStatus status,
                                                                           @Nullable WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }
}
