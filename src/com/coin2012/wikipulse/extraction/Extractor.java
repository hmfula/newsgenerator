package com.coin2012.wikipulse.extraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.coin2012.wikipulse.extraction.statsgrok.StatsGrokExtractor;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.wikipedia.Change;
import com.coin2012.wikipulse.extraction.wikipedia.WikipediaExtractor;
import com.coin2012.wikipulse.models.Page;
import com.coin2012.wikipulse.models.SnippetPage;

/**
 * Represents an Extractor component used to interact with Wikipedia API.
 * 
 * @author Karsten
 * 
 */
public class Extractor implements Extractable {

	@Override
	public List<Page> getTitlesForCategory(String category) {
		List<Page> pages = WikipediaExtractor.getPagesForCategory(category);
		WikipediaExtractor.updatePagesWithEdits(pages);
		StatsGrokExtractor.updatePagesWithRelevance(pages);
		return pages;
	}

	@Override
	public List<SnippetPage> searchForPagesThatMatch(String searchText) {
		List<SnippetPage> pages = WikipediaExtractor.searchForPagesThatMatch(searchText);
		return pages;

	}

	@Override
	public List<Page> searchForPagesReferencing(String url) {
		List<Page> pages = WikipediaExtractor.searchForPagesReferencing(url);
		return pages;

	}

	@Override
	public List<Page> getPageWithImages(String pageTitle) {
		return WikipediaExtractor.getPageWithImages(pageTitle);
	}

	@Override
	public List<AggregatedChanges> getRecentChanges(int minChanges) {
		String currentTimestamp = TimestampGenerator.generateTimestamp();
		String timestamp = getTimestampForLastSavedChange();
		String queryTimestamp = "";
		if (timestamp != null) {
			Date now = TimestampGenerator.generateDateForTimestamp(currentTimestamp);
			Date lastTimestampDate = TimestampGenerator.generateDateForTimestamp(timestamp);
			long diffInHours = (lastTimestampDate.getTime() - now.getTime()) / (1000 * 60 * 60);
			if (diffInHours < 2) {
				queryTimestamp = timestamp;
			} else {
				queryTimestamp = TimestampGenerator.generateTimestampFromTwoHoursAgo();
			}
		} else {
			queryTimestamp = TimestampGenerator.generateTimestampFromTwoHoursAgo();
		}
		List<Change> changes = WikipediaExtractor.getRecentChanges(currentTimestamp, queryTimestamp);
		this.saveChangesToMemDB(changes);
		this.clearOldChangesFromMemDB();
		List<AggregatedChanges> aggregatedChanges = this.aggregateChangesFromMemDB(minChanges);
		this.sortAggregatedChanges(aggregatedChanges);
		return aggregatedChanges;
		/*
		 * read last entry from db clear db check if within 2h if then call get
		 * recentChanges(lasttimestamp) else call get recentChanges(2h
		 * timestamp) add result to db aggregate return
		 */
	}



	private void clearOldChangesFromMemDB() {
		// TODO ClearDB
	}

	private String getTimestampForLastSavedChange() {
		String timestamp = null;
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement psq = connection.prepareStatement("SELECT * FROM changes");
			ResultSet rs = psq.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1));
				if (rs.isFirst()) {
					timestamp = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timestamp;
	}

	private void saveChangesToMemDB(List<Change> changes) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO changes VALUES (?,?)");
			for (Change change : changes) {
				prepStatement.clearParameters();
				prepStatement.setString(1, TimestampGenerator.generateTimestampFromWikipediaTimestamp(change.getTimestamp()));
				prepStatement.setString(2, change.getTitle());
				prepStatement.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Counts the changes for each title and returns a list of AggregatedChanges
	 * @return list of AggregatedChanges
	 */
	private List<AggregatedChanges> aggregateChangesFromMemDB(int minChanges) {
		HashMap<String, AggregatedChanges> map = new HashMap<String, AggregatedChanges>();
		Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement psq = connection.prepareStatement("SELECT * FROM changes");
			ResultSet rs = psq.executeQuery();
			while (rs.next()) {
				String title = rs.getString(2);
				AggregatedChanges change = map.get(rs.getString(2));
				if (change == null) {
					map.put(title, new AggregatedChanges(title));
				} else {
					change.addToCount();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<AggregatedChanges> resultList = cleanUpAggregatedChanges(map,minChanges);
		return resultList;
	}

	/**
	 * Includes only AggregatedChanges with a count greater than one
	 * @param map
	 * @return
	 */
	private List<AggregatedChanges> cleanUpAggregatedChanges(HashMap<String, AggregatedChanges> map, int minChanges) {
		List<AggregatedChanges> resultList = new ArrayList<AggregatedChanges>();
		for (String key : map.keySet()) {
			AggregatedChanges aggregatedChanges = map.get(key);
			int count = aggregatedChanges.getCount();
			if(count>= minChanges){
				resultList.add(aggregatedChanges);
			}
		}
		return resultList;
	}
	
	/**
	 * Sorts list of AggregatedChanges by counted changes
	 * @param aggregatedChanges
	 */
	private void sortAggregatedChanges(List<AggregatedChanges> aggregatedChanges) {
		Collections.sort(aggregatedChanges, new Comparator<AggregatedChanges>() {
		    public int compare(AggregatedChanges s1, AggregatedChanges s2) {
		        return (new Integer(s1.getCount()).compareTo(new Integer(s2.getCount()))) * -1;
		    }
		});
	}

}
