package com.Instagram.post_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Instagram.post_service.entity.LikeEntity;

public interface LikeRepository extends MongoRepository<LikeEntity, String>{
	
}
