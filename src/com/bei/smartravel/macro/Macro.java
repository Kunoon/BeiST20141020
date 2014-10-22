package com.bei.smartravel.macro;
/**
 * 
 * @author Yongkun
 *
 */
public interface Macro {
	public final boolean ACTION_XIAOBING = false;
	public final boolean ACTION_XIAOBING_NOVICE = false;
	public final boolean ACTION_SHENSI = true;
	public final boolean ACTION_SHENSI_PRINT = false;
	public final boolean ACTION_ZHIGULIAN = false;
	
	public static final String testIDNun = "332523196905231813";
	
	// Intent request codes
	public static final int REQUEST_MYKEYBOARD = 0;
	public static final int REQUEST_CHECK_SHOW = 1;
	public static final int REQUEST_SETTING = 3;
	
	public static final String CHECK_ID_DATA = "860003818416";
	
	public static final int WICKET = 0;
	public static final int CHECK = 1;
	
	public static final String GOODS_NUM = "GoodNum";
	public static final String GOODS_ID = "GoodID";
	public static final String GOODS_NAME = "GoodName";
	public static final String GOODS_QUANTITY = "GoodQuantity";
	public static final String GOODS_QUANTITY_OLD = "GoodQuantityOld";
	public static final String GOODS_PRICE = "GoodPrice";
	public static final String GOODS_YEARTICK = "GoodIsYearTick";
	public static final String GOODS_PLANTIME = "plantime";
	public static final String GOODS_PAY_STATUS = "GoodPayStatus";
	public static final String GOODS_USED_STATUS = "GoodUsedStatus";
	public static final String GOODS_START_TIME = "GoodStartTime";
	public static final String GOODS_END_TIME = "GoodEndTime";
	public static final String GOODS_FROM_WHERE = "GoodFromWhere";
	public static final String GOODS_WEEK = "GoodWeek";
	public static final String GOODS_VALID = "GoodValid";
	public static final String TICKET_STATUS_PAID = "ÒÑ¸¶";
	public static final String TICKET_STATUS_UNPAID = "Î´¸¶";
	public static final String TICKET_STATUS_USED = "ÒÑ¼ì";
	public static final String TICKET_STATUS_UNUSED = "Î´¼ì";
	public static final int CHECK_TIME_START_DIALOG = 0;
	public static final int CHECK_TIME_STOP_DIALOG = 1;
	
	public static final String CHECK_QUERY_PARAMETER_IDNUM = "checkKeyWord_IDNum";
	public static final String CHECK_QUERY_PARAMETER_GOODSID = "checkKeyWord_GoodsID";
	public static final String CHECK_QUERY_PARAMETER_GOODNAME = "checkKeyWord_GoodName";
	public static final String CHECK_QUERY_PARAMETER_STATUS_USED = "checkKeyWord_Status_Used";
	public static final String CHECK_QUERY_PARAMETER_STATUS_PAY = "checkKeyWord_Status_Pay";
	public static final String CHECK_QUERY_PARAMETER_BEGIN_TIME = "checkKeyWord_BeginTime";
	public static final String CHECK_QUERY_PARAMETER_END_TIME = "checkKeyWord_EndTime";
}
