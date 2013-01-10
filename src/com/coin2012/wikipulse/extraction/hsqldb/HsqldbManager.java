package com.coin2012.wikipulse.extraction.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.AggregatedChanges;
import com.coin2012.wikipulse.extraction.News_Counter;
import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;

public class HsqldbManager {
	static Logger logger = Logger.getLogger(HsqldbManager.class.getSimpleName());
	private  static Connection connection;
	
	
	public static String getTimestampForLastSavedChange() {
		String timestamp = null;
		PreparedStatement prepStatement = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("SELECT * FROM changes");
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				if (rs.isFirst()) {
					timestamp = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cleanup(connection, prepStatement);	
		}
		return timestamp;
	}
	
	public static void saveChangesToMemDB(List<Change> changes) {
		PreparedStatement prepStatement = null;
		try {
		 connection = getConnection();
			 prepStatement = connection.prepareStatement("INSERT INTO changes VALUES (?,?)");
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
			logger.info("Failed to create  a Preparedstatement because of  " + e.getCause());
			e.printStackTrace();
		}finally{
			cleanup(connection, prepStatement);	
		}
	}

	private static void cleanup(Connection connection, Statement prepStatement) {
		try {
		if(prepStatement != null){
				prepStatement.close();
			} 
		if(connection != null){
			connection.close();
		} 
		}catch (SQLException e) {
			logger.info("Failed to close the connection to the database because of : " + e.getCause() );
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		if(connection != null){
			return connection;
		} 
		return DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
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
		}finally{
			try {
				cleanup(connection, rs.getStatement());
			} catch (SQLException e) {
				logger.info("Failed to close the connection to the database because of : " + e.getCause() );
				e.printStackTrace();
			}	
		}
		
	}

	public static ResultSet getAllChangesFromMemDB() {
		ResultSet rs = null;
		PreparedStatement prepStatement = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("SELECT * FROM changes for update");
			rs = prepStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cleanup(connection, prepStatement);
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
		}finally{
			try {
				cleanup(connection, rs.getStatement());
			} catch (SQLException e) {
				logger.info("Failed to close the connection to the database because of : " + e.getCause() );
				e.printStackTrace();
			}	
		}
		return map;
	}
	
	public static void saveUserInteractionInDB(String News){
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		try {
				connection = getConnection();
				prepStatement = connection.prepareStatement("SELECT * FROM mostreadnews WHERE article=?");
				prepStatement.clearParameters();
				prepStatement.setString(1,News);
				rs = prepStatement.executeQuery();
				int counter = 1;				
				if (rs != null && rs.next()){
					counter = rs.getInt(2) + 1;
					prepStatement.close();
					prepStatement = connection.prepareStatement("UPDATE mostreadnews SET numberofclicks=? WHERE article=? ");
					prepStatement.setString(1,Integer.toString(counter));
					prepStatement.setString(2,News);
				}		
				else {
					prepStatement.close();
					prepStatement = connection.prepareStatement("INSERT INTO mostreadnews VALUES(?,?)");
					prepStatement.clearParameters();
					prepStatement.setString(1,News);
					prepStatement.setString(2,Integer.toString(counter));					
				}
				prepStatement.executeUpdate();					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cleanup(connection, prepStatement);
		}
	}
	
	public static List<News_Counter> getMostReadNews(){
		List<News_Counter> resultList = new ArrayList<News_Counter>();
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("SELECT TOP 10 * FROM mostreadnews ORDER BY numberofclicks DESC");
			rs = prepStatement.executeQuery();		
			while (rs.next()) {
				News_Counter news = new News_Counter(rs.getString(1), rs.getInt(2));
				resultList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			cleanup(connection, prepStatement);
		}
		return resultList;
	}
}
