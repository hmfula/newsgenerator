package com.coin2012.wikipulse.extraction.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class TimestampGenerator {
	
	public static String generateTimestampForToday(){
		final String DATE_PATTERN = "yyyyMMdd000000";
		return generateTimestampForDatePattern(DATE_PATTERN, new Date());
	}
	
	public static String generateTimestamp(){
		final String DATE_PATTERN = "yyyyMMddHHmmss";
		return generateTimestampForDatePattern(DATE_PATTERN, new Date());
	}
	
	public static String generateTimestampFromTwoHoursAgo(){
		final String DATE_PATTERN = "yyyyMMddHHmmss";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2);
		return generateTimestampForDatePattern(DATE_PATTERN, cal.getTime());
	}
	
	
	private static String generateTimestampForDatePattern(final String DATE_PATTERN, Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	
	public static Date generateDateForTimestamp(String timestamp){
		final String DATE_PATTERN = "yyyyMMddHHmmss";
		return generateDateForPatternAndTimestamp(timestamp, DATE_PATTERN);
	}

	private static Date generateDateForPatternAndTimestamp(String timestamp,
			final String DATE_PATTERN) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = dateFormat.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String generateTimestampFromWikipediaTimestamp(String wikiTimestamp){
		String result = wikiTimestamp.replaceAll("T|Z|-|:", "");
		return result;
	}

}
