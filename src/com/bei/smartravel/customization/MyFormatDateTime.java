package com.bei.smartravel.customization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ��ʽ�����ڣ��õ������ʱ���ʽ
 * @author Yongkun
 *
 */
public class MyFormatDateTime {
	private static MyFormatDateTime myJudgeTime = null;
	private SimpleDateFormat df = null;
	
	public MyFormatDateTime() {
		// TODO Auto-generated constructor stub
		df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
	}
	/**
	 * ���MyJudgeTime����
	 * @return
	 */
	public static MyFormatDateTime getMyFormatDateTime() {
		if (myJudgeTime == null)
			myJudgeTime = new MyFormatDateTime();
		return myJudgeTime;
	}
	
	/**
	 * ��� 1970-1-1 ��ʽ��ʱ��
	 * @param timeStr ��� timeStr==null ��ȡ��ǰϵͳʱ�䣬�����ȡָ����timeStrʱ��	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return String ʱ��
	 */
	public String GetDate(String timeStr) {
		if(timeStr == null)
			return df.format(new Date());
		return df.format(new Date(Long.valueOf(timeStr)*1000));
	}
	
	/**
	 * ��� yyyy/MM/dd HH:mm:ss ��ʽ��ʱ��
	 * @param timeStr ��� timeStr==null ��ȡ��ǰϵͳʱ�䣬�����ȡָ����timeStrʱ��	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return String ʱ��
	 */
	public String GetDateTime(String timeStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if(timeStr == null)
			return dateFormat.format(new Date());
		return dateFormat.format(new Date(Long.valueOf(timeStr)*1000));
	}
	
	/**
	 * ������
	 * @param timeStr ��� timeStr==null ��ȡ��ǰϵͳ��ݣ������ȡָ����timeStr���	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return int ���
	 */
	public int GetYear(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getYear() + 1900);
		return Integer.valueOf( new Date(Long.valueOf(timeStr)*1000).getYear() + 1900 );
	}
	
	/**
	 * ����·�
	 * @param timeStr ��� timeStr==null ��ȡ��ǰϵͳ�·ݣ������ȡָ����timeStr�·�	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return int �·�
	 */
	public int GetMonth(String timeStr) {
		if(timeStr == null)
			return Integer.valueOf(new Date().getMonth()) + 1 ;
		return Integer.valueOf( new Date(Long.valueOf(timeStr)*1000).getMonth() ) + 1 ;
	}
	
	/**
	 * �������
	 * @param timeStr ��� timeStr==null ��ȡ��ǰϵͳ���ڣ������ȡָ����timeStr����	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return int ����
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
	 * �Ƚ�ʱ��targetTime��currentTime���Ⱥ󣻲���ΪNULL����ȡ��ǰʱ��
	 * @param currentTime	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @param targetTime	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return ���currentTime <= targetTime������true�����򷵻�false
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
	 * �Ƚ�ʱ��targetTime��currentTime���Ⱥ󣻲���ΪNULL����ȡ��ǰʱ��
	 * @param currentTime	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @param targetTime	1970-01-01����ʱ�ĺ���ĳ������ַ���
	 * @return ���currentTime >= targetTime������true�����򷵻�false
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
	 * ��ô�1970-01-01��ָ��ʱ���ʽ�ĺ�����ֵ
	 * @param tmpForm	��Ҫת����ʱ���ʽ eg: yyyyMMddHHmmss
	 * @param tmpStr	ʱ���ַ��� eg: 20130223053159
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
