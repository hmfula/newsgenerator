package com.coin2012.wikipulse.api;

import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.AggregatedChanges;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.ShortNews;
import com.google.gson.Gson;

/**
 * Implementation of the WikipulseService interface.
 * 
 */
public class WikipulseServiceImpl implements WikipulseService {

	private Logger logger = Logger.getLogger(WikipulseServiceImpl.class.toString());
	private Extractor extractor = new Extractor();
	private Gson gson = new Gson();

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
	public String getNews(String sort, String limit) {
		try {
			List<ShortNews> shortNews;
			if (sort.equals("views")) {
				shortNews = extractor.getMostViewedNews(new Integer(limit));
			} else {
				shortNews = extractor.getLatestNews(new Integer(limit));
			}
			String result = gson.toJson(shortNews);
			return result;
		} catch (NumberFormatException e) {
			logger.warning("The given limit: " + limit + " is not an Integer");
			return null;
		}
	}

	@Override
	public String getNews(String newsId) {
		News news = extractor.getNews(newsId);
		String result = gson.toJson(news);
		return result;
	}

	@Override
	public String getNewsForCategory(String category) {
		List<ShortNews> shortNews = extractor.getNewsForCategory(category);
		String result = gson.toJson(shortNews);
		return result;
	}

	@Override
	public void saveUserInteraction(String newsId) {
		extractor.saveUserInteraction(newsId);

	}

	@Override
	public String getCategories(String limit) {
		try {
			List<Category> categories = extractor.getCategories(new Integer(limit));
			String result = gson.toJson(categories);
			return result;
		} catch (Exception e) {
			logger.warning("The given limit: " + limit + " is not an Integer");
			return null;
		}
	}

	// TODO do we need this here? Not based on DB
	@Override
	public String getEditors(List<String> editorNames) {
		String result = gson.toJson(extractor.getWikipediaEditors(editorNames));
		return result;

	}

	// TODO do we need this here?
	@Override
	public String summarizeArticle(String url, String length) {
		String result = gson.toJson(extractor.summarizeArticle(url, length));
		return result;
	}
}
