package com.Instagram.post_service.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.Instagram.post_service.exception.PostException;
import com.Instagram.post_service.model.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

	private Environment environment;

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

	@ExceptionHandler(PostException.class)
	public ResponseEntity<ErrorResponse> handleProfileException(PostException exception) {
		ErrorResponse error = new ErrorResponse(exception.getMessage(), exception.getStatus());
		return new ResponseEntity<>(error, exception.getStatus());
	}
	
	// TODO: WebClient exception
//	@ExceptionHandler({WebClientRequestException.class, WebClientResponseException.class})
//	public ResponseEntity<ErrorResponse> handleWebClientException(Exception exception) {
//		ErrorResponse error = new ErrorResponse("REST CALLED FALIED", HttpStatus.INTERNAL_SERVER_ERROR);
//		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}
