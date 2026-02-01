package com.Instagram.post_service.model;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
	
	private List<String> message;
	private Integer statusCode;
	
	public ErrorResponse(String message) {
		this.message = List.of(message);
		statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
	}
	
	public ErrorResponse(String message, Integer statusCode) {
		this(List.of(message), statusCode);
	}
	public ErrorResponse(String message, HttpStatus status) {
		this(message, status.value());
	}
	
	public ErrorResponse(List<String> messages, Integer statusCode) {
		this.message = messages;
		this.statusCode = statusCode;
	}
	public ErrorResponse(List<String> messages, HttpStatus status) {
		this(messages, status.value());
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}
