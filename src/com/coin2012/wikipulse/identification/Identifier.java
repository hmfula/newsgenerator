package com.coin2012.wikipulse.identification;

import com.coin2012.wikipulse.extraction.Extractable;
import com.coin2012.wikipulse.extraction.Extractor;

public class Identifier {
	
	private static Extractable extractor = new Extractor();
	private static Thread identificationThread;

	public static void startIdentificationThread() {
		if (identificationThread == null ) {
			identificationThread = new Thread(new IdentificationRunnable(extractor));
			identificationThread.start();
		}
	}
}
