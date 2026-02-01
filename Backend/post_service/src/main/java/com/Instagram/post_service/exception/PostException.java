package com.Instagram.post_service.exception;

import org.springframework.http.HttpStatus;

public class PostException extends RuntimeException{
	private static final long serialVersionUID = 9051L;
	private final String message;
	private final HttpStatus status;

	public PostException() {
		message = "PROFILE_EXCEPTION_MESSAGE";
		status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public PostException(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
