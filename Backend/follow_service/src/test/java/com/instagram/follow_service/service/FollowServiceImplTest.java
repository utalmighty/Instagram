package com.instagram.follow_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.instagram.follow_service.entity.FollowEntity;
import com.instagram.follow_service.repository.FollowRepository;
import com.instagram.follow_service.util.CommonUtil;
import com.instagram.follow_service.util.RestUtil;

@SpringBootTest
public class FollowServiceImplTest {

	@Mock
	RestUtil restUtil;

	@Mock
	FollowRepository followRepository;

	@InjectMocks
	FollowServiceImpl followService;

	@Test
	void followUserTest() {
		UUID follower = UUID.fromString("12345678-1234-1234-1234-123412341234");
		UUID following = UUID.fromString("11112222-3333-4444-5555-666677778888");
		String id = CommonUtil.generateId(follower, following);
		when(followRepository.existsById(id)).thenReturn(false);
		when(followRepository.save(any())).thenReturn(new FollowEntity());
		doNothing().when(restUtil).updateUsersFeed(follower, following, true);
		doNothing().when(restUtil).updateFollowerCountInProfileService(follower, following, true);
		assertTrue(followService.follow(follower, following));
	}
	
	@Test
	void unfollowUserTest() {
		UUID follower = UUID.fromString("12345678-1234-1234-1234-123412341234");
		UUID following = UUID.fromString("11112222-3333-4444-5555-666677778888");
		String id = CommonUtil.generateId(follower, following);
		when(followRepository.existsById(id)).thenReturn(true);
		doNothing().when(followRepository).delete(any());
		doNothing().when(restUtil).updateUsersFeed(follower, following, true);
		doNothing().when(restUtil).updateFollowerCountInProfileService(follower, following, true);
		assertTrue(followService.follow(follower, following));
	}
	
	@Test
	void isFollowingTrueTest() {
		UUID follower = UUID.fromString("12345678-1234-1234-1234-123412341234");
		UUID following = UUID.fromString("11112222-3333-4444-5555-666677778888");
		String id = CommonUtil.generateId(follower, following);
		when(followRepository.existsById(id)).thenReturn(true);
		
		assertTrue(followService.isFollowing(follower, following));
	}
	
	@Test
	void isFollowingFalseTest() {
		UUID follower = UUID.fromString("12345678-1234-1234-1234-123412341234");
		UUID following = UUID.fromString("11112222-3333-4444-5555-666677778888");
		String id = CommonUtil.generateId(follower, following);
		when(followRepository.existsById(id)).thenReturn(false);
		
		assertFalse(followService.isFollowing(follower, following));
	}
	
	@Test
	void getFollowersTest() {
		UUID personA = UUID.fromString("12345678-1234-1234-1234-123412341234");
		when(followRepository.findAllByFollowing(personA)).thenReturn(List.of(new FollowEntity(), new FollowEntity()));
		
		assertEquals(2, followService.getMyFollowers(personA).size());
	}
	
	@Test
	void getFollowingsTest() {
		UUID personA = UUID.fromString("12345678-1234-1234-1234-123412341234");
		when(followRepository.findAllByFollower(personA)).thenReturn(List.of(new FollowEntity(), new FollowEntity()));
		
		assertEquals(2, followService.getMyFollowings(personA).size());
	}
}
