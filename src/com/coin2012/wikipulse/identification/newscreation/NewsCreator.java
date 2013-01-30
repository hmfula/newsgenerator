package com.coin2012.wikipulse.identification.newscreation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class NewsCreator {
	
	Logger logger = Logger.getLogger(NewsCreator.class.toString());

	public List<News> createNews (List<Page> pages){
		List<News> news = new ArrayList<News>();
		Extractor extractor = new Extractor();
		//possible doubles
		List<String> topEditors = new ArrayList<String>();
		//TODO constant
		List<Editor> topContributors = extractor.getTopContributors(50);
		List<Editor> newsContributors =extractor.getNewsContributors(1);
		for (Editor editor : newsContributors) {
			topEditors.add(editor.getUserid());
		}
		for (Editor editor : topContributors) {
			topEditors.add(editor.getUserid());
		}
		
		for (Page page : pages) {
			List<String> domainExperts = new ArrayList<String>();
			List<WikiEdit> relevantEdits = new ArrayList<WikiEdit>();
			for (Category category : page.getCategories()) {
				// TODO constant
				List<Editor> domainExpertsForACategory = extractor.getDomainExperts(category, 5);
				for (Editor editor : domainExpertsForACategory) {
					domainExperts.add(editor.getUserid());
				}
			}
			
			for (WikiEdit edit : page.getEdits()) {
				if(topEditors.contains(edit.getUserId())){
					relevantEdits.add(edit);
					break;
				}
				if(domainExperts.contains(edit.getUserId())){
					relevantEdits.add(edit);
					break;
				}
				if (edit.getContent().length()>50) {
					relevantEdits.add(edit);
					break;
				}
			}
			logger.info("For page: " + page.getTitle()+ " out of " + page.getEdits().size() + " edits " + relevantEdits.size() + " where relevant." );
			News pageNews = createPageNews(page, relevantEdits);
			news.add(pageNews);
		}
		
		return news;
	}

	private News createPageNews(Page page, List<WikiEdit> relevantEdits) {
		News pageNews = new News();
		List<Editor> editors = new ArrayList<Editor>();
		String newsText = "";
		
		for (WikiEdit wikiEdit : relevantEdits) {
			newsText = newsText + wikiEdit.getContent();
			Editor editor = new Editor(wikiEdit.getUserId(), wikiEdit.getUser());
			editors.add(editor);
		}
		StringBuffer betterNewsText = new StringBuffer();;
		Map<Integer, String> sentences = SentenceFinder.findSentences(newsText);
		for (String sentence : sentences.values()) {
			betterNewsText.append(sentence + " ");
		}
		newsText =  betterNewsText.toString().trim();
		
		pageNews.setNews(removeMarkup(newsText));
		pageNews.setPageId(page.getPageId());
		pageNews.setPagetTitle(page.getTitle());
		pageNews.setEditor(editors);
		
		return pageNews;
	}

	private String removeMarkup(String text) {
		
		StringWriter writer = new StringWriter();

        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
        builder.setEmitAsDocument(false);

        MarkupParser parser = new MarkupParser(new MediaWikiLanguage());
        
        parser.setBuilder(builder);
        parser.parse(text);

        final String html = writer.toString();
        final StringBuilder cleaned = new StringBuilder();

        HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
                public void handleText(char[] data, int pos) {
                    cleaned.append(new String(data)).append(' ');
                }
        };
        
        try {
			new ParserDelegator().parse(new StringReader(html), callback, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (cleaned.length() == 0) {
        	return text;
        } else {
        	return cleaned.toString();
        }
	}
}
