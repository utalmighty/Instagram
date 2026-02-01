package com.instagram.userfeed.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.instagram.userfeed.algos.LRU;

@Service
public class TrendingServiceImpl implements TrendingService{
	
	private int trendingSize = 25;
	private WebClient.Builder webClientBuilder;
	private LRU trendings;
	private static final Log LOG = LogFactory.getLog(TrendingServiceImpl.class);
	
	public TrendingServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
		this.trendings = new LRU(trendingSize);
		bootstrap();
	}
	
	private void bootstrap() {
		long start = System.currentTimeMillis();
		List<String> posts = getMostLikedPost();
		for(int i=posts.size()-1; i>=0; i--)
			trendings.add(posts.get(i));
		// since last add will give highest priority
		LOG.info("TRENDING FEED CREATED IN " + (System.currentTimeMillis() - start) + "ms");
	}

	@SuppressWarnings("unchecked")
	private List<String> getMostLikedPost() {
		WebClient client = webClientBuilder.build();
		List<String> mostLikedPosts = client.get().uri("http://PostMS/post/like/"+trendingSize).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK))
				return response.bodyToMono(List.class);
			else
				return response.createError();
		}).block();
		return mostLikedPosts;
	}

	@Override
	public void addToTrend(String postId) {
		trendings.add(postId);
	}

	@Override
	public List<String> getTrendingPosts() {
		return trendings.getData();
	}

}
