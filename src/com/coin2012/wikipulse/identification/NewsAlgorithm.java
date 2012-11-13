package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.Page;

public interface NewsAlgorithm {
	
	public List<Page> findNews(String category);
	
	public List<Page> findMostReadPages(String category);
}
