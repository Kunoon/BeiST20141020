package com.sdses.printer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;

import com.bei.smartravel.chen.configer;
import com.bei.smartravel.customization.MyFormatDateTime;
import com.bei.smartravel.macro.Macro;
import com.sdses.serialport.SerialPort;

public class MyPrinter4ShenSi {
	
	private SerialPort mSerialPort = null;
	private static String mPath = "/dev/ttyS3";
	private static int mBaud = 9600;
	public boolean comOpenFlag = false;

	private static MyPrinter4ShenSi myPrinter = null;
	
	private static final String TITLE_WICKET_PRINTER = "    门票收据";
	private static final String TITLE_CHECK_PRINTER = "    查票打印";
	private static final String SCENIC_NAME_PRINTER = "景区名称(SCENIC NAME):";
	private static final String GOODS_ID_PRINTER = "订 单 号：";
	private static final String GOODS_NAME_PRINTER = "商品名称：";
	private static final String GOODS_NUM_PRINTER = "商品数量：";
	private static final String GOODS_USEDNUM_PRINTER = "使用数量：";
	private static final String GOODS_URPLISNUM_PRINTER = "剩余数量：";
	private static final String GOODS_PRICE_PRINTER = "商品单价：";
	private static final String GOODS_GOOD_STATUS = "商品状态：";
	private static final String GOODS_PAY_WAY = "支付类型：";
	private static final String GOODS_FROM_WHERE = "商品来源：";
	private static final String TECHNICAL_SUPPORT = "           技术支持\n    杭州贝竹电子商务有限公司";

	private String tmpStr = "";
	
	public MyPrinter4ShenSi() {
		// TODO Auto-generated constructor stub
		tmpStr = "";
		openCom();
	}
	
	/**
	 * 获得MyPrinter对象
	 * @return
	 */
	public synchronized static MyPrinter4ShenSi getMyPrinter() {
		if(myPrinter == null)
			myPrinter = new MyPrinter4ShenSi();
		return myPrinter;
	}
	
