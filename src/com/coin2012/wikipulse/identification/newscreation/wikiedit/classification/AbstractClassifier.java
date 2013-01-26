package com.coin2012.wikipulse.identification.newscreation.wikiedit.classification;

import java.util.ArrayList;
import java.util.List;

import com.coin2012.wikipulse.models.WikiEdit;

public abstract class AbstractClassifier implements Classifiable{

	public boolean isEditNewsWorthy(WikiEdit wikiEdit) {
		return 	wikiEdit.getContent().length() < 10 ? false: true;
	}//not typo fix
	
	/**
	 * Identifies author's capability to generate news
	 * @param wikiEdit
	 * @return true if true if  a given author is a news generator
	 */
	public boolean isEditorANewsCreator(WikiEdit wikiEdit) {
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
	 * Classifies wiki edits based  on domain specific rules. It is based on the template method design pattern. 
	 * All subclasses must implement this method with their own classification logic.
	 * @param wikiEdit
	 * @return 
	 */
	public abstract boolean classify(Object classifiable);//classify according to own algorithm
	
	
	@Override
	 public  boolean  classifyWikiEdit(WikiEdit wikiEdit) {
		
				return classify(wikiEdit);//still under work 
		}
		
	

}
