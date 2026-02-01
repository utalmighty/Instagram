package com.instagram.profile_service.util;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.atn.ErrorInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.instagram.profile_service.exception.ProfileException;
import com.instagram.profile_service.model.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

	private Environment environment;
	
	private static final Log log = LogFactory.getLog(ExceptionController.class);

	public ExceptionController(Environment environment) {
		this.environment = environment;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
		List<String> errors = exp.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
		ErrorResponse err = new ErrorResponse(errors, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorResponse>(err, HttpStatus.valueOf(err.getStatusCode()));
	}

	@ExceptionHandler(ProfileException.class)
	public ResponseEntity<ErrorResponse> handleProfileException(ProfileException exception) {
		ErrorResponse error = new ErrorResponse(exception.getMessage(), exception.getStatus());
		return new ResponseEntity<>(error, exception.getStatus());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
		log.error(exception.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse("GENERAL_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
