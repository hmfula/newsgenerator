package com.coin2012.wikipulse.identification.newsselection;

import java.util.List;

import com.coin2012.wikipulse.models.Page;

public class RecentChanges {
	
	public static void rankPages(List<Page> pages, int rankId) {
		
		double avg = 0.0d;
		
		for (Page page : pages) {
			avg += page.getRecentChanges();
		}
		
		avg = avg / pages.size();
		
		for (Page page: pages) {
			page.setRank(rankId, (page.getRecentChanges() / avg));
		}
	}

}
