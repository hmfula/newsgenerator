package com.coin2012.wikipulse.Identification;

import java.util.List;

import com.coin2012.wikipulse.DomainModel.NewsItem;

public interface NewsAlgorithm {
	
	public List<NewsItem> findNews(String category);
}
