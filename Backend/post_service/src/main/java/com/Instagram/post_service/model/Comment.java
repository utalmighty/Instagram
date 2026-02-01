package com.Instagram.post_service.model;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class Comment {
	@NotNull(message = "COMMENT_USER_ID_NOT_NULL")
	private UUID userId;
	@NotNull(message = "COMMENT_POST_ID_NOT_NULL")
	private String postId;
	@NotNull(message = "COMMENT_NOT_NULL")
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	
	
	
}
