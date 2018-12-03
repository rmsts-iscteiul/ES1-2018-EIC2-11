package apps;

import java.util.Calendar;

public enum TimeFilter { // Displayed in Days

	LAST_HOUR(1 / 24), LAST_24H(1), LAST_WEEK(7), LAST_MONTH(30), ALL_TIME(), SPECIFIC_DAY(1);

	private long date = System.currentTimeMillis();
	private int dif;

	private TimeFilter() {
		dif = 0;
	}

	private TimeFilter(int dif) {
		this.dif = dif;
	}

	public long getDate() {
		return date;
	}

	public int getDif() {
		return dif;
	}

	public void setDate(int year, int month, int day) {
		if (this.equals(SPECIFIC_DAY)) {
			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, day);
			date = c.getTime().getTime();
		}
	}

//	public static void main(String[] args) {
//		System.out.println(Math.abs(System.currentTimeMillis() ));
//		System.out.println("Nxt");
//		TimeFilter t;
//		t = TimeFilter.ALL_TIME;
//		t.setDate(2018, 12, 1);
//		System.out.println(t.getDate());
//		t = TimeFilter.SPECIFIC_DAY;
//		t.setDate(2018, 12, 3);
//		System.out.println(t.getDate());
//	}
}
