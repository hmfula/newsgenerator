package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.NewsItem;

public interface NewsAlgorithm {
	
	public List<NewsItem> findNews(String category);
}
