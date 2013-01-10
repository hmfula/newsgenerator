package com.coin2012.wikipulse.extraction.wikipedia.polling;

import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.hsqldb.HsqldbManager;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;

public class RecentChangesRunnable implements Runnable {
	Logger logger = Logger.getLogger(RecentChangesRunnable.class.getSimpleName());

	@Override
	public void run() {
		while (true) {
			logger.info("Starting querying recent changes");
			List<Change> changes = WikipediaExtractor.getRecentChangesWithinTwoHours();
			if(changes != null && !changes.isEmpty() ){
				HsqldbManager.saveChangesToMemDB(changes);
				HsqldbManager.clearOldChangesFromMemDB(TimestampGenerator.generateTimestampFromTwoHoursAgo());
				logger.info("Saved all changes to the database");
			}
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
