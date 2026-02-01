package com.instagram.profile_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.profile_service.model.Credentials;
import com.instagram.profile_service.model.MessageResponse;
import com.instagram.profile_service.model.Profile;
import com.instagram.profile_service.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("profiles")
@Validated
@CrossOrigin()
public class ProfileController {

	private ProfileService service;

	ProfileController(ProfileService service) {
		this.service = service;
	}

	@GetMapping("status")
	public ResponseEntity<MessageResponse<String>> checkStatus() {
		return ResponseEntity.ok(new MessageResponse<>("Profile service operational!"));
	}

	@PostMapping("register")
	public ResponseEntity<Profile> register(@Valid @RequestBody Profile profile) {
		return new ResponseEntity<>(service.register(profile), HttpStatus.CREATED);
	}
	
	@GetMapping("userId/{userId}")
	public ResponseEntity<Profile> getUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getUser(userId));
	}
	
	@GetMapping("username/{username}")
	public ResponseEntity<Profile> getUser(@PathVariable String username) {
		return ResponseEntity.ok(service.getUser(username));
	}
	
	@PutMapping("{follower}/{following}/{hasFollowed}")
	public ResponseEntity<MessageResponse<Integer>> follower(@PathVariable UUID follower, @PathVariable UUID following, @PathVariable Boolean hasFollowed) {
		// This user followed someone: true
		// This user un-followed someone: false
		return ResponseEntity.ok(new MessageResponse<>(service.updateFollow(follower, following, hasFollowed)));
	}
	
	@PutMapping("post/{posted}/{userId}")
	public ResponseEntity<MessageResponse<Integer>> postCount(@PathVariable UUID userId, @PathVariable Boolean posted) {
		return ResponseEntity.ok(new MessageResponse<>(service.updatePostCount(userId, posted)));
	}
	
	@PutMapping("verified/{userId}")
	public ResponseEntity<MessageResponse<String>> verified(@PathVariable UUID userId) {
		return ResponseEntity.ok(new MessageResponse<>("✔️"));
	}
	
	@PostMapping("authenticate")
	public ResponseEntity<Profile> authenticate(@RequestBody Credentials creds) {
		return ResponseEntity.ok(service.authenticateUser(creds));
	}
	
	@PutMapping("passwordreset")
	public ResponseEntity<MessageResponse<Boolean>> passwordReset(@RequestBody Credentials creds) {
		return ResponseEntity.ok(new MessageResponse<>(service.changePassword(creds)));
	}
	
	@GetMapping("users")
	public ResponseEntity<List<UUID>> getAllUsers() {
		return ResponseEntity.ok(service.getAllUsers());
	}
	
	@GetMapping("usernames")
	public ResponseEntity<List<String>> getAllUsername() {
		return ResponseEntity.ok(service.getAllUsernames());
	}
	
}
