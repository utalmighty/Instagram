package com.Instagram.post_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Instagram.post_service.entity.CommentEntity;

public interface CommentRepository extends MongoRepository<CommentEntity, String>{
	public List<CommentEntity> findAllByPostId(String postId);
}
