package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.News;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public List<News> getNewsForCategory(String category) {
		
		NewsAlgorithm algorithm = new NewsAlgorithmImpl(extractor);
		
		return algorithm.findNews(category);
	}

}
