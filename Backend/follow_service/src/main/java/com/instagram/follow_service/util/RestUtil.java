package com.instagram.follow_service.util;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RestUtil {
	
	private WebClient.Builder webClientBuilder;
	
	public RestUtil(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
	
	public void updateUsersFeed(UUID follower, UUID following, boolean follow) {
		String part = follow ? "follow" : "unfollow";
		WebClient client = webClientBuilder.build();
		client.put().uri("http://UserfeedMS/feed/"+part+"/" + follower + "/"+ following)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK))
						return response.bodyToFlux(Integer.class);
					else return response.createError().flux();
				}).subscribe();
		
	}
	
	public void updateFollowerCountInProfileService(UUID follower, UUID following, boolean hasFollowed) {
		WebClient client = webClientBuilder.build();
		client.put().uri("http://ProfileMS/profiles/" + follower + "/"+ following + "/" + hasFollowed)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK))
						return response.bodyToFlux(Integer.class);
					else return response.createError().flux();
				}).subscribe();
	}
}
