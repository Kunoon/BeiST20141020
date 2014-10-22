package com.bei.smartravel.customization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 格式化日期，得到所需的时间格式
 * @author Yongkun
 *
 */
public class MyFormatDateTime {
	private static MyFormatDateTime myJudgeTime = null;
	private SimpleDateFormat df = null;
	
	public MyFormatDateTime() {
		// TODO Auto-generated constructor stub
		df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	}
	/**
	 * 获得MyJudgeTime对象
	 * @return
	 */
	public static MyFormatDateTime getMyFormatDateTime() {
		if (myJudgeTime == null)
			myJudgeTime = new MyFormatDateTime();
		return myJudgeTime;
	}
	
	/**
	 * 获得 1970-1-1 格式的时间
	 * @param timeStr 如果 timeStr==null 获取当前系统时间，否则获取指定的timeStr时间	1970-01-01到此时的毫秒的长整型字符串
	 * @return String 时间
	 */
	public String GetDate(String timeStr) {
		if(timeStr == null)
			return df.format(new Date());
		return df.format(new Date(Long.valueOf(timeStr)*1000));
	}
	
	/**
	 * 获得 yyyy/MM/dd HH:mm:ss 格式的时间
	 * @param timeStr 如果 timeStr==null 获取当前系统时间，否则获取指定的timeStr时间	1970-01-01到此时的毫秒的长整型字符串
	 * @return String 时间
	 */
	public String GetDateTime(String timeStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if(timeStr == null)
			return dateFormat.format(new Date());
		return dateFormat.format(new Date(Long.valueOf(timeStr)*1000));
	}
	
	/**
	 * 获得年份
	 * @param timeStr 如果 timeStr==null 获取当前系统年份，否则获取指定的timeStr年份	1970-01-01到此时的毫秒的长整型字符串
	 * @return int 年份
	 */
	public int GetYear(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getYear() + 1900);
		return Integer.valueOf( new Date(Long.valueOf(timeStr)*1000).getYear() + 1900 );
	}
	
	/**
	 * 获得月份
	 * @param timeStr 如果 timeStr==null 获取当前系统月份，否则获取指定的timeStr月份	1970-01-01到此时的毫秒的长整型字符串
	 * @return int 月份
	 */
	public int GetMonth(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getMonth()) + 1 ;
		return Integer.valueOf( new Date(Long.valueOf(timeStr)*1000).getMonth() ) + 1 ;
	}
	
	/**
	 * 获得日期
	 * @param timeStr 如果 timeStr==null 获取当前系统日期，否则获取指定的timeStr日期	1970-01-01到此时的毫秒的长整型字符串
	 * @return int 日期
	 */
	public int GetDay(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getDate());
		return Integer.valueOf( new Date(Long.valueOf(timeStr)*1000).getDate() );
	}
	
	public int GetHours(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getHours());
		return Integer.valueOf(new Date(Long.valueOf(timeStr)*1000).getHours());
	}
	
	public int GetMinutes(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getMinutes());
		return Integer.valueOf(new Date(Long.valueOf(timeStr)*1000).getMinutes());
	}
	
	public int GetSeconds(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getSeconds());
		return Integer.valueOf(new Date(Long.valueOf(timeStr)*1000).getSeconds());
	}
	
	/**
	 * 比较时间targetTime与currentTime的先后；参数为NULL，获取当前时间
	 * @param currentTime	1970-01-01到此时的毫秒的长整型字符串
	 * @param targetTime	1970-01-01到此时的毫秒的长整型字符串
	 * @return 如果currentTime <= targetTime，返回true，否则返回false
	 */
	public boolean CompareTime(String currentTime, String targetTime, boolean isBeng) {
		if(GetYear(currentTime) < GetYear(targetTime)) {
			return true;
		} else if(GetYear(currentTime) == GetYear(targetTime)) {
			if(GetMonth(currentTime) < GetMonth(targetTime)) {
				return true;
			} else if(GetMonth(currentTime) == GetMonth(targetTime)) {
				if(!isBeng) {
					if(GetDay(currentTime) <= GetDay(targetTime)) {
						return true;
					}
				} else {
					if(GetDay(currentTime) < GetDay(targetTime)) {
						return true;
					} else if(GetDay(currentTime) == GetDay(targetTime)) {
						if(GetHours(currentTime) < GetHours(targetTime)) {
							return true;
						}else if(GetHours(currentTime) == GetHours(targetTime)) {
							if(GetMinutes(currentTime) < GetMinutes(targetTime)) {
								return true;
							} else if(GetMinutes(currentTime) == GetMinutes(targetTime)) {
								if(GetSeconds(currentTime) <= GetSeconds(targetTime)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 比较时间targetTime与currentTime的先后；参数为NULL，获取当前时间
	 * @param currentTime	1970-01-01到此时的毫秒的长整型字符串
	 * @param targetTime	1970-01-01到此时的毫秒的长整型字符串
	 * @return 如果currentTime >= targetTime，返回true，否则返回false
	 */
	public boolean CompareTimeLess(String currentTime, String targetTime) {
		if(GetYear(currentTime) >GetYear(targetTime)) {
			return true;
		} else if(GetYear(currentTime) == GetYear(targetTime)) {
			if(GetMonth(currentTime) > GetMonth(targetTime)) {
				return true;
			} else if(GetMonth(currentTime) == GetMonth(targetTime)) {
				if(GetDay(currentTime) > GetDay(targetTime)) {
					return true;
				} else if(GetDay(currentTime) == GetDay(targetTime)) {
					if(GetHours(currentTime) > GetHours(targetTime)) {
						return true;
					} else if(GetHours(currentTime) == GetHours(targetTime)) {
						if(GetMinutes(currentTime) > GetMinutes(targetTime)) {
							return true;
						} else if(GetMinutes(currentTime) == GetMinutes(targetTime)) {
							if(GetSeconds(currentTime) > GetSeconds(targetTime)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 获得从1970-01-01到指定时间格式的毫秒数值
	 * @param tmpForm	需要转换的时间格式 eg: yyyyMMddHHmmss
	 * @param tmpStr	时间字符串 eg: 20130223053159
	 * @return
	 */
	public Long dateToSecond(String tmpForm, String tmpStr) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(new SimpleDateFormat(tmpForm).parse(tmpStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}
}
