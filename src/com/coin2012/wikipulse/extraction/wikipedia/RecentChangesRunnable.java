package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;

public class RecentChangesRunnable implements Runnable {
	Logger logger = Logger.getLogger("RecentChangesRunnable.class");

	@Override
	public void run() {
		while (true) {
			logger.info("Starting querying recent changes");
			List<Change> changes = WikipediaExtractor.getRecentChangesWithinTwoHours();
			HsqldbManager.saveChangesToMemDB(changes);
			HsqldbManager.clearOldChangesFromMemDB(TimestampGenerator.generateTimestampFromTwoHoursAgo());
			logger.info("Querying done");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
