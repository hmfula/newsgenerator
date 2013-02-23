package com.coin2012.wikipulse.identification.newscreation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.jsoup.Jsoup;

import com.coin2012.wikipulse.extraction.ExtractorImpl;
import com.coin2012.wikipulse.extraction.smmry.SentenceFinder;
import com.coin2012.wikipulse.extraction.smmry.Summarizer;
import com.coin2012.wikipulse.models.Category;
import com.coin2012.wikipulse.models.Editor;
import com.coin2012.wikipulse.models.News;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class NewsCreator {

	Logger logger = Logger.getLogger(NewsCreator.class.toString());

	public List<News> createNews(List<Page> pages) {
		List<News> news = new ArrayList<News>();
		ExtractorImpl extractor = new ExtractorImpl();
		// possible doubles
		List<String> topEditors = new ArrayList<String>();
		// TODO constant
		List<Editor> topContributors = extractor.getTopContributors(50);
		List<Editor> newsContributors = extractor.getNewsContributors(1);
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
				if (topEditors.contains(edit.getUserId())) {
					relevantEdits.add(edit);
					break;
				}
				if (domainExperts.contains(edit.getUserId())) {
					relevantEdits.add(edit);
					break;
				}
				if (edit.getContent().length() > 50) {
					relevantEdits.add(edit);
					break;
				}
			}
			logger.info("For page: " + page.getTitle() + " out of " + page.getEdits().size() + " edits " + relevantEdits.size() + " where relevant.");
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

		try {
			newsText = removeMarkup(newsText);
		} catch (OutOfMemoryError e) {
			logger.warning("Wiki markup removal failed. Newstext is only summarized. "); 
			e.printStackTrace();
		} catch (Exception e) {
			logger.warning("Parsing with threads failed");
			e.printStackTrace();
		}

		String beautifulNews = Summarizer.summerize(newsText);
		pageNews.setNews(beautifulNews);
		pageNews.setPageId(page.getPageId());
		pageNews.setPagetTitle(page.getTitle());
		pageNews.setEditor(editors);

		return pageNews;
	}

	private String removeMarkup(String text) throws Exception {
		// see
		// http://stackoverflow.com/questions/2863272/wikipedia-java-library-to-remove-wikipedia-text-markup-removal
		// and http://wiki.eclipse.org/Mylyn/Incubator/WikiText
		// needs to be done sentence by sentence, or java will crash

		text = text.replace("\\n", "").replace("\"", "");
		Map<Integer, String> sentences = SentenceFinder.findSentences(text);
		String result = "";

		ArrayList<Task> tasks = new ArrayList<Task>();
		int pos = 0;
		for (String sentence : sentences.values()) {
			tasks.add(new Task(pos, sentence));
			pos = pos + 1;
		}
		tasks.trimToSize();
		Map<Integer, String> resultMap = new HashMap<Integer, String>();
		ExecutorService executor = Executors.newFixedThreadPool(4);
		List<Future<Map<Integer, String>>> results = executor.invokeAll(tasks);
		for (Future<Map<Integer, String>> future : results) {
			Map<Integer, String> map;

			map = future.get();
			resultMap.putAll(map);
		}
		for (int i = 0; i < pos; i++) {
			result = result + resultMap.get(i);
		}
		executor.shutdown();

		return result;
	}

}

class Task implements Callable<Map<Integer, String>> {

	private int pos;
	private String text;

	public Task(int pos, String text) {
		this.pos = pos;
		this.text = text;
	}

	public Map<Integer, String> call() throws Exception {
		Logger logger = Logger.getLogger(Task.class.toString());
		Map<Integer, String> map = new HashMap<Integer, String>();
		try {
			text = Jsoup.parse(text).text();

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
				map.put(pos, cleaned.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (OutOfMemoryError e) {
			logger.warning(text + " could not be parse -> Is ignored");
			map.put(pos, "");
			System.gc();
		}
		System.gc();
		return map;
	}
}
