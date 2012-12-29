package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.News;
import com.google.gson.Gson;

public class Identifier implements Identifiable {
	
	Extractable extractor = new Extractor();

	@Override
	public String getNewsForCategory(String category) {
		
		NewsAlgorithm algorithm = new NewsAlgorithmImpl(extractor);
		
		List<News> edits = algorithm.findNews(category);
		Gson gson = new Gson();
		
		
		return gson.toJson(edits);
	}

}
