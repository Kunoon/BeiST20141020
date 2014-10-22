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
	
	private static final String TITLE_WICKET_PRINTER = "    ��Ʊ�վ�";
	private static final String TITLE_CHECK_PRINTER = "    ��Ʊ��ӡ";
	private static final String SCENIC_NAME_PRINTER = "��������(SCENIC NAME):";
	private static final String GOODS_ID_PRINTER = "�� �� �ţ�";
	private static final String GOODS_NAME_PRINTER = "��Ʒ���ƣ�";
	private static final String GOODS_NUM_PRINTER = "��Ʒ������";
	private static final String GOODS_USEDNUM_PRINTER = "ʹ��������";
	private static final String GOODS_URPLISNUM_PRINTER = "ʣ��������";
	private static final String GOODS_PRICE_PRINTER = "��Ʒ���ۣ�";
	private static final String GOODS_GOOD_STATUS = "��Ʒ״̬��";
	private static final String GOODS_PAY_WAY = "֧�����ͣ�";
	private static final String GOODS_FROM_WHERE = "��Ʒ��Դ��";
	private static final String TECHNICAL_SUPPORT = "           ����֧��\n    ���ݱ�������������޹�˾";

	private String tmpStr = "";
	
	public MyPrinter4ShenSi() {
		// TODO Auto-generated constructor stub
		tmpStr = "";
		openCom();
	}
	
	/**
	 * ���MyPrinter����
	 * @return
	 */
	public synchronized static MyPrinter4ShenSi getMyPrinter() {
		if(myPrinter == null)
			myPrinter = new MyPrinter4ShenSi();
		return myPrinter;
	}
	
	/**
	 * ��ӡ�վ�Ʊͷ
	 */
	private synchronized void StartTitlePrinter(int which) {
	    try {
			/* ��ʼ����ӡ�� */
		    printWrite(PrintCommand.getCmdEsc());
			/* ��ӡTitle */
		    /* ����Ŵ� */
		    printWrite(PrintCommand.getCmdW((byte)0x01));
//		    /* ����Ӵ� */
//		    printWrite(PrintCommand.getCmdN((byte)0x01));
		    if(which == Macro.WICKET)
		    	printWrite(TITLE_WICKET_PRINTER.getBytes("GB2312"));
		    else
		    	printWrite(TITLE_CHECK_PRINTER.getBytes("GB2312"));
		    /* �ָ�Ĭ������ */
		    printWrite(PrintCommand.getCmdW((byte)0x00));
//		    /* ����Ӵ� */
//		    printWrite(PrintCommand.getCmdN((byte)0x01));
		    /* ��ǰ��ֽ2�� */
		    printWrite(PrintCommand.getCmdLF());
		    printWrite(PrintCommand.getCmdLF());
		    /* �������� */
		    printWrite(SCENIC_NAME_PRINTER.getBytes("GB2312"));
		    /* ���� */
		    printWrite(PrintCommand.getCmdLF());
		    /* ��ӡ�������� */
		    printWrite(configer.getConfiger().getonfValue(configer.CMD_CONF_NAME, "").getBytes("GB2312"));
		    /* ���� */
		    printWrite(PrintCommand.getCmdLF());
//		    /* ����Ӵ� ,�ָ�*/
//		    printWrite(PrintCommand.getCmdN((byte)0x00));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ������ӡ
	 */
	private synchronized void EndPrinter() {
	    try {
	    	tmpStr = "\n================================";
			printWrite(tmpStr.getBytes("GB2312"));
			/* ���� */
		    printWrite(PrintCommand.getCmdLF());
		    /* �������� */
			printWrite(TECHNICAL_SUPPORT.getBytes("GB2312"));
			/* ���� */
		    printWrite(PrintCommand.getCmdLF());
		    /* ��ӡʱ�� */
		    tmpStr = "     " + MyFormatDateTime.getMyFormatDateTime().GetDateTime(null);
			printWrite(tmpStr.getBytes("GB2312"));
		    tmpStr = "\n\n�����������ش���˺������������";
			printWrite(tmpStr.getBytes("GB2312"));
		    /* ��ǰ��ֽ2�� */
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
	 * ��ӡ������Ϣ��IDNum==null���ڲ�ѯʱ�Ķ�����ӡ��IDNum!=null���ڼ�Ʊʱ�Ķ�����ӡ
	 * @param printerData	��Ҫ��ӡ�Ķ����ַ���
	 * @param IDNum	��Ʊ�ؼ��֣�Ҳ���������ִ�ӡ�Ķ������ͣ��������˵��
	 * @param which ʹ�û���
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
				    /* ��ӡ���֤�� */
				    printWrite(IDNum.getBytes("GB2312"));
				    /* ���� */
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
					if(tmpLName.split("��").length > 1) {
						tmpStr += GOODS_NAME_PRINTER + tmpLName.split("��")[0] + "\n";
						printWrite(tmpStr.getBytes("GB2312"));
						Thread.sleep(150);
						tmpStr = "�������㣺";
						String tmpName = tmpLName.split("��")[1].substring(0, tmpLName.split("��")[1].length()-1);
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
							tmpStr += GOODS_PAY_WAY + "������������" + "\n";
						} else {
							tmpStr += GOODS_PAY_WAY + "����֧������" + "\n";
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
								tmpStr += "                     " + Integer.toString(ii+1) + " �����ο�";
							} else {
								tmpStr += "                     " + Integer.toString(ii+1) + " �������";
							}
							printWrite(tmpStr.getBytes("GB2312"));
						}
					}
					/* �ȴ���ӡbuffer flush */
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
		return (str0 + "��\n");
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
