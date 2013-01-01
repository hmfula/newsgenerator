package com.coin2012.wikipulse.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coin2012.wikipulse.extraction.AggregatedChanges;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.News_Counter;
import com.coin2012.wikipulse.identification.Identifiable;
import com.coin2012.wikipulse.identification.Identifier;
import com.coin2012.wikipulse.models.News;
import com.google.gson.Gson;

/**
 * Implementation of the WikipulseService interface.
 *
 */
public class WikipulseServiceImpl implements WikipulseService {

	private Extractor extractor = new Extractor();
	private Gson gson = new Gson();

	// @Override
	// public String getNewsForCategory(String category) {
	// Identifiable identifier = new Identifier();
	// String news = identifier.getNewsForCategory(category);
	// return news;
	// }
	//
	// @Override
	// public String getMostReadTitlesForCategory(String category){
	// Identifiable identifier = new Identifier();
	// String news = identifier.getMostReadTitlesForCategory(category);
	// return news;
	// }
	//
	// @Override
	// public String searchForPagesThatMatch(String searchText){
	// Identifiable identifier = new Identifier();
	// String snippetPages = identifier.searchForPagesThatMatch(searchText);
	// return snippetPages;
	// }
	//
	// @Override
	// public String searchForPagesReferencing(String url){
	// Identifiable identifier = new Identifier();
	// String pages = identifier.searchForPagesReferencing(url);
	// return pages;
	// }
	//
	// @Override
	// public String getPageWithImages(String pageTitle) {
	// return new Identifier().getPageWithImages(pageTitle);
	// }

	@Override
	public String getRecentChanges(String minChanges) {
		int minimumAmountOfChanges;
		try {
			minimumAmountOfChanges = Integer.valueOf(minChanges);
		} catch (Exception e) {
			minimumAmountOfChanges = 10;
		}
		List<AggregatedChanges> listOfAggregatedChanges = extractor.getRecentChanges(minimumAmountOfChanges);
		String result = gson.toJson(listOfAggregatedChanges);

		return result;
	}
	
	@Override
	public String getNews(String nprop) {
		String result = null;
		if(nprop.contains("top10")){
			List<News_Counter> listOfMostReadNews = extractor.getMostReadNews();
			result = gson.toJson(listOfMostReadNews);
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public String getNewsForCategory(String category, String nprop) {
		Identifiable identifier = new Identifier();
		List<News> news = identifier.getNewsForCategory(category);
		enhanceNewsWithProp(news, nprop);
		
		return gson.toJson(news);
	}
	
	@Override
	public void saveUserInteraction(String News) {
		extractor.saveUserInteraction(News);
	}	
	
	/**
	 * Enhances the news with additional info.
	 * 
	 * @param news
	 *            - the news to be enhanced.
	 * @param nprop
	 *            - additional info separated by |. Currentyl only img is
	 *            supported.
	 */
	private void enhanceNewsWithProp(List<News> news, String nprop) {
		Map<String, Boolean> parsedNewsProp = this.parseNewsProp(nprop);
		if (parsedNewsProp.get("img")) {
			extractor.enhanceNewsWithImages(news);
		}
	}

	/**
	 * Checks for all allowed properties. Currently only img is supported.
	 * 
	 * @param nprop
	 *            - list of given properties
	 * @return A map of properties and a boolean which indicated if the
	 *         properties where specified.
	 */
	private Map<String, Boolean> parseNewsProp(String nprop) {
		String[] props = { "img" };
		Map<String, Boolean> properties = new HashMap<String, Boolean>();
		for (String prop : props) {
			properties.put(prop, nprop.contains(prop));
		}
		return properties;
	}

	@Override
	public String getEditors(List<String> editorNames) {
		String result = gson.toJson(extractor.getWikipediaEditors(editorNames));
		return result;

	}
}
