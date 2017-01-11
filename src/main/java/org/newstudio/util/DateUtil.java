package org.newstudio.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {
	private static final long DAYS_BETWEEN_18991230_AND_19700101 = 25569;
	private static final long MILLISECONDS_PER_DAY = 86400000;
	private static final long UNIX_FILETIME_DIFF = 11644473600000L;
	private static final int MILLISECOND_MULTIPLE = 10000;

	public static final int BEFORE = -1;
	public static final int EQUAL = 0;
	public static final int AFTER = 1;

	/**
	 * Util 類別不允許直接初始化。
	 */
	private DateUtil() {
		throw new UnsupportedOperationException("DateUtil class cannot be initiated directly.");
	}

	/**
	 * 將 Date 物件轉成字串。
	 *
	 * @param dateTime Date 物件
	 * @return yyyy-MM-dd HH:mm:ss 格式字串
	 */
	public static String toDateTimeString(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dateTime);
	}

	/**
	 * 將一個Date物件的時間欄位清除，保留日期。
	 *
	 * @param date 要清除的日期和時間
	 * @return 日期
	 */
	public static Date dateOf(Date date) {
		return startOfTheDay(date);
	}

	/**
	 * 計算兩個時間相差的毫秒數。
	 *
	 * @param a 時間 A
	 * @param b 時間 B
	 * @return 相差毫秒數 (永遠為正值)
	 */
	public static long milliSecondsBetween(Date a, Date b) {
		return Math.abs(a.getTime() - b.getTime());
	}

	/**
	 * 計算兩個時間相差的秒數。
	 *
	 * @param a 時間 A
	 * @param b 時間 B
	 * @return 相差秒數 (永遠為正值)
	 */
	public static long secondsBetween(Date a, Date b) {
		return milliSecondsBetween(a, b) / 1000;
	}

	/**
	 * 計算兩個時間相差的分鐘數。
	 *
	 * @param a 時間 A
	 * @param b 時間 B
	 * @return 相差分鐘數 (永遠為正值)
	 */
	public static long minutesBetween(Date a, Date b) {
		return secondsBetween(a, b) / 60;
	}

	/**
	 * 計算兩個日期相差的小時數。
	 *
	 * @param a 時間 A
	 * @param b 時間 B
	 * @return 相差小時數 (永遠為正值)
	 */
	public static long hoursBetween(Date a, Date b) {
		return minutesBetween(a, b) / 60;
	}

	/**
	 * 計算兩個日期相差的天數。
	 *
	 * @param a 時間 A
	 * @param b 時間 B
	 * @return 相差天數 (永遠為正值)
	 */
	public static long daysBetween(Date a, Date b) {
		return hoursBetween(a, b) / 24;
	}

	/**
	 * 計算兩個日期相差的天數，但可能為負值。
	 *
	 * @param a 時間 A
	 * @param b 時間 B (基準點)
	 * @return 相差天數，如果 A 比 B 早，則天數是負的
	 */
	public static long daysDiff(Date a, Date b) {
		long daysBetween = daysBetween(a, b);
		if (a.compareTo(b) < 0) {
			daysBetween *= -1;
		}
		return daysBetween;
	}

	/**
	 * 增加毫秒。
	 *
	 * @param date Date 物件
	 * @param milliSeconds 毫秒
	 * @return 增加的 Date 物件
	 */
	public static Date incMilliSecond(Date date, long milliSeconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND, (int)milliSeconds);
		return cal.getTime();
	}

	/**
	 * 增加秒。
	 *
	 * @param date Date 物件
	 * @param second 秒
	 * @return 增加的 Date 物件
	 */
	public static Date incSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 增加分。
	 *
	 * @param date Date 物件
	 * @param minute 分
	 * @return 增加的 Date 物件
	 */
	public static Date incMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}

	/**
	 * 增加時。
	 *
	 * @param date Date 物件
	 * @param hour 時
	 * @return 增加的 Date 物件
	 */
	public static Date incHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	/**
	 * 增加天。
	 *
	 * @param date Date 物件
	 * @param day 天
	 * @return 增加的 Date 物件
	 */
	public static Date incDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	/**
	 * 增加週。
	 *
	 * @param date Date 物件
	 * @param week 週
	 * @return 增加的 Date 物件
	 */
	public static Date incWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, week);
		return cal.getTime();
	}

	/**
	 * 增加月。
	 *
	 * @param date Date 物件
	 * @param month 月
	 * @return 增加的 Date 物件
	 */
	public static Date incMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	/**
	 * 取得現在時間 UTC。
	 *
	 * @return UTC 的時間
	 */
	public static Date nowUTC() {
		Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, utc.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, utc.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, utc.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, utc.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, utc.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, utc.get(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * 取得現在本地時區時間。
	 *
	 * @return 本地時區時間
	 */
	public static Date nowLocal() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * 取得 UTC 與本地時區的差異毫秒數。含日光節約時間 (有的話)
	 *
	 * @return 差異毫秒數
	 */
	public static int timeZoneOffsetMillis() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
	}

	/**
	 * 取得該月第一天。
	 *
	 * @param date 時間點
	 * @return 該月第一天
	 */
	public static Date startOfTheMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該月最後一天。
	 *
	 * @param date 時間點
	 * @return 該月最後一天
	 */
	public static Date endOfTheMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該年第一天。
	 *
	 * @param date 時間點
	 * @return 該年第一天
	 */
	public static Date startOfTheYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該年最後一天。
	 *
	 * @param date 時間點
	 * @return 該年最後一天
	 */
	public static Date endOfTheYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該週第一天。
	 *
	 * @param date 時間點
	 * @return 該週第一天
	 */
	public static Date startOfTheWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該週最後一天。
	 *
	 * @param date 時間點
	 * @return 該週最後一天
	 */
	public static Date endOfTheWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該日開頭。
	 *
	 * @param date 時間點
	 * @return 該日開頭
	 */
	public static Date startOfTheDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 取得該日結尾。
	 *
	 * @param date 時間點
	 * @return 該日結尾
	 */
	public static Date endOfTheDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}

	/**
	 * 比較兩個日期。
	 *
	 * @param a 第一個日期
	 * @param b 第二個日期
	 * @return -1表示a在b之前，0表示a和b相同，1表示a在b之後
	 */
	public static int compareDate(Date a, Date b) {
		return compareDateTime(dateOf(a), dateOf(b));
	}

	/**
	 * 比較兩個日期時間。
	 *
	 * @param a 第一個日期時間
	 * @param b 第二個日期時間
	 * @return -1表示a在b之前，0表示a和b相同，1表示a在b之後
	 */
	public static int compareDateTime(Date a, Date b) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(a);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(b);
		return cal1.compareTo(cal2);
	}

	/**
	 * 將Delphi格式的時間轉成Java格式。
	 *
	 * @param time Delphi格式GMT時間
	 * @return Java格式GMT時間
	 */
	public static Date delphiTimeToJavaTime(final double time) {
		long timeZoneOffsetMillis = timeZoneOffsetMillis();
		long millis = (long)((time - DAYS_BETWEEN_18991230_AND_19700101) * MILLISECONDS_PER_DAY - timeZoneOffsetMillis);
		// 再轉一次，消除誤差
		double delphiTime = (millis + timeZoneOffsetMillis + DAYS_BETWEEN_18991230_AND_19700101 * MILLISECONDS_PER_DAY) / (double)MILLISECONDS_PER_DAY;
		millis = (long)((delphiTime - DAYS_BETWEEN_18991230_AND_19700101) * MILLISECONDS_PER_DAY - timeZoneOffsetMillis);
		return new Date(millis);
	}

	/**
	 * 將Java格式的時間轉成Delphi格式。
	 *
	 * @param time Java格式GMT時間
	 * @return Delphi格式GMT時間
	 */
	public static double javaTimeToDelphiTime(final Date time) {
		if (time == null) {
			return 0.0;
		}
		long timeZoneOffsetMillis = timeZoneOffsetMillis();
		return (time.getTime() + timeZoneOffsetMillis + DAYS_BETWEEN_18991230_AND_19700101
				* MILLISECONDS_PER_DAY)
				/ (double) MILLISECONDS_PER_DAY;
	}

	/**
	 * 檔案時間轉為 Date 物件。
	 *
	 * @param time 檔案時間
	 * @return Date 物件
	 */
	public static Date fileTimeToJavaTime(final long time){
		return new Date(time / MILLISECOND_MULTIPLE - UNIX_FILETIME_DIFF);
	}

	/**
	 * UTC 轉本地時間。
	 *
	 * @param value UTC
	 * @return 本地時間
	 */
	public static Date toLocalTime(Date value) {
		return new Date(value.getTime() + timeZoneOffsetMillis());
	}

	/**
	 * 本地時間轉 UTC。
	 *
	 * @param value 本地時間
	 * @return UTC
	 */
	public static Date toUtcTime(Date value) {
		return new Date(value.getTime() - timeZoneOffsetMillis());
	}
}
