package com.coin2012.wikipulse.identification.newscreation;


	

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.coin2012.wikipulse.conf.WikipulseConstants;
import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.smmry.PageSummarizer;
import com.coin2012.wikipulse.identification.newscreation.classification.WikiEditClassifier;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.PageSummary;
import com.coin2012.wikipulse.models.WikiEdit;

	/**
	 *Generates news based on predefined rules. 
	 *@author mfula
	 *@since 27-01.2013
	 */
	public class SimpleNewsGenerator {
		
		public static List<News> createNewsFromPages(Extractable extractor, List<Page> pages) {
			List<News> newsItemList = new ArrayList<News>();
			WikiEditClassifier wikiEditClassifier = new WikiEditClassifier();
			List<WikiEdit> validEdits = new ArrayList<WikiEdit>();
			List<WikiEdit> initialFilteredEdits = new ArrayList<WikiEdit>();
			for (Page page : pages) {
				
				initialFilteredEdits = wikiEditClassifier.filterEditsBasedOnNewsWorthiness(page);//remove typo edits
				int counter = 0;
//				extractor.enhanceEditsWithContent(validEdits);
				for(WikiEdit currentWikiEdit : initialFilteredEdits){
					for(Category category : page.getCategories()){//check through all available categories
						if(!wikiEditClassifier.isEditorADomainExpert(extractor, category, currentWikiEdit, WikipulseConstants.THRESHOLD_NUMBER_OF_EDITS_FOR_DOMAIN_EXPERT) || 
								!wikiEditClassifier.isEditorANewsContributor(extractor, currentWikiEdit, WikipulseConstants.THRESHOLD_FOR_NEWS_CONTRIBUTOR) ||
										!wikiEditClassifier.isEditorInListOfTopContributors(extractor, currentWikiEdit, WikipulseConstants.THRESHOLD_FOR_TOP_CONTRIBUTOR)){ 
							counter++;
							continue; //break  if editor is not any of the following:  a domain expert,  a news contributor,   a top news contributor 
						}
				
						
						if(counter == initialFilteredEdits.size()){
							return Collections.emptyList();
						}
						
					validEdits.add(currentWikiEdit);
					break;
					
					}
				
				}
					
				//prepare news item
				News newsItem = new News();	
				newsItem.setPageId(page.getPageId());
				newsItem.setPagetTitle(page.getTitle());
				newsItem.setRelYesterday(page.getRelYesterday());
//				Editor editor = new Editor();
				//all edits on the same page get only one news item
				page.setEdits(validEdits);//update with updated list of edits - remove typo edits & edits from unknown users
				WikiEdit samePageEdits = wikiEditClassifier.createCompundWikiEditOfAllSentencesFromEditsOnSamePage(page);
						
				//case 1:   - 1 news item for all edits on same page
				WikiEdit summarizedWikiEdit = wikiEditClassifier.summarizeWikiEditWithBufferSentences(page, samePageEdits);
//				editor.setUserid(summarizedWikiEdit.getUserId());
//				editor.setName(summarizedWikiEdit.getUser());
				newsItem.setNews(summarizedWikiEdit.getContent());
						
				//case 2:		
				//if there are more that 20 sentences in the buffered WikiEdit,  we use the smmry api to summarize to 20 sentences
				//SentenceFinder.getNumberOfSentencesInBufferedWikiEdit()
				if(wikiEditClassifier.findSentencesInAWikiEdit(summarizedWikiEdit).size() > WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES){
				PageSummary summarizedEdit = PageSummarizer.summarizeArticle(page.getPageUrl(), Integer.toString(WikipulseConstants.MAXIMUM_ALLOWED_NUMBER_OF_SENTENCES));
				newsItem.setNews(summarizedEdit.getSm_api_content());
				}
						
				newsItemList.add(newsItem);
						
				}
				
				return newsItemList;	
		}	
		
		
	}
		

