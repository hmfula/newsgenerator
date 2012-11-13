package com.coin2012.wikipulse.extraction;

import java.util.List;

import com.coin2012.wikipulse.models.Page;

public interface Extractable {

	/**
	 * Returns a list of titles (articles, categories, etc.) with 5 edits each without content
	 * @param category
	 * @return
	 */
	public abstract List<Page> getTitlesForCategory(String category);
}