package com.instagram.userfeed.algos;

public class EditDistance {

	public static int minDistance(String word1, String word2) {
		return tab(word1, word2);
	}
	
	private static int tab(String word2, String word1) {
		int[] prev = new int[word2.length() + 1];
		int[] curr = new int[word2.length() + 1];
		for (int m = 1; m <= word2.length(); m++)
			prev[m] = m;

		for (int n = 1; n <= word1.length(); n++) {
			curr[0] = n;
			for (int m = 1; m <= word2.length(); m++) {
				if (word1.charAt(n - 1) == word2.charAt(m - 1))
					curr[m] = prev[m - 1];
				else
					curr[m] = 1 + Math.min(prev[m - 1], Math.min(prev[m], curr[m - 1]));
			}
			for (int m = 0; m <= word2.length(); m++)
				prev[m] = curr[m];
		}
		return prev[word2.length()];
	}
}
