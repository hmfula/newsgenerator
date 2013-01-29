package com.coin2012.wikipulse.models;

import java.util.List;

import com.coin2012.wikipulse.extraction.utils.WikipulseConstants;

public class PageSummary {
	
	
	
	String sm_api_message;
	String sm_api_title;
	String sm_api_content;
	List <String> sm_api_keyword_array;
	public String getSm_api_message() {
		return sm_api_message;
	}
	public void setSm_api_message(String sm_api_message) {
		this.sm_api_message = sm_api_message;
	}
	public String getSm_api_title() {
		return sm_api_title;
	}
	public void setSm_api_title(String sm_api_title) {
		this.sm_api_title = sm_api_title;
	}
	public String getSm_api_content() {
		return sm_api_content.replaceAll("[\\[BREAK\\]]", WikipulseConstants.EMPTY);
	}
	public void setSm_api_content(String sm_api_content) {
		this.sm_api_content = sm_api_content;
	}
	public List<String> getSm_api_keyword_array() {
		return sm_api_keyword_array;
	}
	public void setSm_api_keyword_array(List<String> sm_api_keyword_array) {
		this.sm_api_keyword_array = sm_api_keyword_array;
	}
	
	

}