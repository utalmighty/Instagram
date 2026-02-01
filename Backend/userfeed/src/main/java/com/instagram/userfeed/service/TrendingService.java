package com.instagram.userfeed.service;

import java.util.List;

public interface TrendingService {
	
	public void addToTrend(String postId);
	public List<String> getTrendingPosts();

}
