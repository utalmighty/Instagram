package com.Instagram.post_service.service;

import java.util.List;
import java.util.UUID;

import com.Instagram.post_service.model.Comment;
import com.Instagram.post_service.model.Post;

public interface PostService {
	
	public Boolean likeStatus(String postId, UUID userId);
	public Integer likePost(String postId, UUID userId);
	public List<String> getMostLikedPosts(int count);
	
	public Integer viewPost(String postId);
	
	public Integer comment(Comment comment);
	public List<Comment> getComments(String postId);
	
	public Post postPost(Post post);
	public Boolean deletePost(String post);
	public Post getPost(String postId);
	public List<Post> getPosts(List<String> postIds);
	public List<Post> getPostsOfUser(UUID userId);
	public List<String> getPostIdsOfUser(UUID userId);
	
	public void setTag(String tag, String postId);
	public List<String> getTaggedPost(String tag);
	public List<String> getTags();
}
