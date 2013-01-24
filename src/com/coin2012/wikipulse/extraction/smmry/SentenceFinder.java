package com.coin2012.wikipulse.extraction.smmry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceFinder {
/**
 * Adds buffer sentence(s) to  given text (edit) by matching edited sentences  between the original page text and the edit text and adding buffer sentences to the edited text as buffer sentences accordingly.
 * If the edit is only made to the first sentence of the page content then the second sentence is added to the edited text as buffer. If a sentence between two sentences is edited then two buffer sentences 
 * are added, one sentence before the edit and another sentence after the edit.
 * If the last sentence is the only edit then the sentence before it is added as buffer. 
 * Note: All buffer sentences are calculated from the page text i.e the source page that contain text that was actually edited.
 * @param pageContent the text that represents the page content
 * @param wikiEditContent text that represents the edited part of the page content.
 */
	public static void summarize(final String pageContent, final String wikiEditContent) {
		Map <Integer, String> editIndexSentenceMap = new HashMap <Integer, String>();
		Map <Integer, String> pageIndexSentenceMap = new HashMap <Integer, String>();
		List <String> sentenceList = new ArrayList<String>();
		StringBuffer editcontent = new StringBuffer();
		
		 pageIndexSentenceMap =  findSentences(pageContent);
		 editIndexSentenceMap =  findSentences(wikiEditContent);
		 
		 if(!editIndexSentenceMap.isEmpty()  && !pageIndexSentenceMap.isEmpty() && editIndexSentenceMap.size() >= 1){
				
				sentenceList.addAll(editIndexSentenceMap.values());
				String edit = sentenceList.get(0);
				if(edit != null){
					for(Entry<Integer, String> entry: pageIndexSentenceMap.entrySet()){
						if(entry.getValue().equals(edit)){
							Integer key = entry.getKey();
							sentenceList.add(0, pageIndexSentenceMap.get(key-1)!= null ? pageIndexSentenceMap.get(key-1) : "");
							break;
						}
					}
					
					 edit = sentenceList.get(sentenceList.size()-1);
					if(edit != null){
						for(Entry<Integer, String> entry: pageIndexSentenceMap.entrySet()){
							if(entry.getValue().equals(edit)){
								Integer key = entry.getKey();
								sentenceList.add(pageIndexSentenceMap.get(key+1) != null ? pageIndexSentenceMap.get(key+1) : "");
									break;
							}
						}
					
					}
					}
				}
				for(String sentence :sentenceList){
					editcontent.append(sentence) ;	
					editcontent.append("\n") ;	
				}
					
	}

	/**
	 * Finds sentences from  given text.
	 * @param textToProcess input containing sentence (s).
	 * @return a map of indices  ask keys and sentences as values
	 */
	public  static Map <Integer, String> findSentences(String textToProcess) {
		List <String> sentenceList = new ArrayList<String>();
		Map <Integer, String > IndexSentenceMap = new HashMap<Integer, String>();
        Pattern re = Pattern.compile(
            "# Match a sentence ending in punctuation or EOS.\n" +
            "[^.!?\\s]    # First char is non-punct, non-ws\n" +
            "[^.!?]*      # Greedily consume up to punctuation.\n" +
            "(?:          # Group for unrolling the loop.\n" +
            "  [.!?]      # (special) inner punctuation ok if\n" +
            "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n" +
            "  [^.!?]*    # Greedily consume up to punctuation.\n" +
            ")*           # Zero or more (special normal*)\n" +
            "[.!?]?       # Optional ending punctuation.\n" +
            "['\"]?       # Optional closing quote.\n" +
            "(?=\\s|$)", 
            Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(textToProcess);
       Integer count = 0;
        while (reMatcher.find()) {
            sentenceList.add(reMatcher.group());
        } 
        for (String sentence: sentenceList) {
            IndexSentenceMap.put(count++, sentence);
        }
        return IndexSentenceMap;
	}
	
	

}