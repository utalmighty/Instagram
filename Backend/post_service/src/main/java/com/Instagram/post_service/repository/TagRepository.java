package com.Instagram.post_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Instagram.post_service.entity.TagEntity;

public interface TagRepository extends MongoRepository<TagEntity, String>{

}
