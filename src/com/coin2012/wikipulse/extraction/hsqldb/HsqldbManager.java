package com.coin2012.wikipulse.extraction.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.AggregatedChanges;
import com.coin2012.wikipulse.extraction.News_Counter;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;

public class HsqldbManager {
	static Logger logger = Logger.getLogger("HsqldbManager.class");
	
	public static String getTimestampForLastSavedChange() {
		String timestamp = null;
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement psq = connection.prepareStatement("SELECT * FROM changes");
			ResultSet rs = psq.executeQuery();
			while (rs.next()) {
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

	public static void saveChangesToMemDB(List<Change> changes) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO changes VALUES (?,?)");
			for (Change change : changes) {
				try {
					prepStatement.clearParameters();
					prepStatement.setString(1, TimestampGenerator.generateTimestampFromWikipediaTimestamp(change.getTimestamp()));
					prepStatement.setString(2, change.getTitle());
					prepStatement.executeUpdate();
				} catch (SQLException e) {
					logger.info("Ignored double: " + change.getTitle() + " " + change.getTimestamp());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void clearOldChangesFromMemDB(String timestamp) {
		logger.info("Removing all changes from DB older than " + timestamp);
		long timeAsLong = Long.valueOf(timestamp);
		ResultSet rs = getAllChangesFromMemDB();
		try {
			while (rs.next()) {
				if(rs.getLong(1) < timeAsLong){
					rs.deleteRow();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static ResultSet getAllChangesFromMemDB() {
		ResultSet rs = null;
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			PreparedStatement psq = connection.prepareStatement("SELECT * FROM changes for update");
			rs = psq.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static HashMap<String, AggregatedChanges> getAllAggregatedChangesFromMemDB() {
		HashMap<String, AggregatedChanges> map = new HashMap<String, AggregatedChanges>();
		ResultSet rs = getAllChangesFromMemDB();
		try {
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
		return map;
	}
	
	public static void saveUserInteractionInDB(String News){
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			ResultSet rs = null;
			try {
				PreparedStatement psq = connection.prepareStatement("SELECT * FROM mostreadnews WHERE article=?");
				psq.clearParameters();
				psq.setString(1,News);
				rs = psq.executeQuery();
				int counter = 1;				
				if (rs != null && rs.next()){
					counter = rs.getInt(2) + 1;
					psq.close();
					psq = connection.prepareStatement("UPDATE mostreadnews SET numberofclicks=? WHERE article=? ");
					psq.setString(1,Integer.toString(counter));
					psq.setString(2,News);
				}		
				else {
					psq.close();
					psq = connection.prepareStatement("INSERT INTO mostreadnews VALUES(?,?)");
					psq.clearParameters();
					psq.setString(1,News);
					psq.setString(2,Integer.toString(counter));					
				}
				psq.executeUpdate();					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<News_Counter> getMostReadNews(){
		List<News_Counter> resultList = new ArrayList<News_Counter>();
		try {
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
			ResultSet rs = null;
			PreparedStatement psq = connection.prepareStatement("SELECT TOP 10 * FROM mostreadnews ORDER BY numberofclicks DESC");
			rs = psq.executeQuery();		
			while (rs.next()) {
				News_Counter news = new News_Counter(rs.getString(1), rs.getInt(2));
				resultList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
}
