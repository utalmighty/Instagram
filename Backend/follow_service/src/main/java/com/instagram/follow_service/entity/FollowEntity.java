package com.instagram.follow_service.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Follow")
public class FollowEntity {
	
	@Id
	private String id;
	private UUID follower;
	private UUID following;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UUID getFollower() {
		return follower;
	}
	public void setFollower(UUID follower) {
		this.follower = follower;
	}
	public UUID getFollowing() {
		return following;
	}
	public void setFollowing(UUID following) {
		this.following = following;
	}
	
	
	

}
