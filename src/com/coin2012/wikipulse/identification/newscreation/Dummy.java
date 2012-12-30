package com.coin2012.wikipulse.identification.newscreation;

import java.util.LinkedList;
import java.util.List;

import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class Dummy {
	
	public static List<News> createNewsFromPages(List<Page> pages) {
		List<News> resultSet = new LinkedList<News>();
		
		for (Page p: pages) {
			News item = new News();
			
			item.setPageId(p.getPageId());
			item.setPagetTitle(p.getTitle());
			
			resultSet.add(item);
		}
		return resultSet;
	}

}