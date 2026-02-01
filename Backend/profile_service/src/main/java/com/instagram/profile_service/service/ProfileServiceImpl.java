package com.instagram.profile_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.instagram.profile_service.entity.ProfileEntity;
import com.instagram.profile_service.exception.ProfileException;
import com.instagram.profile_service.model.Credentials;
import com.instagram.profile_service.model.Profile;
import com.instagram.profile_service.repository.ProfileRepository;
import com.instagram.profile_service.util.CommonUtils;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

	private ProfileRepository repository;
	private WebClient.Builder webClientBuilder;

	public ProfileServiceImpl(ProfileRepository repository, WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
		this.repository = repository;
	}

	private UUID generateUserId() {
		UUID random = UUID.randomUUID();
		while (isValidUserId(random))
			random = UUID.randomUUID();
		return random;
	}

	@Override
	public Profile register(Profile profile) {
		UUID id = generateUserId();
		if (repository.existsByUsername(profile.getUsername()))
			throw new ProfileException("SERVICE.USERNAME_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
		if (repository.existsByEmail(profile.getEmail()))
			throw new ProfileException("SERVICE.EMAIL_ALREADY_REGISTERED", HttpStatus.BAD_REQUEST);
		ProfileEntity entity = CommonUtils.dtoToEntity(profile);
		entity.setUserId(id);
		entity.setFollowersCount(0);
		entity.setFollowingCount(0);
		entity.setPostCount(0);
		entity.setVerified(false);
		repository.save(entity);
		updateUsernameList(entity.getUsername());
		return CommonUtils.EntityToDto(entity);
	}
	
	private void updateUsernameList(String username) {
		WebClient client = webClientBuilder.build();
		client.put().uri("http://UserfeedMS/feed/update/username/"+username).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(String.class);
			} else {
				return response.createError();
			}
		}).subscribe();
	}

	@Override
	public void verify(UUID userId) {
		ProfileEntity entity = repository.findById(userId)
				.orElseThrow(() -> new ProfileException("SERVICE.USER_NOT_FOUND", HttpStatus.NOT_FOUND));
		entity.setVerified(true);
		repository.save(entity);
	}

	@Override
	public boolean isValidUserId(@NonNull UUID userId) {
		return repository.existsById(userId);
	}

	@Override
	public Integer updateFollow(UUID follower, UUID following, boolean hasFollowed) {
		Optional<ProfileEntity> optFollower = repository.findById(follower);
		Optional<ProfileEntity> optFollowing = repository.findById(following);
		if (optFollower.isEmpty() || optFollowing.isEmpty())
			return 0;
		ProfileEntity followerEntity = optFollower.get();
		ProfileEntity followingEntity = optFollowing.get();
		if (hasFollowed) {
			followerEntity.setFollowingCount(followerEntity.getFollowingCount()+1);
			followingEntity.setFollowersCount(followingEntity.getFollowersCount()+1);
		}
		else {
			followerEntity.setFollowingCount(Math.max(0, followerEntity.getFollowingCount()-1));
			followingEntity.setFollowersCount(Math.max(0, followingEntity.getFollowersCount()-1));
		}
		repository.save(followerEntity);
		repository.save(followingEntity);
		
		return followingEntity.getFollowersCount();
	}

	@Override
	public Integer updatePostCount(UUID userId, boolean posted) {
		Optional<ProfileEntity> optionalEntity = repository.findById(userId);
		if (optionalEntity.isEmpty())
			return 0;
		ProfileEntity entity = optionalEntity.get();
		int updatedCount = entity.getPostCount();
		if (posted)
			 updatedCount += 1;
		else
			updatedCount = Math.max(updatedCount - 1, 0);
		entity.setPostCount(updatedCount);
		return updatedCount;
	}

	@Override
	public Profile getUser(UUID userId) {
		ProfileEntity entity = repository.findById(userId)
				.orElseThrow(() -> new ProfileException("SERVICE.USER_NOT_FOUND", HttpStatus.NOT_FOUND));
		System.out.println(entity);
		return CommonUtils.EntityToDto(entity);
	}

	@Override
	public Profile getUser(String username) {
		ProfileEntity entity = repository.findByUsername(username)
				.orElseThrow(() -> new ProfileException("SERVICE.USER_NOT_FOUND", HttpStatus.NOT_FOUND));
		return CommonUtils.EntityToDto(entity);
	}
	

	@Override
	public Profile authenticateUser(Credentials creds) {
		// Login by username or email
		Optional<ProfileEntity> optEntity = repository.findByUsername(creds.getUsername());
		if (optEntity.isEmpty()) {
			optEntity = repository.findByEmail(creds.getUsername());
			if (optEntity.isEmpty())
				throw new ProfileException("USERNAME_AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED);
		}
		ProfileEntity entity = optEntity.get();
		if (!creds.getPassword().equals(entity.getPassword()))
			throw new ProfileException("PASSWORD_AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED);
		return CommonUtils.EntityToDto(entity);
	}

	@Override
	public boolean changePassword(Credentials creds) {
		ProfileEntity entity = repository.findByUsername(creds.getUsername())
				.orElseThrow(()-> new ProfileException("USERNAME_AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED));
		if (!creds.getEmail().equals(entity.getEmail()))
			throw new ProfileException("EMAIL_AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED);
		entity.setPassword(creds.getPassword());
		repository.save(entity);
		return true;
	}

	@Override
	public List<UUID> getAllUsers() {
		List<UUID> users = new ArrayList<>();
		repository.findAll().forEach(e-> users.add(e.getUserId()));
		return users;
	}

	@Override
	public List<String> getAllUsernames() {
		List<String> users = new ArrayList<>();
		repository.findAll().forEach(e-> users.add(e.getUsername()));
		return users;
	}
}
