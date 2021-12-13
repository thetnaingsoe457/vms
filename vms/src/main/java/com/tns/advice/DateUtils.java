package com.tns.advice;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
}
