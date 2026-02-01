package com.instagram.follow_service.service;

import java.util.List;
import java.util.UUID;

public interface FollowService {	
	public Boolean follow(UUID follower, UUID following);
	public Boolean isFollowing(UUID follower, UUID following);
	public List<UUID> getMyFollowers(UUID userId);
	public List<UUID> getMyFollowings(UUID userId);
}
