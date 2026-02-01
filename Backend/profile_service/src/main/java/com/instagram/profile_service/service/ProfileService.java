package com.instagram.profile_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.instagram.profile_service.model.Credentials;
import com.instagram.profile_service.model.Profile;

public interface ProfileService {
	
	public Profile authenticateUser(Credentials creds);
	public boolean changePassword(Credentials creds);
	public boolean isValidUserId(@NonNull UUID userId);
	public Profile register(Profile profile);
	public Profile getUser(UUID userId);
	public Profile getUser(String username);
	public Integer updateFollow(UUID follower, UUID following, boolean hasFollowed);
	public Integer updatePostCount(UUID userId, boolean posted);
	public void verify(UUID userId);
	public List<UUID> getAllUsers();
	public List<String> getAllUsernames();
}
