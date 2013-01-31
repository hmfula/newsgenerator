package com.coin2012.wikipulse.identification;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.conf.WikipulseConstants;
import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.newscreation.NewsCreator;
import com.coin2012.wikipulse.identification.newsselection.AuthorsWithNews;
import com.coin2012.wikipulse.identification.newsselection.CommonAuthors;
import com.coin2012.wikipulse.identification.newsselection.DomainExperts;
import com.coin2012.wikipulse.identification.newsselection.RecentChanges;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class IdentificationRunnable implements Runnable {
	
	private Logger logger = Logger.getLogger("AuthorgraphRunnable.class");
	
	public static final String FILENAME ="rankData.csv";
	private PrintWriter fileWriter;

	private Extractable ex;
	private Timespan timer;

	public IdentificationRunnable(Extractable extractor) {
		ex = extractor;
		timer = new Timespan();
	}

	@Override
	public void run() {
		logger.info("Starting identification algorithm.");
		
		if (WikipulseConstants.WRITE_RANK_DATA_FILE) {
			try {
				fileWriter = new PrintWriter(new FileWriter(FILENAME));
				fileWriter.println("timestamp;pageid;pagetitle;totalrank;authorswithnews;domainexperts;commonauthors;yesterdayRelevance");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(WikipulseConstants.IDENTIFICATION_RUNNER_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.info("initial news creation run started");
		
		createNews(10);

		logger.info("initial news creation run finished");
		
		while (true) {
			try {
				Thread.sleep(WikipulseConstants.IDENTIFICATION_RUNNER_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			logger.info("news generation run started");
		
			createNews(3);
			
			logger.info("news generation run finished.");
		}
	}
	
	
	private void createNews(int minChanges) {
		// Testdata start
		//LinkedList<Page> pages = createTestData();
		//Testdata end
		
		long current = System.currentTimeMillis() / 1000;
		long last = current;
		long diff = current - last;
		logger.info("starting:" + diff );
		
		
			// Get list of new edits/pages and save them to the database
			List<Page> pages = ex.getPagesForIdentification(timer, minChanges);
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("gotPagesForIdentification:" + diff );
			last = current;
			
			ex.enhancePagesWithRelevance(pages);
			//ex.enhancePagesWithEdits(pages);
			
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("added Relevance:" + diff );
			last = current;
			
			ex.savePages(pages);
			
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("saved Pages:" + diff );
			last = current;
			
			// select pages that are news-worthy
			AuthorsWithNews.rankPages(pages, 0);
			DomainExperts.rankPages(pages, 1);
			CommonAuthors.rankPages(pages, 2);
			RecentChanges.rankPages(pages, 3);
			
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("ranked Pages:" + diff );
			last = current;
			
			
			// save ranking data to textfile
			if (WikipulseConstants.WRITE_RANK_DATA_FILE) {
				for (Page p: pages) {
					fileWriter.println((new Date()).getTime()+";"+p.getPageId()+";"+p.getTitle()+";"+p.getTotalRank()+";"+p.getRanks()[0]+";"+p.getRanks()[1]+";"+p.getRanks()[2]+";"+p.getRelYesterday()+";"+p.getRanks()[3]);
					System.out.println((new Date()).getTime()+";"+p.getPageId()+";"+p.getTitle()+";"+p.getTotalRank()+";"+p.getRanks()[0]+";"+p.getRanks()[1]+";"+p.getRanks()[2]+";"+p.getRelYesterday()+";"+p.getRanks()[3]); // TODO
				}
				fileWriter.flush();
			}
			
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("saved to File:" + diff );
			last = current;
			
			// create and sort result set
			List<Page> resultSet = createRankedList(pages, WikipulseConstants.MIN_PAGERANK);
			
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("create Ranked List:" + diff );
			last = current;
			
			// enhance result set with content
			for (Page p: resultSet) {
				ex.enhanceEditsWithContent(p.getEdits());
			}
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("added content:" + diff );
			last = current;
			
			// extract content from pages and generate news
//			List <News> newsResults = Dummy.createNewsFromPages(resultSet);
			//List <News> newsResults = NewsGenerator.createNewsFromPages(resultSet);
			//List <News> news = SimpleNewsGenerator.createNewsFromPages(ex , resultSet);
			//System.err.println("############# Number of news items: ->  " + news.size());//for debugging only
			NewsCreator creator = new NewsCreator();
			List <News> newsResults = creator.createNews(resultSet);
			current = System.currentTimeMillis() / 1000;
		    diff = current - last;
			logger.info("created News:" + diff );
			last = current;
			ex.enhanceNewsWithImages(newsResults);
			ex.saveNews(newsResults);
	}

	private List<Page> createRankedList(List<Page> pages, double minRank) {
		List<Page> resultSet = new LinkedList<Page>();
		
		for (Page p: pages) {
			if (p.getTotalRank() > minRank) {
				int i;
			
				for (i = 0; i < resultSet.size(); i++) {
					if (p.getTotalRank() > resultSet.get(i).getTotalRank()) {
						resultSet.add(i, p);
						break;
					}
				}
			
				if (i == resultSet.size()) {
					resultSet.add(p);
				}
			}
		}
		
		return resultSet;
	}

	private LinkedList<Page> createTestData() {
		LinkedList<Page> pages = new LinkedList<Page>();
		
		LinkedList<Category> cl1 = new LinkedList<Category>();
		LinkedList<Category> cl2 = new LinkedList<Category>();
		
		Category c1 = new Category();
		c1.setTitle("Category 1");
		Category c2 = new Category();
		c2.setTitle("Category 2");
		Category c3 = new Category();
		c3.setTitle("Category 3");
		
		cl1.add(c1);
		cl1.add(c2);
		
		cl2.add(c2);
		cl2.add(c3);
		
		for (int i = 0; i < 10; i++) {
			Page p = new Page();
			p.setPageid(""+i);
			p.setTitle("Test "+i);
			
			if (i % 2 == 0) {
				p.setCategories(cl1);
			} else {
				p.setCategories(cl2);
			}
			
			LinkedList<WikiEdit> edits = new LinkedList<WikiEdit>();
			
			for (int j = 0; j < 5; j++) {
				WikiEdit e = new WikiEdit();
				e.setRevid(i+""+j);
				e.setUserid(""+j);
				e.setUser("User "+j);
			
				edits.add(e);
			}
			
			p.setEdits(edits);
			pages.add(p);
		}
		
		return pages;
	}
}