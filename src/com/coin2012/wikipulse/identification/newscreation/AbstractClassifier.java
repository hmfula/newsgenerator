package com.coin2012.wikipulse.identification.newscreation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public abstract class AbstractClassifier implements Classifiable{

	boolean isEditNewsWorthy(WikiEdit wikiEdit) {
		return 	wikiEdit.getContent().length() < 10 ? false: true;
	}//not typo fix
	
	/**
	 * Identifies author's capability to generate news
	 * @param wikiEdit
	 * @return true if true if  a given author is a news generator
	 */
	boolean isEditorANewsCreator(WikiEdit wikiEdit) {
		List <String> editorNames = new ArrayList<String>();//TODO get domain experts/authors from DB
		editorNames.add("Woohookitty");
		editorNames.add("Bearcat");
		editorNames.add("Dr. Blofeld");
		editorNames.add("Tassedethe");
		editorNames.add("Rjwilmsi");
		editorNames.add("Koavf");
		editorNames.add("Rich Farmbrough");
		editorNames.add("Waacstats");
		editorNames.add("Hmains");
		editorNames.add("Ser Amantio di Nicolao");
		return editorNames.contains(wikiEdit.getUser());//can use id also
	}//compare from domain experts list
	
	
	/**
	 * Identifies page relevance based on how many views it received from the previous 30 days
	 * @param page
	 * @return  true if the edited page's relevance is above a  defined threshold otherwise returns false
	 */
	boolean isEditedPageRelevant(Page page){
		ArrayList<Page> pages = new ArrayList<Page>();
		pages.add(page);
		StatsGrokExtractor.updatePagesWithRelevance(pages);	
		return page.getRelYesterday() > 0F ? true : false;
		
	}//check page ranking
	
	
	/**
	 * Classifies wiki edits based  on domain specific rules. It is based on the template method design pattern. 
	 * All subclasses must implement this method with their own classification logic.
	 * @param wikiEdit
	 * @return 
	 */
	public abstract boolean classifyWikiEdit(WikiEdit wikiEdit);//classify according to own algorithm
	
	
	@Override
	 public  boolean  classify(Object classifiable) {
		Page page = null; 
		if(classifiable instanceof Page){
			 page = (Page)classifiable;
		}
		else{
			throw new RuntimeException("Unrecogined object input into the classifier's classfify method.");
		}
		
		if(page != null){
			for(Iterator <WikiEdit> iterator =  page.getEdits().iterator();;){
				boolean wikiEditIsPostivelyClassified = classifyWikiEdit(iterator.next());	
				iterator.remove();
				return wikiEditIsPostivelyClassified && isEditedPageRelevant(page) ? true : false;//still under work 
			}
		}
		return false;//under work
		
	}
	

}
