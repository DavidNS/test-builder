package com.dns.resttestbuilder.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {
	
	
	
	@ResponseBody
	@ExceptionHandler(UserIDNotFoundException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String userIDNotFoundException(UserIDNotFoundException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(InvalidUserIDException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String invalidUserID(InvalidUserIDException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String noFound(NotFoundException ex) {
		return ex.getMessage();
	}
	
	

}
