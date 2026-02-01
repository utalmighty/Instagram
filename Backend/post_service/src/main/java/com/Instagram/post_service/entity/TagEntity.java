package com.Instagram.post_service.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Tag")
public class TagEntity {
	
	@Id
	private String id;
	private List<String> postId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getPostId() {
		return postId;
	}
	public void setPostId(List<String> postId) {
		this.postId = postId;
	}
}
