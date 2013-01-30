package com.coin2012.wikipulse.extraction.smmry;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.coin2012.wikipulse.conf.WikipulseConstants;
import com.coin2012.wikipulse.models.PageSummary;
import com.google.gson.Gson;

public class Summarizer {

	private static final String SMMRY_URL = "http://api.smmry.com/";
	private static final String SM_API_KEY = "792375072138384";

	public static String summerize(String text) {

		ClientResource resource = new ClientResource(SMMRY_URL);
		resource.getReference().addQueryParameter("SM_API_KEY", SM_API_KEY);
		resource.getReference().addQueryParameter("SM_LENGTH", "10");
		resource.getReference().addQueryParameter("SM_KEYWORD_COUNT", "5");
		resource.getReference().addQueryParameter("SM_LENGTH", "10");
		resource.getReference().addSegment("&amp;SM_WITH_BREAK");

		Form textForm = new Form();
		textForm.set("sm_api_input", text);
		Representation response = resource.post(textForm);

		String content = "ERROR";
		Gson gson = new Gson();
		try {
			String result = response.getText();
			PageSummary summary = gson.fromJson(result, PageSummary.class);
			content = summary.getSm_api_content();
			if (content == null) {
				content = text;
			}else{
				content = content.replaceAll("[\\[BREAK\\]]", WikipulseConstants.EMPTY);
			}
		} catch (IOException e) {
			// TODO;
			e.printStackTrace();
		}

		return content;
	};
}
