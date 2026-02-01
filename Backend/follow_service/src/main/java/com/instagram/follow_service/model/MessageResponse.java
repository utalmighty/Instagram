package com.instagram.follow_service.model;

public class MessageResponse<T> {
	private T message;

	public MessageResponse(T key) {
		super();
		this.message = key;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}
	
}
