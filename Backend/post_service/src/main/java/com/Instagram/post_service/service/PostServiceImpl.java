package com.Instagram.post_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.Instagram.post_service.entity.CommentEntity;
import com.Instagram.post_service.entity.LikeEntity;
import com.Instagram.post_service.entity.PostEntity;
import com.Instagram.post_service.entity.TagEntity;
import com.Instagram.post_service.exception.PostException;
import com.Instagram.post_service.model.Comment;
import com.Instagram.post_service.model.Post;
import com.Instagram.post_service.repository.CommentRepository;
import com.Instagram.post_service.repository.LikeRepository;
import com.Instagram.post_service.repository.PostRepository;
import com.Instagram.post_service.repository.TagRepository;
import com.Instagram.post_service.util.CommonUtils;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	private CommentRepository commentRepository;
	private LikeRepository likeRepository;
	private PostRepository postRepository;
	private TagRepository tagRepository;
	private WebClient.Builder webClientBuilder;

	public PostServiceImpl(CommentRepository commentRepository, LikeRepository likeRepository,
			PostRepository postRepository, TagRepository tagRepository, WebClient.Builder webClientBuilder) {
		this.commentRepository = commentRepository;
		this.likeRepository = likeRepository;
		this.postRepository = postRepository;
		this.tagRepository = tagRepository;
		this.webClientBuilder = webClientBuilder;
	}

	@Override
	public Boolean likeStatus(String postId, UUID userId) {
		String likeId = postId + ":" + userId;
		return likeRepository.existsById(likeId);
	}

	@Override
	public Integer likePost(String postId, UUID userId) {
		String likeId = postId + ":" + userId;
		boolean likeExists = likeRepository.existsById(likeId);
		if (likeExists) {
			// Unlike
			LikeEntity entity = likeRepository.findById(likeId)
					.orElseThrow(() -> new PostException("UNLIKED_FAILED", HttpStatus.INTERNAL_SERVER_ERROR));
			likeRepository.delete(entity);
			return updateLike(postId, -1);
		}
		LikeEntity entity = new LikeEntity();
		entity.setId(likeId);
		likeRepository.save(entity);
		return updateLike(postId, 1);
	}

	@Override
	public List<String> getMostLikedPosts(int count) {
		List<PostEntity> entites = postRepository.findAll();
		return entites.stream().filter(p -> p.getIsActive()).sorted((a, b) -> b.getLikeCount() - a.getLikeCount())
				.limit(count).map(p -> p.getId()).collect(Collectors.toList());
	}

	@Override
	public Integer viewPost(String postId) {
		PostEntity entity = postRepository.findById(postId)
				.orElseThrow(() -> new PostException("POST_NOT_FOUND", HttpStatus.NOT_FOUND));
		entity.setViewCount(entity.getViewCount() + 1);
		postRepository.save(entity);
		return entity.getViewCount();
	}

	@Override
	public Integer comment(Comment comment) {
		CommentEntity commentEntity = CommonUtils.dtoToEntity(comment);
		int commentCount = updateCommentCount(comment.getPostId());
		commentRepository.save(commentEntity);
		return commentCount;
	}

	@Override
	public List<Comment> getComments(String postId) {
		List<CommentEntity> entites = commentRepository.findAllByPostId(postId);
		return entites.stream().map(c -> CommonUtils.entityToDto(c)).collect(Collectors.toList());
	}

	@Override
	public Post postPost(Post post) {
		PostEntity entity = CommonUtils.dtoToEntity(post);
		List<String> tags = CommonUtils.extractTags(post.getPostContent());
		entity.setLikeCount(0);
		entity.setTimestamp(LocalDateTime.now());
		entity.setViewCount(0);
		entity.setCommentCount(0);
		entity.setIsActive(true);

		postRepository.save(entity);
		updatePostCount(entity.getUserId(), true);
		notifyFriends(post.getUserId(), entity.getId());
		tags.forEach((tag) -> setTag(tag, entity.getId()));
		updateTagIndex(tags);
		return CommonUtils.entityToDto(entity);
	}

	@Override
	public Boolean deletePost(String postId) {
		Optional<PostEntity> optEntity = postRepository.findById(postId);
		if (optEntity.isPresent()) {
			PostEntity entity = optEntity.get();
			updatePostCount(entity.getUserId(), false);
			entity.setIsActive(false);
			postRepository.save(entity);
			return true;
		}
		return false;
	}

	@Override
	public Post getPost(String postId) {
		// TODO: 💡 Is findById efficient than findAllById(List)?
		List<Post> post = getPosts(List.of(postId));
		if (post.isEmpty())
			throw new PostException("POST_NOT_FOUND", HttpStatus.NOT_FOUND);
		return post.get(0);
	}

	@Override
	public List<Post> getPosts(List<String> postIds) {
		// TODO: what if any one of it is missing in db?
		// Note: result.size <= postIds.size
		List<PostEntity> entities = postRepository.findAllById(postIds);
		return entities.parallelStream().filter(p -> p.getIsActive())
				.sorted((a, b) -> postIds.indexOf(a.getId()) - postIds.indexOf(b.getId()))
				.map(p -> CommonUtils.entityToDto(p)).collect(Collectors.toList());
	}

	@Override
	public List<Post> getPostsOfUser(UUID userId) {
		List<PostEntity> entities = postRepository.findAllByUserId(userId);
		return entities.stream().filter(p -> p.getIsActive()).map(p -> CommonUtils.entityToDto(p))
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getPostIdsOfUser(UUID userId) {
		List<PostEntity> entities = postRepository.findAllByUserId(userId);
		return entities.stream().sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
				.filter(p -> p.getIsActive()).map(p -> p.getId()).limit(5).collect(Collectors.toList());
	}

	@Override
	public void setTag(String tag, String postId) {
		TagEntity entity = tagRepository.findById(tag.trim().toLowerCase()).orElseGet(() -> {
			TagEntity e = new TagEntity();
			e.setId(tag.trim().toLowerCase());
			e.setPostId(List.of());
			return e;
		});
		List<String> posts = new ArrayList<>(entity.getPostId());
		posts.add(postId);
		entity.setPostId(posts);
		tagRepository.save(entity);
	}

	@Override
	public List<String> getTaggedPost(String tag) {
		TagEntity entity = tagRepository.findById(tag.trim().toLowerCase()).orElseGet(() -> {
			TagEntity e = new TagEntity();
			e.setId(tag.trim().toLowerCase());
			e.setPostId(List.of());
			return e;
		});
		return entity.getPostId();
	}

	private void updatePostCount(UUID userId, boolean posted) {
		WebClient webClient = webClientBuilder.build();
		webClient.put().uri("http://ProfileMS/profiles/post/" + posted + "/" + userId).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(String.class);
			} else {
				return response.createError();
			}
		}).subscribe();
	}

	private int updateCommentCount(String postId) {
		PostEntity entity = postRepository.findById(postId)
				.orElseThrow(() -> new PostException("POST_NOT_FOUND", HttpStatus.NOT_FOUND));
		entity.setCommentCount(entity.getCommentCount() + 1);
		postRepository.save(entity);
		return entity.getCommentCount();
	}

	private int updateLike(String postId, int by) {
		updateTrending(postId);
		PostEntity entity = postRepository.findById(postId)
				.orElseThrow(() -> new PostException("POST_NOT_FOUND", HttpStatus.NOT_FOUND));
		entity.setLikeCount(entity.getLikeCount() + (1 * by));
		postRepository.save(entity);
		return entity.getLikeCount();
	}

	private void updateTrending(String postId) {
		WebClient webClient = webClientBuilder.build();
		webClient.put().uri("http://UserfeedMS/feed/trendsetter/" + postId).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(String.class);
			} else {
				return response.createError();
			}
		}).subscribe();

	}

	private void notifyFriends(UUID userId, String id) {
		WebClient webClient = webClientBuilder.build();
		webClient.put().uri("http://UserfeedMS/feed/notify/" + userId + "/" + id).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(String.class);
			} else {
				return response.createError();
			}
		}).subscribe();
	}

	private void updateTagIndex(List<String> tags) {
		tags.stream().forEach(tag -> {
			WebClient webClient = webClientBuilder.build();
			webClient.put().uri("http://UserfeedMS/feed/update/tag/" + tag).exchangeToMono(response -> {
				if (response.statusCode().equals(HttpStatus.OK)) {
					return response.bodyToMono(String.class);
				} else {
					return response.createError();
				}
			}).subscribe();
		});
	}

	@Override
	public List<String> getTags() {
		return tagRepository.findAll().parallelStream().map(t -> t.getId()).collect(Collectors.toList());
	}

}
