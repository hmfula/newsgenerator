package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.coin2012.wikipulse.models.Page;

public class WikipediaExtractorTest {
	
	@Test
	public void getPagesWithCategoriesForTitles(){
		List<String> titles = new ArrayList<String>();
		titles.add("Eintracht Frankfurt");
		titles.add("Hessen");
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForTitles(titles);
	}

}
