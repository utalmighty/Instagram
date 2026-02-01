package com.instagram.userfeed.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.userfeed.model.MessageResponse;
import com.instagram.userfeed.service.FeedService;
import com.instagram.userfeed.service.SearchService;
import com.instagram.userfeed.service.TrendingService;

@RestController
@RequestMapping("feed")
public class Controller {

	private FeedService feedService;
	private SearchService searchService;
	private TrendingService trendingService;

	Controller(FeedService feedService, SearchService searchService, TrendingService trendingService) {
		this.feedService = feedService;
		this.searchService = searchService;
		this.trendingService = trendingService;
	}

	@GetMapping("status")
	public ResponseEntity<MessageResponse<String>> status() {
		return ResponseEntity.ok(new MessageResponse<>("Feed Service Operational!"));
	}

	@GetMapping("init")
	public String init() {
		feedService.init();
		return "DONE";
	}

	@GetMapping("user/{userId}")
	public ResponseEntity<List<String>> getUserFeed(@PathVariable String userId) {
		return ResponseEntity.ok(feedService.getFeed(userId));
	}

	@PutMapping("notify/{userId}/{postId}")
	public ResponseEntity<MessageResponse<String>> notify(@PathVariable String userId, @PathVariable String postId) {
		feedService.notifyMyFriends(userId, postId);
		return ResponseEntity.ok(new MessageResponse<>("Notified!"));
	}

	@PutMapping("follow/{follower}/{following}")
	public ResponseEntity<MessageResponse<String>> followed(@PathVariable String follower,
			@PathVariable String following) {
		feedService.followUser(follower, following);
		return ResponseEntity.ok(new MessageResponse<>("Follower feed updated"));
	}

	@PutMapping("unfollow/{follower}/{following}")
	public ResponseEntity<MessageResponse<String>> unfollowed(@PathVariable String follower,
			@PathVariable String following) {
		feedService.unfollowUser(follower, following);
		return ResponseEntity.ok(new MessageResponse<>("Following feed deleted"));
	}

	@PutMapping("update/username/{username}")
	public ResponseEntity<MessageResponse<String>> updateUsername(@PathVariable String username) {
		searchService.updateUsernameList(username);
		return ResponseEntity.ok(new MessageResponse<>("Notified!"));
	}

	@PutMapping("update/tag/{tag}")
	public ResponseEntity<MessageResponse<String>> updateTagName(@PathVariable String tag) {
		searchService.updateTagList(tag);
		return ResponseEntity.ok(new MessageResponse<>("Notified!"));
	}

	// Search
	@GetMapping("search/tag/{key}")
	public ResponseEntity<List<String>> searchTag(@PathVariable String key) {
		return ResponseEntity.ok(searchService.tags(key));
	}

	@GetMapping("search/username/{key}")
	public ResponseEntity<List<String>> searchUsername(@PathVariable String key) {
		return ResponseEntity.ok(searchService.username(key));
	}

	// Trending
	@PutMapping("trendsetter/{postId}")
	public ResponseEntity<MessageResponse<String>> trendSetter(@PathVariable String postId) {
		trendingService.addToTrend(postId);
		return ResponseEntity.ok(new MessageResponse<>("Ok"));
	}

	@GetMapping("trendsetter")
	public ResponseEntity<List<String>> getTrendingPosts() {
		return ResponseEntity.ok(trendingService.getTrendingPosts());
	}

}
