package com.ai.paas.ipaas.me.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTest {
	public static void main(String args[]) {
		try {
			String ts = "2016-02-22T07:47:00.791Z";
			System.out.println("ts =" + ts);
			ts = ts.replace("Z", "UTC");
			System.out.println("ts =" + ts);
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

			Date dt = sdf.parse(ts);

			SimpleDateFormat sdf2 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println(sdf2.format(dt));
//			TimeZone tz = sdf.getTimeZone();
//			Calendar c = sdf.getCalendar();
//			System.out.println("Display name:" + tz.getDisplayName());
//			System.out.println(getString(c));
		} catch (ParseException pe) {
			System.out.println("Error offset:" + pe.getErrorOffset());
			pe.printStackTrace();
		}
	}

	private static String getString(Calendar c) {
		StringBuffer result = new StringBuffer();
		result.append(c.get(Calendar.YEAR));
		result.append("-");
		result.append((c.get(Calendar.MONTH) + 1));
		result.append("-");
		result.append(c.get(Calendar.DAY_OF_MONTH));
		result.append("");
		result.append(c.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(c.get(Calendar.MINUTE));
		result.append(":");
		result.append(c.get(Calendar.SECOND));
		return result.toString();
	}

}