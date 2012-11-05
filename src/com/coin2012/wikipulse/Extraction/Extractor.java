package com.coin2012.wikipulse.Extraction;

import java.util.List;

import com.coin2012.wikipulse.DomainModel.WikiEdit;


public interface Extractor {

	public List<WikiEdit> getLatestEdits(String category, Integer amount);
}
