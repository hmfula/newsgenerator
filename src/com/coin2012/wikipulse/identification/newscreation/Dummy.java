package com.coin2012.wikipulse.identification.newscreation;

import java.util.LinkedList;
import java.util.List;

import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class Dummy {
	
	public static List<News> createNewsFromPages(List<Page> pages) {
		List<News> resultSet = new LinkedList<News>();
		
		for (Page p: pages) {
			News item = new News();
			
			item.setPageId(p.getPageId());
			item.setPagetTitle(p.getTitle());
			item.setRelYesterday(p.getRelYesterday());
			item.setNews(p.getTitle());
			
			// TODO
			Editor e = new Editor();
			e.setUserid("1");
			item.setEditor(e);
			
			resultSet.add(item);
		}
		return resultSet;
	}

}
