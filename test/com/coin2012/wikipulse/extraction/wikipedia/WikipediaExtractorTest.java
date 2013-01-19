package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.models.Page;

public class WikipediaExtractorTest {
	
	@Test
	public void getPagesWithCategoriesForTitles(){
		List<String> titles = new ArrayList<String>();
		titles.add("736");
		titles.add("20396");
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForPageIds(titles);
		Assert.assertTrue(pages.size()==2);
	}

	@Test
	public void getRecentChangesWithinTwoHours(){
		List<Change> changes = WikipediaExtractor.getRecentChangesWithinTwoHours();
		for (Change change : changes) {
			Assert.assertNotNull(change.getPageid());
		}
	}
}
