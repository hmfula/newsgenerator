package com.coin2012.wikipulse.extraction.smmry;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SentenceFinderTest {
	String samplePageContent;
	
	
	@Before
	public void setup() {
		samplePageContent = "This is sentence one. A journey of a thousand miles always start with one step. Before God all men are created equal.";	
	}


	@Test
	public void editInFirstSentenceAddsSecondSentenceAsBuffer()
			throws Exception {
		
		String sampleWikiEdit = "This is sentence one.";

		String expectedOutput = "This is sentence one. A journey of a thousand miles always start with one step.";
		
		String actualOutput = SentenceFinder.summarize(samplePageContent, sampleWikiEdit);
		
		assertEquals(expectedOutput, actualOutput);

	}
	
	@Test
	public void editInLastSentenceAddsSentenceBeforeAsBuffer()
			throws Exception {
		
		String sampleWikiEdit = "Before God all men are created equal.";

		String expectedOutput = "A journey of a thousand miles always start with one step. Before God all men are created equal.";
		
		String actualOutput = SentenceFinder.summarize(samplePageContent, sampleWikiEdit);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void editInMiddleSentenceAddsSentencesBeforeAndAfterAsBuffer()
			throws Exception {
		
		String sampleWikiEdit = "A journey of a thousand miles always start with one step.";

		String expectedOutput = "This is sentence one. A journey of a thousand miles always start with one step. Before God all men are created equal.";
		
		String actualOutput = SentenceFinder.summarize(samplePageContent, sampleWikiEdit);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void editInLastTwoSentencesAddsSentenceBeforeThemAsBuffer()
			throws Exception {
		
		String sampleWikiEdit = "A journey of a thousand miles always start with one step. Before God all men are created equal.";

		String expectedOutput = "This is sentence one. A journey of a thousand miles always start with one step. Before God all men are created equal.";
		
		String actualOutput = SentenceFinder.summarize(samplePageContent, sampleWikiEdit);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void editInFirsTwoSentencesAddsSentenceAfterThemAsBuffer()
			throws Exception {
		
		String sampleWikiEdit = "This is sentence one. A journey of a thousand miles always start with one step. Before God all men are created equal.";

		String expectedOutput = "This is sentence one. A journey of a thousand miles always start with one step. Before God all men are created equal.";
		
		String actualOutput = SentenceFinder.summarize(samplePageContent, sampleWikiEdit);
		
		assertEquals(expectedOutput, actualOutput);
	}

}