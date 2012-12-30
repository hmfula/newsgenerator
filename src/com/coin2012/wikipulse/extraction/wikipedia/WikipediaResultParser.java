package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.coin2012.wikipulse.extraction.Editor;
import com.coin2012.wikipulse.extraction.utils.ResultParser;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.extraction.utils.models.RecentChangesQueryResult;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WikipediaResultParser extends ResultParser {

	private static final Gson gson = createConfiguredGson();
	private static final JsonParser jsonParser = new JsonParser();

	public static List<WikiEdit> parseResultToEdits(String result, String pageId) {
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().get("pages").getAsJsonObject()
				.get(pageId).getAsJsonObject().getAsJsonArray("revisions");

		List<WikiEdit> edits = new LinkedList<WikiEdit>();
		if (categorymembers != null) {
			for (JsonElement jsonElement : categorymembers) {
				WikiEdit edit = gson.fromJson(jsonElement, WikiEdit.class);
				// edit.setContent(jsonElement.getAsJsonObject().get("*").getAsString());
				edits.add(edit);
			}
		}

		return edits;
	}

	public static List<Page> parseResultToPages(String result) {
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().getAsJsonArray("categorymembers");

		List<Page> titles = new LinkedList<Page>();
		for (JsonElement jsonElement : categorymembers) {
			Page title = gson.fromJson(jsonElement, Page.class);
			titles.add(title);
		}

		return titles;
	}

//	/**
//	 * TODO refactor this to create universal parser
//	 * 
//	 * @param result
//	 * @return list of page snippets
//	 */
//	public static List<SnippetPage> parseResultToPageSnippets(String result) {
//		JsonArray matchingSnnipePageArray = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().getAsJsonArray("search");
//
//		List<SnippetPage> pages = new LinkedList<SnippetPage>();
//		for (JsonElement jsonElement : matchingSnnipePageArray) {
//			SnippetPage page = gson.fromJson(jsonElement, SnippetPage.class);
//			page.setPageUrl(page.getTitle());
//			pages.add(page);
//		}
//
//		return pages;
//	}
//
//	// TODO refactor this to create universal parser
//
//	public static List<Page> parseResultToMatchingPages(String result) {
//		JsonArray matchingPageArray = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().getAsJsonArray("exturlusage");
//
//		List<Page> pages = new LinkedList<Page>();
//		for (JsonElement jsonElement : matchingPageArray) {
//			Page page = gson.fromJson(jsonElement, Page.class);
//			pages.add(page);
//		}
//
//		return pages;
//	}

	public static List<String> parseResultToImageFileNames(String result) {
		List<String> imageFileNames = new ArrayList<String>();
		Set<Entry<String, JsonElement>> pageEntryMap = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().get("pages")
				.getAsJsonObject().entrySet();
		for (Entry<String, JsonElement> entry : pageEntryMap) {
			JsonArray jsonArray = entry.getValue().getAsJsonObject().getAsJsonArray("images");
			for (JsonElement jsonElement : jsonArray) {
				String imageFileName = jsonElement.getAsJsonObject().get("titel").getAsString();
				imageFileNames.add(imageFileName);
			}
		}
		return imageFileNames;
	}

	public static List<String> parseResultToImageURLs(String result) {
		List<String> imageUrls = new ArrayList<String>();
		Set<Entry<String, JsonElement>> set = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().get("pages")
				.getAsJsonObject().entrySet();
		for (Entry<String, JsonElement> entry : set) {
			JsonObject page = entry.getValue().getAsJsonObject();
			JsonArray imageinfo = page.get("imageinfo").getAsJsonArray();
			JsonElement firstElement = imageinfo.get(0);
			String url = firstElement.getAsJsonObject().get("url").getAsString();
			imageUrls.add(url);

		}
		return imageUrls;
	}

	public static RecentChangesQueryResult parseResultToRecentChangesQueryResult(String result) {
		RecentChangesQueryResult parsedResult = new RecentChangesQueryResult();
		JsonParser jsonParser = new JsonParser();
		Gson gson = createConfiguredGson();
		JsonArray recentchanges = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().getAsJsonArray("recentchanges");
		for (JsonElement jsonElement : recentchanges) {
			Change change = gson.fromJson(jsonElement, Change.class);
			parsedResult.getChanges().add(change);
		}
		if (jsonParser.parse(result).getAsJsonObject().has("query-continue")) {
			String rcstart = jsonParser.parse(result).getAsJsonObject().get("query-continue").getAsJsonObject().get("recentchanges")
					.getAsJsonObject().get("rcstart").getAsString();
			parsedResult.setRcstart(TimestampGenerator.generateTimestampFromWikipediaTimestamp(rcstart));
		}
		return parsedResult;
	}

	public static List<Editor> parseResultToMatchingEditors(String result) {
		List <Editor> editors = new ArrayList <Editor>();
		JsonParser jsonParser = new JsonParser();
		Gson gson = createConfiguredGson();
		JsonArray editorJsonArray = jsonParser.parse(result).getAsJsonObject().get("query").getAsJsonObject().getAsJsonArray("users");
		for (JsonElement jsonElement : editorJsonArray) {
			Editor editor = gson.fromJson(jsonElement, Editor.class);
			editors.add(editor);
		}
		return editors;
	}
}
