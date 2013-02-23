package com.coin2012.wikipulse.identification;

import com.coin2012.wikipulse.extraction.Extractor;
import com.coin2012.wikipulse.extraction.ExtractorImpl;

public class Identifier {
	
	private static Extractor extractor = new ExtractorImpl();
	private static Thread identificationThread;

	public static void startIdentificationThread() {
		if (identificationThread == null ) {
			identificationThread = new Thread(new IdentificationRunnable(extractor));
			identificationThread.start();
		}
	}
}
