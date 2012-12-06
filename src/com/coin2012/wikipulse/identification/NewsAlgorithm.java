package com.coin2012.wikipulse.identification;

import java.util.List;

import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

public interface NewsAlgorithm {
	
	public List<Page> findNews(String category);
	
	public List<Page> findMostReadPages(String category);

	public List<SnippetPage> searchForPagesThatMatch(String searchText);
	
	public List<Page> searchForPagesReferencing(String url);
	
	public List<Page> getPageWithImages(String subjectTitle);
}
