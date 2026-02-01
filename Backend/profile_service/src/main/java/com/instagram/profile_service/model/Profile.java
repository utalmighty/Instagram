package com.instagram.profile_service.model;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Profile {
	@Null(message = "NULL_UUID")
	private UUID userId;

	@NotNull(message = "FULLNAME_NOT_NULL")
	@Pattern(regexp = "[A-Za-z]+([ ][A-Za-z]+)*", message = "FULLNAME_INVALID")
	private String fullName;

	@NotNull(message = "USERNAME_NOT_NULL")
	@Pattern(regexp = "[A-Za-z0-9._]{3,20}", message = "USERNAME_INVALID")
	private String username;

	@NotNull(message = "EMAIL_NOT_NULL")
	@Email(message = "EMAIL_NOT_VALID")
	private String email;

	private String ppId;
	private Integer followersCount;
	private Integer followingCount;

	@NotNull(message = "PASSWORD_NOT_NULL")
	@Size(min = 8, message = "PASSWORD_INVALID")
	private String password;
	private Integer postCount;
	private boolean isPrivate;
	private boolean isVerified;

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public Integer getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}

	public Integer getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(Integer followingCount) {
		this.followingCount = followingCount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPostCount() {
		return postCount;
	}

	public void setPostCount(Integer postCount) {
		this.postCount = postCount;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
    
}
