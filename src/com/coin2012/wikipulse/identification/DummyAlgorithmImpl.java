package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.newscreation.Dummy;
import com.coin2012.wikipulse.models.News;

public class DummyAlgorithmImpl implements NewsAlgorithm {

	Extractable ex;
	
	public DummyAlgorithmImpl(Extractable extractor) {
		ex = extractor;
	}

	@Override
	public List<News> findNews(String category) {
		
		return Dummy.createNewsFromPages(ex.getPagesForCategory(category));
	}

}
