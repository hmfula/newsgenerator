package com.coin2012.wikipulse.extraction.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;
import com.coin2012.wikipulse.extraction.utils.models.Change;

public class HsqldbManager {
	static Logger logger = Logger.getLogger(HsqldbManager.class.getSimpleName());

	public static String getTimestampForLastSavedChange() {
		String timestamp = null;
		Connection connection = null;
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
			logger.warning("Failed to get timestamp of last saved change from database because of : " + e.getCause());
			e.printStackTrace();
		} finally {
			closeDatabaseConnections(connection, prepStatement);
		}
		return timestamp;
	}

	public static void saveChangesToMemDB(List<Change> changes) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("INSERT INTO changes VALUES (?,?,?)");
			for (Change change : changes) {
				try {
					prepStatement.clearParameters();
					prepStatement.setString(1, TimestampGenerator.generateTimestampFromWikipediaTimestamp(change.getTimestamp()));
					prepStatement.setString(2, change.getTitle());
					prepStatement.setString(3, change.getPageid());
					prepStatement.executeUpdate();
				} catch (SQLException e) {
					logger.fine("Ignored double: " + change.getTitle() + " " + change.getTimestamp());
				}
			}
		} catch (SQLException e) {
			logger.warning("Failed to create a Preparedstatement because of  " + e.getCause());
			e.printStackTrace();
		} finally {
			closeDatabaseConnections(connection, prepStatement);
		}
	}

	public static void clearOldChangesFromMemDB(String timestamp) {
		logger.info("Removing all changes from DB older than " + timestamp);
		long timeAsLong = Long.valueOf(timestamp);
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("SELECT * FROM changes for update");
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				if (rs.getLong(1) < timeAsLong) {
					rs.deleteRow();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDatabaseConnections(connection, prepStatement);
		}

	}

	public static HashMap<String, AggregatedChanges> getAllAggregatedChangesFromMemDB() {
		HashMap<String, AggregatedChanges> map = new HashMap<String, AggregatedChanges>();
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = getConnection();
			prepStatement = connection.prepareStatement("SELECT * FROM changes for update");
			ResultSet rs = prepStatement.executeQuery();
			while (rs.next()) {
				String title = rs.getString(2);
				String pageid = rs.getString(3);
				AggregatedChanges change = map.get(rs.getString(2));

				if (change == null) {
					map.put(title, new AggregatedChanges(title, pageid));
				} else {
					change.addToCount();
				}
			}
		} catch (SQLException e) {
			logger.warning("Failed to aggregate all changes from the database because of : " + e.getCause());
			e.printStackTrace();
		} finally {
			closeDatabaseConnections(connection, prepStatement);
		}
		return map;
	}

	private static void closeDatabaseConnections(Connection connection, Statement prepStatement) {
		try {
			if (prepStatement != null && prepStatement.getResultSet() != null) {
				prepStatement.getResultSet().close();
			}
			if (prepStatement != null) {
				prepStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.warning("Failed to close the connection to the database because of : " + e.getCause());
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:mem:wikipulsememdb", "SA", "");
	}
}
