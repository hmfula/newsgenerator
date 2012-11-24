package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.LinkedList;
import java.util.List;

import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.coin2012.wikipulse.identification.PageImageDetails;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;
import com.coin2012.wikipulse.models.WikiEdit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WikipediaResultParser extends ResultParser {

	public static List<WikiEdit> parseResultToEdits(String result, String pageId) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject().get("pages").getAsJsonObject()
				.get(pageId).getAsJsonObject().getAsJsonArray("revisions");

		List<WikiEdit> edits = new LinkedList<WikiEdit>();
		if (categorymembers != null) {
			Gson gson = createConfiguredGson();
			for (JsonElement jsonElement : categorymembers) {
				WikiEdit edit = gson.fromJson(jsonElement, WikiEdit.class);
				// edit.setContent(jsonElement.getAsJsonObject().get("*").getAsString());
				edits.add(edit);
			}
		}

		return edits;
	}

	public static List<Page> parseResultToTitles(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("categorymembers");

		Gson gson = createConfiguredGson();
		List<Page> titles = new LinkedList<Page>();
		for (JsonElement jsonElement : categorymembers) {
			Page title = gson.fromJson(jsonElement, Page.class);
			titles.add(title);
		}

		return titles;
	}
	
	/**
	 * TODO  refactor this to create universal parser
	 * @param result
	 * @return list of page snippets
	 */
	public static List<SnippetPage> parseResultToPageSnippets(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray matchingSnnipePageArray = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("search");

		Gson gson = createConfiguredGson();
		List<SnippetPage> pages = new LinkedList<SnippetPage>();
		for (JsonElement jsonElement : matchingSnnipePageArray) {
			SnippetPage page  = gson.fromJson(jsonElement, SnippetPage.class);
			page.setPageUrl(page.getTitle());
			pages.add(page);
		}

		return pages;
	}
	
/**
 * TODO  refactor this to create universal parser
 * @param result
 * @return pages
 */
	public static List<Page> parseResultToMatchingPages(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray matchingPageArray = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("exturlusage");

		Gson gson = createConfiguredGson();
		List<Page> pages = new LinkedList<Page>();
		for (JsonElement jsonElement : matchingPageArray) {
			Page page  = gson.fromJson(jsonElement, Page.class);
			pages.add(page);
		}

		return pages;
	}

public static List<PageImageDetails> parseResultToMatchingPageImages(String result) {
	JsonParser jsonParser = new JsonParser();
	JsonArray matchingPageImageDetailsArray = jsonParser.parse(result).getAsJsonObject()
			.get("query").getAsJsonObject()
			.getAsJsonArray("allimages");

	Gson gson = createConfiguredGson();
	List<PageImageDetails> pageImagesDetailsList = new LinkedList<PageImageDetails>();
	for (JsonElement jsonElement : matchingPageImageDetailsArray) {
		PageImageDetails pageImageDetails  = gson.fromJson(jsonElement, PageImageDetails.class);
		pageImagesDetailsList.add(pageImageDetails);
	}

	return pageImagesDetailsList;
}

}
