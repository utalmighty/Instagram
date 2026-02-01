package com.Instagram.post_service.util;

import java.util.ArrayList;
import java.util.List;

import com.Instagram.post_service.entity.CommentEntity;
import com.Instagram.post_service.entity.PostEntity;
import com.Instagram.post_service.model.Comment;
import com.Instagram.post_service.model.Post;

public class CommonUtils {

	public static Comment entityToDto(CommentEntity entity) {
		Comment comment = new Comment();
		comment.setComment(entity.getContent());
		comment.setPostId(entity.getPostId());
		comment.setUserId(entity.getUserId());
		return comment;
	}

	public static CommentEntity dtoToEntity(Comment comment) {
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setContent(comment.getComment());
		commentEntity.setPostId(comment.getPostId());
		commentEntity.setUserId(comment.getUserId());
		return commentEntity;
	}

	public static Post entityToDto(PostEntity entity) {
		Post post = new Post();
		post.setCommentCount(entity.getCommentCount());
		post.setId(entity.getId());
		post.setIsActive(entity.getIsActive());
		post.setLikeCount(entity.getLikeCount());
		post.setLinks(entity.getLinks());
		post.setPostContent(entity.getPostContent());
		post.setTimestamp(entity.getTimestamp());
		post.setUserId(entity.getUserId());
		post.setViewCount(entity.getViewCount());
		return post;
	}

	public static PostEntity dtoToEntity(Post post) {
		PostEntity postEntity = new PostEntity();
		postEntity.setCommentCount(post.getCommentCount());
		postEntity.setId(post.getId());
		postEntity.setIsActive(post.getIsActive());
		postEntity.setLikeCount(post.getLikeCount());
		postEntity.setLinks(post.getLinks());
		postEntity.setPostContent(post.getPostContent());
		postEntity.setTimestamp(post.getTimestamp());
		postEntity.setUserId(post.getUserId());
		postEntity.setViewCount(post.getViewCount());
		return postEntity;
	}

	public static List<String> extractTags(String post) {
		List<String> tags = new ArrayList<>();
		for (String word : post.split(" ")) {
			if (word.startsWith("#") && word.length() > 1)
				tags.add(word.substring(1).trim().toLowerCase());
		}
		return tags;
	}

}
