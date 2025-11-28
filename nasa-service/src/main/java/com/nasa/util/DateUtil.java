package com.nasa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {
	
	public static Date convertDate(String dateString) {
		
        // Define the date format that matches the input string
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // Set lenient to false to enforce strict parsing
        formatter.setLenient(false);
        Date date = null;
        try {
            // Parse the string into a Date object
            date = formatter.parse(dateString);
            
            // Output the Date object
            System.out.println("Converted Date: " + date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid value for date");
        }
        
        return date;
	}
	
	
	public static Date getClientDate() {
	    Calendar calendar = new GregorianCalendar();
	    
	    TimeZone timeZone = TimeZone.getTimeZone(TimeZoneContext.getTimezone());
	    
	    calendar.setTimeZone(timeZone);
	    
	    // Get the year, month (add 1), and day
	    int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH) + 1; // Add 1 to the month
	    int day = calendar.get(Calendar.DATE);
	    
	    // Format month and day to ensure they are two digits
	    StringBuilder builder = new StringBuilder()
	            .append(year)
	            .append("-")
	            .append(String.format("%02d", month)) // Format month to two digits
	            .append("-")
	            .append(String.format("%02d", day)); // Format day to two digits

	    System.out.println("Formatted Date String: " + builder.toString());
	    return convertDate(builder.toString());
	}


}
