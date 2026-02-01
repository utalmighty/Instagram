package com.instagram.follow_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.instagram.follow_service.entity.FollowEntity;

public interface FollowRepository extends MongoRepository<FollowEntity, String>{
	public List<FollowEntity> findAllByFollower(UUID follower);
	public List<FollowEntity> findAllByFollowing(UUID following);
}
