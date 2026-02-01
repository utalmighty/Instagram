package com.instagram.profile_service.exception;

import org.springframework.http.HttpStatus;

public class ProfileException extends RuntimeException {

	private static final long serialVersionUID = 7355608L;
	private final String message;
	private final HttpStatus status;

	public ProfileException() {
		message = "PROFILE_EXCEPTION_MESSAGE";
		status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public ProfileException(String message, HttpStatus status) {
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
