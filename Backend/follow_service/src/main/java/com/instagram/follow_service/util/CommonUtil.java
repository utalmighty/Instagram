package com.instagram.follow_service.util;

import java.util.UUID;

public class CommonUtil {
	
	public static String generateId(UUID follower, UUID following) {
		return follower+":"+following;
	}

}
