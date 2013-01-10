package com.coin2012.wikipulse.identification.newsselection;

import java.util.List;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.models.Page;

public class CommonWorkingSetAuthors {

	public static void rankPages(List<Page> pages, Extractable extractor) {
		
		for (Page p: pages) {
			for (Page q: pages) {
				if (extractor.doArticlesHaveAtleastOneCommonEditor(p, q)) {
					p.increaseCommonWorkingSetAuthorsRank();
				}
			}
		}
	}	
}
