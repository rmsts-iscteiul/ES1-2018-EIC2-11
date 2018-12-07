package apps;

import java.util.Calendar;
/**
 * This is an enumerator that has time values in days. E.G Last 24h = 1 day.
 * @author ES1-2018-EIC2-11
 *
 */
public enum TimeFilter { // Displayed in Days

	LAST_HOUR(1 / 24), LAST_24H(1), LAST_WEEK(7), LAST_MONTH(30), ALL_TIME(), SPECIFIC_DAY(1);
	
	/**
	 * current date in milliseconds (everyone is System.currentTimeMillis()) except Specific_day that is the only that can be modified.
	 */
	private long date = System.currentTimeMillis();
	
	/**
	 * days that take the enum.
	 */
	private int dif;
	/**
	 * Constructor of the ALL TIME
	 */
	private TimeFilter() {
		dif = 0;
	}
	/**
	 * Constructor of all the others enums
	 * @param dif how many days it is
	 */
	private TimeFilter(int dif) {
		this.dif = dif;
	}
	/**
	 * getter of the date
	 * @return date
	 */
	public long getDate() {
		return date;
	}
	/**
	 * ggetter of the dif
	 * @return dif
	 */
	public int getDif() {
		return dif;
	}
	/**
	 * Set the date of the specific number enum
	 * @param year - year 
	 * @param month - month
	 * @param day - day
	 */
	public void setDate(int year, int month, int day) {
		if (this.equals(SPECIFIC_DAY)) {
			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, day);
			date = c.getTime().getTime();
		}
	}
}
