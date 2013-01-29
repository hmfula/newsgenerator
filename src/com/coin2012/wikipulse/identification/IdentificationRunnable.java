package com.coin2012.wikipulse.identification;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.identification.newscreation.Dummy;
import com.coin2012.wikipulse.identification.newscreation.SimpleNewsGenerator;
import com.coin2012.wikipulse.identification.newscreation.NewsGenerator;
import com.coin2012.wikipulse.identification.newsselection.AuthorsWithNews;
import com.coin2012.wikipulse.identification.newsselection.CommonWorkingSetAuthors;
import com.coin2012.wikipulse.identification.newsselection.DomainExperts;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class IdentificationRunnable implements Runnable {

	public static final int MIN_RANK = 1;
	
	private Logger logger = Logger.getLogger("AuthorgraphRunnable.class");
	
	public static final String FILENAME ="rankData.csv";
	private PrintWriter fileWriter;

	private Extractable ex;

	public IdentificationRunnable(Extractable extractor) {
		ex = extractor;
	}

	@Override
	public void run() {
		logger.info("Starting identification algorithm.");
		
		try {
			fileWriter = new PrintWriter(new FileWriter(FILENAME));
			fileWriter.println("pageid;pagetitle;totalrank;singleranks0;singleranks1;singleranks2");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
		
		// Testdata start
		//LinkedList<Page> pages = createTestData();
		//Testdata end
		
			// Get list of new edits/pages and save them to the database
			List<Page> pages = ex.getPagesForIdentification();
			ex.enhancePagesWithRelevance(pages);
			//ex.enhancePagesWithEdits(pages);
			
			ex.savePages(pages);
			
			// select pages that are news-worthy
			AuthorsWithNews.rankPages(pages, 0);
			DomainExperts.rankPages(pages, 1);
			CommonWorkingSetAuthors.rankPages(pages, 2, ex);
			
			
			// save ranking data to textfile
			for (Page p: pages) {
				fileWriter.println(p.getPageId()+";"+p.getTitle()+";"+p.getTotalRank()+";"+p.getRanks()[0]+";"+p.getRanks()[1]+";"+p.getRanks()[2]);
				System.out.println(p.getPageId()+";"+p.getTitle()+";"+p.getTotalRank()+";"+p.getRanks()[0]+";"+p.getRanks()[1]+";"+p.getRanks()[2]); // TODO
			}
			fileWriter.flush();
			
			
			// create and sort result set
			List<Page> resultSet = createRankedList(pages, MIN_RANK);
			
			// extract information from pages and generate news
			List <News> newsResults = Dummy.createNewsFromPages(resultSet);
			//List <News> newsResults = NewsGenerator.createNewsFromPages(resultSet);
			//List <News> news = SimpleNewsGenerator.createNewsFromPages(ex , resultSet);
			//System.err.println("############# Number of news items: ->  " + news.size());//for debugging only
			
			ex.saveNews(newsResults);
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Page> createRankedList(List<Page> pages, int minRank) {
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
