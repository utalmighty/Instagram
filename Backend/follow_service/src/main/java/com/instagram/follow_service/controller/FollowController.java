package com.instagram.follow_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.follow_service.model.MessageResponse;
import com.instagram.follow_service.service.FollowService;

@RestController
@RequestMapping("follow")
public class FollowController {
	
	private FollowService followService;
	
	public FollowController(FollowService followService) {
		this.followService = followService;
	}
	
	@GetMapping("status")
	public ResponseEntity<MessageResponse<String>> status() {
		return ResponseEntity.ok(new MessageResponse<>("Follow Service Operational!"));
	}
	
	@PostMapping("{follower}/{following}")
	public ResponseEntity<MessageResponse<Boolean>> follow(@PathVariable UUID follower, @PathVariable UUID following) {
		return ResponseEntity.ok(new MessageResponse<>(followService.follow(follower, following)));
	}
	
	@GetMapping("{follower}/{following}")
	public ResponseEntity<MessageResponse<Boolean>> isFollowing(@PathVariable UUID follower, @PathVariable UUID following) {
		return ResponseEntity.ok(new MessageResponse<>(followService.isFollowing(follower, following)));
	}
	
	@GetMapping("followers/{userId}")
	public ResponseEntity<List<UUID>> getMyFollowers(@PathVariable UUID userId) {
		return ResponseEntity.ok(followService.getMyFollowers(userId));
	}
	
	@GetMapping("following/{userId}")
	public ResponseEntity<List<UUID>> getMyFollowings(@PathVariable UUID userId) {
		return ResponseEntity.ok(followService.getMyFollowings(userId));
	}

}
