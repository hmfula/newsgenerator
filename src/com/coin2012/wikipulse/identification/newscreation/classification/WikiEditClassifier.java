package com.coin2012.wikipulse.identification.newscreation.classification;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
import com.coin2012.wikipulse.extraction.utils.WikipulseConstants;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class WikiEditClassifier  extends  AbstractClassifier{


	@Override
	public boolean classify(Object classifiable) {
		WikiEdit wikiEdit;
		if(classifiable != null && classifiable instanceof WikiEdit){
			wikiEdit = (WikiEdit)classifiable;
		}
		else{
			throw new RuntimeException("Unrecogined object input into the classifier's classfify method.");
		}
		return isEditNewsWorthy(wikiEdit);
	}

	public List<WikiEdit> filterEditsBasedOnNewsWorthiness(Page page) {
		List<WikiEdit> listOfWikiEdits = new ArrayList<WikiEdit>();
		for(WikiEdit wikiEdit : page.getEdits()){
			if(isEditNewsWorthy(wikiEdit) ){
				
				listOfWikiEdits.add(wikiEdit);	
			}
		}
		
		return listOfWikiEdits;
	}

	public boolean areEditsBasedOnTheSamePage(WikiEdit wikiEdit1,
			WikiEdit wikiEdit2) {
		return wikiEdit1.getParentid() != null && wikiEdit2.getParentid()  != null ? wikiEdit1.getParentid().equals(wikiEdit2.getParentid()): false;
	}

	public List<String> findSentencesInAWikiEdit(WikiEdit wikiEdit1) {
		List<String> sentenceList = new ArrayList<String>();
		Map<Integer, String> indexSentenceMap = new HashMap<Integer, String>();
		indexSentenceMap =	SentenceFinder.findSentences(wikiEdit1.getContent());
		for (String sentence : indexSentenceMap.values()) {
			sentenceList.add(sentence + WikipulseConstants.SPACE);
		}
		return sentenceList;
	}

	public List<String> findCommonSentencesInWikiEdits(WikiEdit wikiEdit1,
			WikiEdit wikiEdit2) {
		List<String> sentenceListOfWikiEdit1 = new ArrayList<String>();
		List<String> sentenceListOfWikiEdit2 = new ArrayList<String>();
		sentenceListOfWikiEdit1 = findSentencesInAWikiEdit(wikiEdit1);
		sentenceListOfWikiEdit2 = findSentencesInAWikiEdit(wikiEdit2);
		if(wikiEdit1.getParentid().equals(wikiEdit2.getParentid())){//if the edited page is the same
			
			sentenceListOfWikiEdit1.retainAll(sentenceListOfWikiEdit2);
			return sentenceListOfWikiEdit1;
		}
		
		return Collections.emptyList();
	}

	
	public WikiEdit removeTextFromAWikiEdit(WikiEdit wikiEdit1, String textToBeRemoved) {
		wikiEdit1.setContent(wikiEdit1.getContent().replace(textToBeRemoved, WikipulseConstants.SPACE));

		return wikiEdit1;
	}

	public WikiEdit summarizeWikiEditWithBufferSentences(Page page, WikiEdit wikiEdit1) {
		wikiEdit1.setContent(SentenceFinder.summarize(page.getTextContent(), wikiEdit1.getContent()));
		return wikiEdit1;
	}

	public List<String> processDuplicateSentencesFromWikiEditsOnTheSamePage(
			WikiEdit wikiEdit1, WikiEdit wikiEdit2) {
		Set<String> nonOverlappingWikiEditSentencesFromSamePageSet = new HashSet<String>();
		List<String> nonOverlappingWikiEditSentencesFromSamePageList = new ArrayList<String>();
		List<String> sentenceListOfWikiEdit1 = new ArrayList<String>();
		List<String> sentenceListOfWikiEdit2 = new ArrayList<String>();
		sentenceListOfWikiEdit1 = findSentencesInAWikiEdit(wikiEdit1);
		sentenceListOfWikiEdit2 = findSentencesInAWikiEdit(wikiEdit2);
		if(wikiEdit1.getParentid().equals(wikiEdit2.getParentid())){//if the edited page is the same
			
			nonOverlappingWikiEditSentencesFromSamePageSet.addAll(sentenceListOfWikiEdit1);
			nonOverlappingWikiEditSentencesFromSamePageSet.addAll(sentenceListOfWikiEdit2);
					
			nonOverlappingWikiEditSentencesFromSamePageList.addAll(nonOverlappingWikiEditSentencesFromSamePageSet);
			return nonOverlappingWikiEditSentencesFromSamePageList;
		}
		
		return Collections.emptyList();
	}

	public String getNonOverlappingSummarySentencesForSamePageWikiEdits(
			Page page, WikiEdit wikiEdit1, WikiEdit wikiEdit2) {
		StringBuffer editcontent = new StringBuffer();
		WikiEdit tempWikiEdit =  new WikiEdit();
		List<Integer> indexCollection = new ArrayList<Integer>();
		List<String> sentencesFromPage = new ArrayList<String>();
		List<String> nonOverlappingWikiEditSentencesFromSamePageList = new ArrayList<String>();
		tempWikiEdit.setContent(page.getTextContent());
		sentencesFromPage = findSentencesInAWikiEdit(tempWikiEdit);
		nonOverlappingWikiEditSentencesFromSamePageList = processDuplicateSentencesFromWikiEditsOnTheSamePage(wikiEdit1,wikiEdit2);
		for(Integer i = 0; i < sentencesFromPage.size() ; i++){
			String senFromPage = sentencesFromPage.get(i);
			for(String	sentenceFromEdit : nonOverlappingWikiEditSentencesFromSamePageList){
				
			if(sentenceFromEdit.equals(senFromPage)){
				indexCollection.add(i);	
				break;
			}
			
		}
			
		}
		if(!indexCollection.isEmpty()){
			
			int min = Collections.min(indexCollection);
			int max = Collections.max(indexCollection);
			
			for(Integer index = min; index <= max ; index++){
				
				editcontent.append(sentencesFromPage.get(index) + WikipulseConstants.SPACE);
			}
		}
		return editcontent.toString().trim();
	}

	public WikiEdit createCompundWikiEditOfAllSentencesFromEditsOnSamePage(Page page) {
		Set<String> sentenceSetOfWikiEdits = new HashSet<String>();
		WikiEdit tempWikiEdit =  new WikiEdit();
		tempWikiEdit.setParentid(page.getPageId());
		for(WikiEdit wikiEdit : page.getEdits()){
			
			
			if(page.getPageId() != null && wikiEdit.getParentid()!= null 
					&& wikiEdit.getParentid().equals(page.getPageId()) //ids should all be same for a edits on a given page
					){
				sentenceSetOfWikiEdits.addAll(findSentencesInAWikiEdit(wikiEdit));
			}
		}
		StringBuffer editcontent = new StringBuffer();
		for(String sentence : sentenceSetOfWikiEdits){
			
			editcontent.append(sentence + WikipulseConstants.SPACE);
		}
		
		tempWikiEdit.setContent(editcontent.toString().trim());
		return tempWikiEdit;
	}
}
