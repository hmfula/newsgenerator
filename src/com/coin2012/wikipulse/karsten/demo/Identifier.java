package com.coin2012.wikipulse.karsten.demo;


public class Identifier {

	public String getNewsForCategory(String category) {
		Extractor extractor = new Extractor();
		String list = extractor.getEditsForCategory(category);
		return list;
	}

}
