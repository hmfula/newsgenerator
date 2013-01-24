package com.coin2012.wikipulse.identification;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.neo4j.AuthorgraphDatabase;
import com.coin2012.wikipulse.identification.newscreation.Dummy;
import com.coin2012.wikipulse.identification.newsselection.Authorgraph;
import com.coin2012.wikipulse.identification.newsselection.CommonWorkingSetAuthors;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;

public class IdentificationRunnable implements Runnable {

	private Logger logger = Logger.getLogger("AuthorgraphRunnable.class");

	private Extractable ex;

	public IdentificationRunnable(Extractable extractor) {
		ex = extractor;
	}

	@Override
	public void run() {
		logger.info("Starting authorgraph-database updater");

		while (true) {
			
			// Get list of new edits/pages and save them to the database
			List<Page> pages = ex.getPagesForIdentification();
			ex.enhancePagesWithEdits(pages);
			ex.enhancePagesWithRelevance(pages);
			

			for (Page p : pages) {
				AuthorgraphDatabase.savePage(p);
			}

			
			// select pages that are news-worthy
			Authorgraph.rankPages(pages);
			CommonWorkingSetAuthors.rankPages(pages, ex);
			
			
			// create and sort result set
			List<Page> resultSet = new LinkedList<Page>();
			
			for (Page p: pages) {
				int i;
				
				for (i = 0; i < resultSet.size(); i++) {
					if ((p.getAuthorgraphRank()+p.getCommonWorkingSetAuthorsRank()) > (resultSet.get(i).getAuthorgraphRank()+resultSet.get(i).getCommonWorkingSetAuthorsRank())) {
						resultSet.add(i, p);
						break;
					}
				}
				
				if (i == resultSet.size()) {
					resultSet.add(p);
				}
			}
			
			// extract information from pages and generate news
			List <News> newsResults = Dummy.createNewsFromPages(resultSet); // dummy for now
			
			// save News to database
			for (News n: newsResults) {
				AuthorgraphDatabase.saveNews(n);
			}
			
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
