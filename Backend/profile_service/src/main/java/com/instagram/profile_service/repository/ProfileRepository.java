package com.instagram.profile_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.instagram.profile_service.entity.ProfileEntity;

public interface ProfileRepository extends CrudRepository<ProfileEntity, UUID> {
	
	public Optional<ProfileEntity> findByUsername(String username);
	public Optional<ProfileEntity> findByEmail(String email);
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
}
