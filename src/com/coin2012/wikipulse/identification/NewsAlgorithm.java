package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.News;

public interface NewsAlgorithm {
	
	public List<News> findNews(String category);
	
}
