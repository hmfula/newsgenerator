package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class WikipediaExtractorTest {
	
	
	public void getPagesWithCategoriesForTitles(){
		List<String> titles = new ArrayList<String>();
		titles.add("736");
		titles.add("20396");
		List<Page> pages = WikipediaExtractor.getPagesWithCategoriesForPageIds(titles);
		Assert.assertTrue(pages.size()==2);
	}

	
	public void getRecentChangesWithinTwoHours(){
		List<Change> changes = WikipediaExtractor.getRecentChangesWithinTwoHours();
		for (Change change : changes) {
			Assert.assertNotNull(change.getPageid());
		}
	}
	
	@Test
	public void updateEditsWithContent(){
		List<WikiEdit> edits = new ArrayList<WikiEdit>();
		WikiEdit edit = new WikiEdit();
		edit.setRevid("535072278");
		edits.add(edit);
		WikipediaExtractor.updateEditsWithContent(edits);
	}
}
