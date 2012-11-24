package com.coin2012.wikipulse.identification;




public interface Identifiable {
	
	public String getNewsForCategory(String category);
	
	public String getMostReadTitlesForCategory(String category);
	
	public String  searchForPagesThatMatch(String searchText);

	public String  searchForPagesReferencing(String url);
	
	public String getImageDetailsRelatedTo(String subjectTitle);
}
