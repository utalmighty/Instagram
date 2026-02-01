package com.instagram.userfeed.service;

import java.util.List;

public interface FeedService {
	public List<String> getFeed(String userId);
	public void notifyMyFriends(String userId, String postId);
	public void followUser(String follower, String following);
	public void unfollowUser(String follower, String following);
	public void init();
}
