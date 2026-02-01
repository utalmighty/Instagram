package com.instagram.userfeed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.instagram.userfeed.algos.EditDistance;

@Service
public class SmartSearch implements SearchService {

	private WebClient.Builder webClientBuilder;
	private List<String> usernames;
	private List<String> tags;
	private static final Log LOG = LogFactory.getLog(SmartSearch.class);

	public SmartSearch(WebClient.Builder webClientBuilder) {
		this.usernames = new ArrayList<>();
		this.tags = new ArrayList<>();
		this.webClientBuilder = webClientBuilder;
		bootStrap();
	}

	@Override
	public List<String> tags(String key) {
		List<String> ans = tags.stream()
				.sorted((a, b) -> EditDistance.minDistance(a, key) - EditDistance.minDistance(b, key)).limit(2)
				.collect(Collectors.toList());
		return ans;
	}

	@Override
	public List<String> username(String key) {
		List<String> ans = usernames.stream()
				.sorted((a, b) -> EditDistance.minDistance(a, key) - EditDistance.minDistance(b, key)).limit(2)
				.collect(Collectors.toList());
		return ans;
	}

	@Override
	public void updateTagList(String tag) {
		if (tags.contains(tag))
			return;
		tags.add(tag);
	}

	@Override
	public void updateUsernameList(String username) {
		if (usernames.contains(username))
			return;
		usernames.add(username);
	}

	private void bootStrap() {
		long start = System.currentTimeMillis();
		getAllActiveUsers();
		getAllTags();
		LOG.info("SMART SEARCH INDEXED IN " + (System.currentTimeMillis() - start) + "ms");
	}

	@SuppressWarnings("unchecked")
	private void getAllTags() {
		WebClient client = webClientBuilder.build();
		tags = client.get().uri("http://PostMS/post/posts/tags").exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK))
				return response.bodyToMono(List.class);
			else
				return response.createError();
		}).block();
	}

	@SuppressWarnings("unchecked")
	private void getAllActiveUsers() {
		WebClient client = webClientBuilder.build();
		usernames = client.get().uri("http://ProfileMS/profiles/usernames").exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK))
				return response.bodyToMono(List.class);
			else
				return response.createError();
		}).block();
	}

}
