package com.example.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.exception.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {BaseException.class})
	public void handleBaseException() {
		
	}
}
