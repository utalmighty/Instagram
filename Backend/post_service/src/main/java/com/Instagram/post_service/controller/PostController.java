package com.Instagram.post_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Instagram.post_service.model.Comment;
import com.Instagram.post_service.model.MessageResponse;
import com.Instagram.post_service.model.Post;
import com.Instagram.post_service.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("post")
@Validated
public class PostController {
	
	private PostService postService;
	
	PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("status")
	public ResponseEntity<MessageResponse<String>> getStatus() {
		return ResponseEntity.ok(new MessageResponse<>("Post Service Operational!"));
	}
	
	@GetMapping("like/{postId}/{userId}")
	public ResponseEntity<MessageResponse<Boolean>> likeStatus(@PathVariable String postId, @PathVariable UUID userId) {
		return ResponseEntity.ok(new MessageResponse<>(postService.likeStatus(postId, userId)));
	}

	@PutMapping("like/{postId}/{userId}")
	public ResponseEntity<MessageResponse<Integer>> like(@PathVariable String postId, @PathVariable UUID userId) {
		return ResponseEntity.ok(new MessageResponse<>(postService.likePost(postId, userId)));
	}
	
	@GetMapping("like/{count}")
	public ResponseEntity<List<String>> getMostLiked(@PathVariable Integer count) {
		return ResponseEntity.ok(postService.getMostLikedPosts(count));
	}
	
	@PutMapping("view")
	public ResponseEntity<MessageResponse<Integer>> view(@RequestParam String postId) {
		return ResponseEntity.ok(new MessageResponse<>(postService.viewPost(postId)));
	}
	
	@PostMapping("comment")
	public ResponseEntity<MessageResponse<Integer>> comment(@Valid @RequestBody Comment comment) {
		return new ResponseEntity<>(new MessageResponse<>(postService.comment(comment)), HttpStatus.CREATED);
	}
	
	@GetMapping("comment/{postId}")
	public ResponseEntity<List<Comment>> getComments(@PathVariable String postId) {
		// TODO: Apply Pagination
		return ResponseEntity.ok(postService.getComments(postId));
	}
	
	@PostMapping("")
	public ResponseEntity<Post> post(@Valid @RequestBody Post post) {
		return new ResponseEntity<>(postService.postPost(post), HttpStatus.CREATED);
	}
	
	@GetMapping("{postId}")
	public ResponseEntity<Post> getPost(@PathVariable String postId) {
		return ResponseEntity.ok(postService.getPost(postId));
	}
	
	@PostMapping("posts")
	public ResponseEntity<List<Post>> getMultiplePost(@RequestBody List<String> postIds) {
		return ResponseEntity.ok(postService.getPosts(postIds));
	}
	
	@GetMapping("user/postId/{userId}")
	public ResponseEntity<List<String>> getPostIdsOfUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(postService.getPostIdsOfUser(userId));
	}
	
	@GetMapping("user/{userId}")
	public ResponseEntity<List<Post>> getPostsOfUser(@PathVariable UUID userId) {
		// TODO: Apply Pagination
		return ResponseEntity.ok(postService.getPostsOfUser(userId));
	}
	
	@GetMapping("posts/tags")
	public ResponseEntity<List<String>> getTags() {
		return ResponseEntity.ok(postService.getTags());
	}
	
	@GetMapping("posts/tag/{tag}")
	public ResponseEntity<List<String>> getTaggedPost(@PathVariable String tag) {
		return ResponseEntity.ok(postService.getTaggedPost(tag));
	}
	
	@DeleteMapping("{postId}")
	public ResponseEntity<MessageResponse<Boolean>> deletePost(@PathVariable String postId) {
		return new ResponseEntity<>(new MessageResponse<>(postService.deletePost(postId)), HttpStatus.OK);
	}
}
