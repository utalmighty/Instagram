package com.instagram.follow_service.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instagram.follow_service.entity.FollowEntity;
import com.instagram.follow_service.repository.FollowRepository;
import com.instagram.follow_service.util.CommonUtil;
import com.instagram.follow_service.util.RestUtil;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

	private FollowRepository followRepository;
	private RestUtil httpUtils;

	public FollowServiceImpl(FollowRepository followRepository, RestUtil httpUtils) {
		this.followRepository = followRepository;
		this.httpUtils = httpUtils;
	}

	@Override
	public Boolean follow(UUID follower, UUID following) {
		String id = CommonUtil.generateId(follower, following);
		if (followRepository.existsById(id)) { // Unfollow
			followRepository.deleteById(id);
			httpUtils.updateUsersFeed(follower, following, false);
			httpUtils.updateFollowerCountInProfileService(follower, following, false);
		} else { // Follow
			FollowEntity entity = new FollowEntity();
			entity.setId(id);
			entity.setFollower(follower);
			entity.setFollowing(following);
			followRepository.save(entity);
			httpUtils.updateUsersFeed(follower, following, true);
			httpUtils.updateFollowerCountInProfileService(follower, following, true);
		}
		return true;
	}

	@Override
	public Boolean isFollowing(UUID follower, UUID following) {
		return followRepository.existsById(CommonUtil.generateId(follower, following));
	}

	@Override
	public List<UUID> getMyFollowers(UUID userId) {
		// TODO Add Pagination
		List<FollowEntity> entities = followRepository.findAllByFollowing(userId);
		return entities.stream().map(f -> f.getFollower()).collect(Collectors.toList());
	}

	@Override
	public List<UUID> getMyFollowings(UUID userId) {
		// TODO Add Pagination
		List<FollowEntity> entities = followRepository.findAllByFollower(userId);
		return entities.stream().map(f -> f.getFollowing()).collect(Collectors.toList());
	}

}
