package com.example.weibotest.util;

/**
 * 
 * @author zhangxin about time tool class
 * 
 */
public class TimeTools {

	/**
	 * 
	 * @param time
	 *            给一个时间String
	 * @return
	 */
	public static String changeTimeAndYear(String time) {
		String[] s = time.split(" ");
		String mouth = getMouth(s[1]) + "月";
		String year = s[5] + "年";
		String day = s[2] + "号";
		int i = Integer.parseInt(s[3].split(":")[0]);
		String hour = (12 >= i) ? "上午" + i+"点" : "下午" + (i - 12)+"点";

		String times = year + mouth + day + hour;
		return times;
	}
	public static String getMouth(String s) {
		switch (s) {
		case "Jan":
			return "1";
		case "Fab":
			return "2";
		case "Mar":
			return "3";
		case "Apr":
			return "4";
		case "May":
			return "5";
		case "Jun":
			return "6";
		case "Jul":
			return "7";
		case "Aug":
			return "8";
		case "Sep":
			return "9";
		case "Oct":
			return "10";
		case "Nov":
			return "11";
		case "Dec":
			return "12";
		default:
			return "1";
		}
	}
}
