package com.Instagram.post_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Instagram.post_service.entity.PostEntity;

public interface PostRepository extends MongoRepository<PostEntity, String>{

	public List<PostEntity> findAllByUserId(UUID userId);
}
