package com.coin2012.wikipulse.extraction;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.models.Title;
import com.coin2012.wikipulse.models.WikiEdit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Extractor implements Extractable {

	/* (non-Javadoc)
	 * @see com.coin2012.wikipulse.extraction.Extractable#getEditsForCategory(java.lang.String)
	 */
	@Override
	public List<Title> getTitlesWithEditsForCategory(String category) {
		List<Title> titles = getTitlesForCategory(category);
		for (Title title : titles) {
			title.setEdits(this.getEditsForPageId(title.getPageid()));
		}

		return titles;
	}
	
	@Override
	public List<Title> getTitlesInCategory(String category) {
		List<Title> titles = getTitlesForCategory(category);
		return titles;
	}

	@Override
	public String getEditsForCategory(String category, int amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Title> getRelevantTitlesForCategory(String category) {
		List<Title> titles = getTitlesForCategory(category);
		return titles;
	}
	
	@Override
	public float getRelOfTitleForYesterday(String pageTitle){
		pageTitle = pageTitle.replaceAll(" ", "%20");
		String url = "http://stats.grok.se/json/en/latest30/"+pageTitle;
		ClientResource resource= new ClientResource(url);
		//System.out.println(url);
		String result = executeQueryToResource(resource);
		JsonParser jsonParser = new JsonParser();
		JsonObject dailyViews = jsonParser.parse(result).getAsJsonObject()
				.get("daily_views").getAsJsonObject();				
		String str_DailyViews = dailyViews.toString().substring(1,dailyViews.toString().length()-1);
		str_DailyViews = str_DailyViews.trim();
		str_DailyViews = str_DailyViews.replaceAll("\"", "");
		System.out.println(pageTitle);
		if (str_DailyViews == "") {
			return 0;
		}
		
		String tempArr[] = str_DailyViews.split(",");
		String viewsPerDay[][] = new String[tempArr.length][2];
		for (int i = 0; i < tempArr.length; i++) {
			viewsPerDay[i] = tempArr[i].split(":");
		}
		float total_views, yesterday_views ;
		total_views = 0;
		yesterday_views = Integer.parseInt(viewsPerDay[0][1]);
		
		for (int i = 0; i < viewsPerDay.length; i++) {
			total_views += Integer.parseInt(viewsPerDay[i][1]);
		}
		if (total_views == 0) {
			return 0;
		}
		float relevance_result = yesterday_views/total_views;
		System.out.println(relevance_result);
		return relevance_result;
	}
	

	private List<Title> getTitlesForCategory(String category) {
		// TODO include pageination
		ClientResource resource = buildQueryForCategoryMembers(category);
		String result = executeQueryToResource(resource);
		List<Title> titles = parseResultToTitles(result);

		return titles;
	}

	private List<WikiEdit> getEditsForPageId(String pageId) {
		// Todo pageinatation possible?
		ClientResource resource = buildQueryForRevisions(pageId);
		String result = executeQueryToResource(resource);
		List<WikiEdit> edits = parseResultToEdits(result, pageId);
		return edits;
	}

	private List<WikiEdit> parseResultToEdits(String result, String pageId) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject().get("pages").getAsJsonObject()
				.get(pageId).getAsJsonObject().getAsJsonArray("revisions");

		Gson gson = this.createConfiguredGson();
		List<WikiEdit> edits = new LinkedList<WikiEdit>();
		for (JsonElement jsonElement : categorymembers) {
			WikiEdit edit = gson.fromJson(jsonElement, WikiEdit.class);
			//edit.setContent(jsonElement.getAsJsonObject().get("*").getAsString());
			edits.add(edit);
		}

		return edits;
	}

	private ClientResource buildQueryForRevisions(String pageId) {
		ClientResource resource = new ClientResource(
				"http://en.wikipedia.org/w/api.php");
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("pageids", pageId);
		resource.getReference().addQueryParameter("prop", "revisions");
		//resource.getReference().addQueryParameter("rvprop", "content|ids|timestamp|flags|comment|user");
		resource.getReference().addQueryParameter("rvprop", "ids|timestamp|flags|comment|user");
		resource.getReference().addQueryParameter("rvlimit", "1");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	private List<Title> parseResultToTitles(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("categorymembers");

		Gson gson = this.createConfiguredGson();
		List<Title> titles = new LinkedList<Title>();
		for (JsonElement jsonElement : categorymembers) {
			Title title = gson.fromJson(jsonElement, Title.class);
			titles.add(title);
		}

		return titles;
	}

	private String executeQueryToResource(ClientResource resource) {
		Representation response = resource.get();

		String result = "error";
		try {
			result = response.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private ClientResource buildQueryForCategoryMembers(String category) {
		ClientResource resource = new ClientResource(
				"http://en.wikipedia.org/w/api.php");
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "categorymembers");
		resource.getReference().addQueryParameter("cmtitle", category);
		resource.getReference().addQueryParameter("cmsort", "timestamp");
		resource.getReference().addQueryParameter("cmdir", "desc");
		resource.getReference().addQueryParameter("cmprop",
				"ids|title|timestamp");
		resource.getReference().addQueryParameter("cmlimit", "max");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	private Gson createConfiguredGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
