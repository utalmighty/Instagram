package com.instagram.userfeed.service;

import java.util.List;

public interface SearchService {
	
	public List<String> tags(String query);
	public void updateTagList(String tag);
	public List<String> username(String query);
	public void updateUsernameList(String username);

}
