package com.coin2012.wikipulse.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.WikiEdit;

public class TestExperiment {

//	private static final String FRUIT_FLY = "Fruit fly";
//	private static final String FILE_WIKTIONARY_LOGO_EN_SVG = "File:Wiktionary-logo-en.svg";
//	private static final String FILE_DISAMBIG_GRAY_SVG = "File:Disambig gray.svg";
	private Extractable extractor;
	private Page firstArticle;
	private Page secondArticle;
	private WikiEdit wikiedit1;
	private WikiEdit wikiedit2;
	private String author1;
	private String author2;
	private List<WikiEdit> wikieditsForArticle1;
	private List<WikiEdit> wikieditsForArticle2;

	@Before
	public void setUp() throws Exception {
		
		extractor = Mockito.mock(Extractable.class);
		firstArticle = Mockito.mock(Page.class);
		secondArticle = Mockito.mock(Page.class);
		wikiedit1 =  Mockito.mock(WikiEdit.class);
		wikiedit2 =  Mockito.mock(WikiEdit.class);
		author1 = "author1";
		author2 = "author2";
		
		wikieditsForArticle1 = new ArrayList<WikiEdit>();
		wikieditsForArticle2 = new ArrayList<WikiEdit>();
		
	}

	@Test
	public void articlesWithAtleastOneCommonEditorReturnTrue() {
		String author1 = "author1";
		Extractable extractor = new Extractor();
		Page firstArticle = new Page();
		Page secondArticle = new Page();
		WikiEdit wikiedit1 = new WikiEdit();
		WikiEdit wikiedit2 = new WikiEdit();

		List<WikiEdit> wikiedits = new ArrayList<WikiEdit>();
		List<WikiEdit> wikiedits2 = new ArrayList<WikiEdit>();

		wikiedit1.setUser(author1);
		wikiedits.add(wikiedit1);
		firstArticle.setEdits(wikiedits);
		wikiedit2.setUser(author1);
		wikiedits2.add(wikiedit2);
		secondArticle.setEdits(wikiedits2);


		assertTrue("Articles must have at least one common editor",
				extractor.doArticlesHaveAtleastOneCommonEditor(firstArticle,
						secondArticle));
	}


	@Test
	public void articlesWithoutAtleastOneCommonEditorReturnsFalse() {
	

		wikiedit1.setUser(author1);
		wikieditsForArticle1.add(wikiedit1);
		firstArticle.setEdits(wikieditsForArticle1);

		wikiedit2.setUser(author2);
		wikieditsForArticle2.add(wikiedit2);
		secondArticle.setEdits(wikieditsForArticle2);

		assertFalse("Articles must not  have any common editor",
				extractor.doArticlesHaveAtleastOneCommonEditor(firstArticle,
						secondArticle));
	}

//	@Test
//	public void getPageWithImages() {
//		List<String> imageTitles = new ArrayList<String>();
//		Page page = null;
//		String fakeJsonObject = createJsonObject();
//		Gson gson = new Gson();
//		JsonParser jsonParser = new JsonParser();
//		Set<Entry<String, JsonElement>> pageEntryMap = jsonParser
//				.parse(fakeJsonObject).getAsJsonObject().get("query")
//				.getAsJsonObject().get("pages").getAsJsonObject().entrySet();
//		for (Entry<String, JsonElement> entry : pageEntryMap) {
//			// System.out.println("key (page id):" + entry.getKey());
//			// System.out.println("value: " + entry.getValue());
//			page = gson.fromJson(entry.getValue(), Page.class);
//			for (Page.Image image : page.getImages()) {
//				imageTitles.add(image.getTitle());
//			}
//		}
//		assertEquals(2, page.getImages().size());
//		assertEquals(FRUIT_FLY, page.getTitle());
//		assertTrue(imageTitles.contains(FILE_DISAMBIG_GRAY_SVG));
//		assertTrue(imageTitles.contains(FILE_WIKTIONARY_LOGO_EN_SVG));
//	}
//
//	private String createJsonObject() {
//		return new DummyJSONObject().toString();
//	}
//
//	public class DummyJSONObject {
//		@Override
//		public String toString() {
//			StringBuffer buffer = new StringBuffer();
//			return buffer
//					.append("{\"query\": {\"pages\": {\"23839111\": {\"pageid\": 23839111,\"ns\": 0,\"title\": \"Fruit fly\",\"images\": [{	\"ns\": 6,\"title\": \"File:Disambig gray.svg\"	},	{\"ns\": 6,	\"title\": \"File:Wiktionary-logo-en.svg\"}]}}}}")
//					.toString();
//		}
//
//	}
}
