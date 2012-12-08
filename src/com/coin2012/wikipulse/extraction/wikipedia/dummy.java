package com.coin2012.wikipulse.extraction.wikipedia;

import java.util.Date;

import com.coin2012.wikipulse.extraction.utils.TimestampGenerator;

public class dummy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String x  = "2012-12-08T14:34:50Z";
		x = x.replaceAll("T|Z|-|:", "");
		System.out.println(x);
	}

}
