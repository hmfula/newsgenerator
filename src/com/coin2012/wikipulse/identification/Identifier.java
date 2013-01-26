package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.neo4j.WikipulseGraphDatabase;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class Identifier implements Identifiable {
	
	private static Extractable extractor = new Extractor();
	private static Thread identificationThread;

	//TODO not needed anymore?
	@Override
	public List<News> getNews() {
		
//		return WikipulseGraphDatabase.getLatestNews();
		return null;
	}
	
	

	public static void startIdentificationThread() {
		if (identificationThread == null ) {
			identificationThread = new Thread(new IdentificationRunnable(extractor));
			identificationThread.run();
		}
	}
}
