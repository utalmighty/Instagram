package com.instagram.profile_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.instagram.profile_service.entity.ProfileEntity;

public interface ProfileRepository extends CrudRepository<ProfileEntity, UUID> {
	
	Optional<ProfileEntity> findByUsername(String username);
	Optional<ProfileEntity> findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
