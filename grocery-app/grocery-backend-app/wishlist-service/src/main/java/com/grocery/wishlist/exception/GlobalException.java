package com.grocery.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(error.getDefaultMessage(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WishlistAlreadyAddedException.class)
    public ResponseEntity<Map<String,String>> handleWishlistAlreadyAddedException(WishlistAlreadyAddedException ex) {
        Map<String,String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

}
