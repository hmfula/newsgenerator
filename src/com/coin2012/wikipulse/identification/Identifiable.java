package com.coin2012.wikipulse.identification;

import com.coin2012.wikipulse.models.Page;

import java.util.List;

import com.coin2012.wikipulse.models.SnippetPage;

public interface Identifiable {
	
	public String getNewsForCategory(String category);
	
	public String getMostReadTitlesForCategory(String category);
	
	public List<SnippetPage> searchForPagesThatMatch(String searchText);

	public List<Page> searchForPagesReferencing(String url);
}
