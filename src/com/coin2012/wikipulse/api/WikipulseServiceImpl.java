package com.coin2012.wikipulse.api;

import java.util.List;

import com.coin2012.wikipulse.extraction.ExtractorImpl;
import com.coin2012.wikipulse.extraction.hsqldb.AggregatedChanges;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.ShortNews;
import com.google.gson.Gson;

/**
 * Implementation of the WikipulseService interface.
 * 
 */
public class WikipulseServiceImpl implements WikipulseService {

	private ExtractorImpl extractor = new ExtractorImpl();
	private Gson gson = new Gson();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#getRecentChanges(java.lang
	 * .String)
	 */
	@Override
	public String getRecentChanges(int minChanges) {
		List<AggregatedChanges> listOfAggregatedChanges = extractor.getRecentChanges(minChanges);
		String result = gson.toJson(listOfAggregatedChanges);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#getNews(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getNews(String sort, int limit) {
			List<ShortNews> shortNews;
			if (sort.equals("views")) {
				shortNews = extractor.getMostViewedNews(limit);
			} else {
				shortNews = extractor.getLatestNews(limit);
			}
			String result = gson.toJson(shortNews);
			return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#getNews(java.lang.String)
	 */
	@Override
	public String getNews(String newsId) {
		News news = extractor.getNews(newsId);
		String result = gson.toJson(news);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#getNewsForCategory(java.lang
	 * .String)
	 */
	@Override
	public String getNewsForCategory(String category) {
		List<ShortNews> shortNews = extractor.getNewsForCategory(category);
		String result = gson.toJson(shortNews);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#saveUserInteraction(java.
	 * lang.String)
	 */
	@Override
	public void saveUserInteraction(String newsId) {
		extractor.saveUserInteraction(newsId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coin2012.wikipulse.api.WikipulseService#getCategories(java.lang.String
	 * )
	 */
	@Override
	public String getCategories(int limit) {
		List<Category> categories = extractor.getCategories(limit);
		String result = gson.toJson(categories);
		return result;
	}
}
