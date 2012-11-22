package com.coin2012.wikipulse.identification.significancealgorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.identification.NewsAlgorithm;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class SignificanceAlgorithm implements NewsAlgorithm {

	Extractor ex;
	
	public SignificanceAlgorithm(Extractor ex) {
		this.ex = ex;
	}
	
	
	@Override
	public List<Page> findNews(String category) {
		List<Page> titleList = ex.getTitlesForCategory(category);
		List<NewsItem> newsItems = new LinkedList<NewsItem>();
		List<Page> newsList = new LinkedList<Page>();
		
		for (Page p: titleList) {
			newsItems.add(new NewsItem(p));
		}
		
		calculateRanks(newsItems);
		Collections.sort(newsItems, null);
		
		for (NewsItem i: newsItems) {
			newsList.add(i.getPage());
		}
		
		return newsList;
	}

	@Override
	public List<Page> findMostReadPages(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void calculateRanks(List<NewsItem> newsItems) {
		
		for (NewsItem i: newsItems) {
			List<WikiEdit> edits_i = i.getPage().getEdits();
			for (NewsItem j: newsItems) {
				if (! i.equals(j) ) {
					List<WikiEdit> edits_j = j.getPage().getEdits();
					for (WikiEdit e_i: edits_i) {
						for (WikiEdit e_j: edits_j) {
							if (e_i.getUser().equals(e_j.getUser())) {
								i.increaseRank();
							}
						}
					}
				}
			}
		}
	}
}
