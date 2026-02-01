package com.instagram.userfeed.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FeedServiceImpl implements FeedService {

	private WebClient.Builder webClientBuilder;
	private Map<String, List<String>> usersFeed = new HashMap<>();
	private static final Log LOG = LogFactory.getLog(FeedServiceImpl.class);

	public FeedServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
		bootstrapFeed();
	}

	@Override
	public void init() {
		bootstrapFeed();
	}

	@Override
	public List<String> getFeed(String userId) {
		List<String> userIdPostIds = usersFeed.getOrDefault(userId, Collections.emptyList());
		return userIdPostIds.stream().map(p -> getPostIdFromFormat(p)).collect(Collectors.toList());
	}
	
	@Override
	public void followUser(String follower, String following) {
		List<String> posts = getPostsOfUser(following);
		List<String> originalFeed = usersFeed.getOrDefault(follower, new LinkedList<>());
		originalFeed.addAll(posts);
		usersFeed.put(follower, originalFeed);
		LOG.info("UPDATED FEED: " + usersFeed);
	}

	@Override
	public void unfollowUser(String follower, String following) {
		if (!usersFeed.containsKey(follower))
			return;
		List<String> userIdPostIdList = usersFeed.get(follower);
		usersFeed.put(follower,
				userIdPostIdList.stream().filter(p -> !p.startsWith(following)).collect(Collectors.toList()));
		LOG.info("UPDATED FEED: " + usersFeed);
	}

	@Override
	public void notifyMyFriends(String userId, String postId) {
		List<String> friends = getFollowers(userId);
//		friends.addAll(getFollowings(userId));
		notify(format(postId, userId), friends);
		LOG.info(usersFeed);
	}

	private void notify(String postId, List<String> friends) {
		// Parallel stream
		friends.parallelStream().forEach((aFollower) -> {
			if (!usersFeed.containsKey(aFollower))
				usersFeed.put(aFollower, new LinkedList<>());
			List<String> feed = usersFeed.get(aFollower);
			if (feed.size() >= 20)
				feed.remove(0);
			feed.add(postId);
		});
	}

	private void bootstrapFeed() {
		long start = System.currentTimeMillis();
		// Get all users
		List<String> users = getAllActiveUsers();
		// Make feed for each user
		users.parallelStream().forEach((u) -> {
			List<String> posts = makeFeedFor(u);
			usersFeed.put(u, posts);
			// posts is list of string in format postCreatorsUserId:postId
		});
		LOG.info("FEED GENERATED in " + (System.currentTimeMillis() - start) + "ms");
	}

	private List<String> makeFeedFor(String userId) {
		List<String> posts = new LinkedList<>();
		
		// Get Followings .foreach get all post
		List<String> followings = getFollowings(userId);
		// LOG.info("USER: "+userId+" FOLLOWINGS: "+followings);
		followings.parallelStream().forEach((f) -> {
			posts.addAll(getPostsOfUser(f));
		});

		return posts;
	}

	@SuppressWarnings("unchecked")
	private List<String> getPostsOfUser(String userId) {
		WebClient client = webClientBuilder.build();
		List<String> posts = client.get().uri("http://PostMS/post/user/postId/" + userId).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK))
				return response.bodyToMono(List.class);
			else
				return response.createError();
		}).block();
		return posts.stream().map(i -> format(i, userId)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private List<String> getFollowers(String userId) {
		WebClient client = webClientBuilder.build();
		List<String> followers = client.get().uri("http://FollowMS/follow/followers/" + userId)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK))
						return response.bodyToMono(List.class);
					else
						return response.createError();
				}).block();
		return followers;
	}

	@SuppressWarnings("unchecked")
	private List<String> getFollowings(String userId) {
		WebClient client = webClientBuilder.build();
		List<String> followings = client.get().uri("http://FollowMS/follow/following/" + userId)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK))
						return response.bodyToMono(List.class);
					else
						return response.createError();
				}).block();
		return followings;
	}

	@SuppressWarnings("unchecked")
	private List<String> getAllActiveUsers() {
		WebClient client = webClientBuilder.build();
		List<String> users = client.get().uri("http://ProfileMS/profiles/users").exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK))
				return response.bodyToMono(List.class);
			else
				return response.createError();
		}).block();
		return users;
	}

	private String format(String postId, String userId) {
		return userId + ":" + postId;
	}

	private String getPostIdFromFormat(String id) {
		return id.split(":")[1];
	}
}