	/**
	 * 打印收据票头
	 */
	private synchronized void StartTitlePrinter(int which) {
	    try {
			/* 初始化打印机 */
		    printWrite(PrintCommand.getCmdEsc());
			/* 打印Title */
		    /* 字体放大 */
		    printWrite(PrintCommand.getCmdW((byte)0x01));
//		    /* 字体加粗 */
//		    printWrite(PrintCommand.getCmdN((byte)0x01));
		    if(which == Macro.WICKET)
		    	printWrite(TITLE_WICKET_PRINTER.getBytes("GB2312"));
		    else
		    	printWrite(TITLE_CHECK_PRINTER.getBytes("GB2312"));
		    /* 恢复默认字体 */
		    printWrite(PrintCommand.getCmdW((byte)0x00));
//		    /* 字体加粗 */
//		    printWrite(PrintCommand.getCmdN((byte)0x01));
		    /* 向前走纸2行 */
		    printWrite(PrintCommand.getCmdLF());
		    printWrite(PrintCommand.getCmdLF());
		    /* 景区名称 */
		    printWrite(SCENIC_NAME_PRINTER.getBytes("GB2312"));
		    /* 换行 */
		    printWrite(PrintCommand.getCmdLF());
		    /* 打印景区名称 */
		    printWrite(configer.getConfiger().getonfValue(configer.CMD_CONF_NAME, "").getBytes("GB2312"));
		    /* 换行 */
		    printWrite(PrintCommand.getCmdLF());
//		    /* 字体加粗 ,恢复*/
//		    printWrite(PrintCommand.getCmdN((byte)0x00));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 结束打印
	 */
	private synchronized void EndPrinter() {
	    try {
	    	tmpStr = "\n================================";
			printWrite(tmpStr.getBytes("GB2312"));
			/* 换行 */
		    printWrite(PrintCommand.getCmdLF());
		    /* 景区名称 */
			printWrite(TECHNICAL_SUPPORT.getBytes("GB2312"));
			/* 换行 */
		    printWrite(PrintCommand.getCmdLF());
		    /* 打印时间 */
		    tmpStr = "     " + MyFormatDateTime.getMyFormatDateTime().GetDateTime(null);
			printWrite(tmpStr.getBytes("GB2312"));
		    tmpStr = "\n\n·····沿此线撕开·····";
			printWrite(tmpStr.getBytes("GB2312"));
		    /* 向前走纸2行 */
		    printWrite(PrintCommand.getCmdLF());
		    printWrite(PrintCommand.getCmdLF());
		    printWrite(PrintCommand.getCmdLF());
		    printWrite(PrintCommand.getCmdLF());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印订单信息，IDNum==null用于查询时的订单打印，IDNum!=null用于检票时的订单打印
	 * @param printerData	需要打印的订单字符串
	 * @param IDNum	检票关键字，也可用于区分打印的订单类型，详见方法说明
	 * @param which 使用环境
	 */
	public synchronized void OrdersPrinter(List<HashMap<String, Object>> tmpPrinterData, String IDNum, int which) {
		List<HashMap<String, Object>> printerData = tmpPrinterData;
		int tmpNum = 1;
		if (which == Macro.WICKET) {
			tmpNum = Integer.valueOf(configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.PORT));
		}
		for(int ii = 0; ii < tmpNum; ii++) {
			StartTitlePrinter(which);
		    try {
		    	if(IDNum != null) {
				    /* 打印身份证号 */
				    printWrite(IDNum.getBytes("GB2312"));
				    /* 换行 */
				    printWrite(PrintCommand.getCmdLF());
		    	}
			    
				for(int i = 0; i < printerData.size(); i++) {
					tmpStr = "";
					if(i == 0) {
						tmpStr += "================================\n";
					}
					
					tmpStr += GOODS_ID_PRINTER + printerData.get(i).get(Macro.GOODS_ID) + "\n";
//					tmpStr += GOODS_NAME_PRINTER + printerData.get(i).get(Macro.GOODS_NAME) + "\n";getCmdEscAN
///////////////////////////////////===========================///////////////////////////////////
					String tmpLName = printerData.get(i).get(Macro.GOODS_NAME).toString();
					if(tmpLName.split("（").length > 1) {
						tmpStr += GOODS_NAME_PRINTER + tmpLName.split("（")[0] + "\n";
						printWrite(tmpStr.getBytes("GB2312"));
						Thread.sleep(150);
						tmpStr = "包含景点：";
						String tmpName = tmpLName.split("（")[1].substring(0, tmpLName.split("（")[1].length()-1);
						for(int tmpNameLen = 0; tmpNameLen < tmpName.split("\\+").length; tmpNameLen++) {
							if(tmpNameLen == 0)
								tmpStr += tmpName.split("\\+")[tmpNameLen] + cumSpace(tmpName.split("\\+")[tmpNameLen]);
							else
								tmpStr += "          " + tmpName.split("\\+")[tmpNameLen] + cumSpace(tmpName.split("\\+")[tmpNameLen]);
						}
					} else {
						tmpStr += GOODS_NAME_PRINTER + tmpLName + "\n";
					}
					printWrite(tmpStr.getBytes("GB2312"));
					Thread.sleep(150);
					tmpStr = "";
					
///////////////////////////////////===========================///////////////////////////////////
					if(IDNum != null) {
						tmpStr += GOODS_USEDNUM_PRINTER + printerData.get(i).get(Macro.GOODS_QUANTITY) + "\n";
						if(printerData.get(i).get(Macro.GOODS_QUANTITY_OLD) == null) {
							tmpStr += GOODS_URPLISNUM_PRINTER + "0" + "\n";
						} else {
							tmpStr += GOODS_URPLISNUM_PRINTER + Integer.toString(
									Integer.valueOf((String) printerData.get(i).get(Macro.GOODS_QUANTITY_OLD))
									- Integer.valueOf((String) printerData.get(i).get(Macro.GOODS_QUANTITY))) + "\n";
						}
					} else {
						tmpStr += GOODS_NUM_PRINTER + printerData.get(i).get(Macro.GOODS_QUANTITY) + "\n";
					}
					tmpStr += GOODS_PRICE_PRINTER + printerData.get(i).get(Macro.GOODS_PRICE) + " RMB\n";
					if(IDNum == null) {
						if(printerData.get(i).get(Macro.GOODS_PAY_STATUS).equals(Macro.TICKET_STATUS_PAID)) {
							tmpStr += GOODS_GOOD_STATUS + printerData.get(i).get(Macro.GOODS_USED_STATUS) + "\n";
						} else {
							tmpStr += GOODS_GOOD_STATUS + printerData.get(i).get(Macro.GOODS_PAY_STATUS) + "\n";
						}
					} else {
						if(printerData.get(i).get(Macro.GOODS_PAY_STATUS).toString()
								.indexOf(Macro.TICKET_STATUS_UNPAID) > -1) {
							tmpStr += GOODS_PAY_WAY + "景区到付订单" + "\n";
						} else {
							tmpStr += GOODS_PAY_WAY + "在线支付订单" + "\n";
						}
					}
					tmpStr += GOODS_FROM_WHERE + printerData.get(i).get(Macro.GOODS_FROM_WHERE) + "\n";
					printWrite(tmpStr.getBytes("GB2312"));
					tmpStr = "";
					if(i < (printerData.size()-1)) {
						tmpStr += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
						printWrite(tmpStr.getBytes("GB2312"));
					} else {
						if (which == Macro.WICKET) {
							if(ii == 0) {
								tmpStr += "                     " + Integer.toString(ii+1) + " 联：游客";
							} else {
								tmpStr += "                     " + Integer.toString(ii+1) + " 联：存根";
							}
							printWrite(tmpStr.getBytes("GB2312"));
						}
					}
					/* 等待打印buffer flush */
					Thread.sleep(150);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    EndPrinter();
		}
	}
	
	private String cumSpace(String str) {
		int tmp0 = -1;
		String str0 = "";
		try {
			tmp0 = 20 - str.getBytes("GB2312").length;
			System.out.println(str + tmp0);
			while(tmp0 > 0) {
				str0 += " ";
				tmp0 --;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (str0 + "□\n");
	}
	
	public void printWrite(byte [] tmpByte) {
		mSerialPort.oneCommand(tmpByte, tmpByte.length, 0, 0);
	}
	
	public int openCom() {
		if (mSerialPort == null) {
			if ((mPath.length() == 0) || (mBaud == -1)) {
				throw new InvalidParameterException();
			}
			try {
				mSerialPort = new SerialPort(new File(mPath), mBaud, 0);
			} catch (SecurityException e) {
				return -2;
			} catch (IOException e) {
				return -3;
			}
			comOpenFlag = true;
		}
		return 1;
	}
}
