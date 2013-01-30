package com.coin2012.wikipulse.identification;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;

public class Timespan {

	private String start = TimestampGenerator.generateTimestampFromTwoHoursAgo();
	private String end = "0";

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

}
