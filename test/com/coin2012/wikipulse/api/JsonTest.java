package com.coin2012.wikipulse.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.coin2012.wikipulse.models.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonTest {

	
	private static final String FRUIT_FLY = "Fruit fly";
	private static final String FILE_WIKTIONARY_LOGO_EN_SVG = "File:Wiktionary-logo-en.svg";
	private static final String FILE_DISAMBIG_GRAY_SVG = "File:Disambig gray.svg";

	@Test 
	public void getPageWithImages()  {
		List<String> imageTitles = new ArrayList<String>();
			  Page page = null;
			  String fakeJsonObject = createJsonObject();
			  Gson gson = new Gson();
			  JsonParser jsonParser = new JsonParser();
				Set<Entry<String, JsonElement>> pageEntryMap = jsonParser.parse(fakeJsonObject).getAsJsonObject().get("query").getAsJsonObject().get("pages").getAsJsonObject().entrySet();
				for (Entry<String, JsonElement> entry : pageEntryMap) {
//					System.out.println("key (page id):" + entry.getKey());
//					System.out.println("value: " + entry.getValue());
					page = gson.fromJson(entry.getValue(), Page.class);
					for (Page.Image image : page.getImages()) {
						imageTitles.add(image.getTitle());
					}
				}
				assertEquals(2, page.getImages().size());
				assertEquals(FRUIT_FLY,page.getTitle());
				assertTrue(imageTitles.contains(FILE_DISAMBIG_GRAY_SVG));
				assertTrue(imageTitles.contains(FILE_WIKTIONARY_LOGO_EN_SVG));
	}

	private String createJsonObject() {
		return new DummyJSONObject().toString();
	}
	
	public class DummyJSONObject {
		@Override
		public String toString() {
			 StringBuffer buffer = new StringBuffer();
		   return buffer.append("{\"query\": {\"pages\": {\"23839111\": {\"pageid\": 23839111,\"ns\": 0,\"title\": \"Fruit fly\",\"images\": [{	\"ns\": 6,\"title\": \"File:Disambig gray.svg\"	},	{\"ns\": 6,	\"title\": \"File:Wiktionary-logo-en.svg\"}]}}}}").toString();
		}
	 
	}
}
