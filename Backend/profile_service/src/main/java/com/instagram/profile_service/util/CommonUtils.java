package com.instagram.profile_service.util;

import org.springframework.util.StringUtils;

import com.instagram.profile_service.entity.ProfileEntity;
import com.instagram.profile_service.model.Profile;

public class CommonUtils {

	public static ProfileEntity dtoToEntity(Profile profile) {
		ProfileEntity entity = new ProfileEntity();
		entity.setUserId(profile.getUserId());
		entity.setEmail(profile.getEmail());
		entity.setFollowersCount(profile.getFollowersCount());
		entity.setFollowingCount(profile.getFollowingCount());
		entity.setFullName(toTitleCase(profile.getFullName()));
		entity.setPassword(profile.getPassword());
		entity.setPostCount(profile.getPostCount());
		entity.setPpId(profile.getPpId());
		entity.setPrivate(profile.isPrivate());
		entity.setUsername(profile.getUsername());
		entity.setVerified(profile.isVerified());
		return entity;
	}

	public static Profile EntityToDto(ProfileEntity entity) {
		Profile dto = new Profile();
		dto.setUserId(entity.getUserId());
		dto.setEmail(entity.getEmail());
		dto.setFollowersCount(entity.getFollowersCount());
		dto.setFollowingCount(entity.getFollowingCount());
		dto.setFullName(entity.getFullName());
		dto.setPassword("MASKED");
		dto.setPostCount(entity.getPostCount());
		dto.setPpId(entity.getPpId());
		dto.setPrivate(entity.isPrivate());
		dto.setUsername(entity.getUsername());
		dto.setVerified(entity.isVerified());
		return dto;
	}

	public static String toTitleCase(String name) {
		StringBuilder sb = new StringBuilder();
		for (String word : name.split(" ")) {
			word = word.trim();
			sb.append(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ");
		}
		return sb.toString().trim();
	}

}
