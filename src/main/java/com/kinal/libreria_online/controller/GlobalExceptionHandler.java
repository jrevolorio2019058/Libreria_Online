package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.exceptions.DPIAlreadyUsedException;
import com.kinal.libreria_online.exceptions.EmailAlreadyUsedException;
import com.kinal.libreria_online.exceptions.RoleNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DPIAlreadyUsedException.class)
    public ResponseEntity<String> handleDpiAlreadyUsedException(DPIAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RoleNotValidException.class)
    public ResponseEntity<String> handleRoleNotValidException(RoleNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
