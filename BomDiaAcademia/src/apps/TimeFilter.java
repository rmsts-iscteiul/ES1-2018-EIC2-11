package apps;

import java.util.Date;

public enum TimeFilter {

	LAST_HOUR(new Date(3600000)), LAST_24H(new Date(86400000)), LAST_WEEK(new Date(604800000)), LAST_MONTH(new Date((long) 259200000.0)), ALL_TIME(new Date());
	
	Date date;
	private TimeFilter(Date date) {
		this.date = date;
	}
}
