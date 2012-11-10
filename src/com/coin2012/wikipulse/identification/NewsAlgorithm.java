package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.Title;

public interface NewsAlgorithm {
	
	public List<Title> findNews(String category);
}
