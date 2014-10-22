//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package com.alibaba.cn.idcard;
/**
 * For XiaoBing
 * @author Yongkun
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class ReadCardInfo
{
	private static final String TAG = "ReadCardInfo";
	//寻卡所需数据
	public final static byte[] FIND_CARD_BYTE_DATA = 
		{ 
			(byte) 0xaa, (byte) 0xaa, (byte) 0xaa, 
			(byte) 0x96, (byte) 0x69, (byte) 0x00,
			(byte) 0x03,( byte) 0x20, (byte) 0x01, 
			(byte) 0x22
		};
	
	//选卡所需数据
	public final static byte[] CHOOSE_CARD_BYTE_DATA = 
		{
			(byte) 0xaa, (byte) 0xaa, (byte) 0xaa, 
			(byte) 0x96, (byte) 0x69, (byte) 0x00,
			(byte) 0x03,( byte) 0x20, (byte) 0x02, 
			(byte) 0x21
		};
			
	//读卡所需数据
	public final static byte[] READ_CARD_BYTE_DATA = 
		{
			(byte) 0xaa, (byte) 0xaa, (byte) 0xaa, 
			(byte) 0x96, (byte) 0x69, (byte) 0x00, 
			(byte) 0x03,( byte) 0x30, (byte) 0x01, 
			(byte) 0x32
		};
	
	//寻卡时，返回byte数组的数据长度
	public final static int FIND_CARD_RETURN_DATA_LENGTH = 15;
	
	//选卡时，返回byte数组的数据长度
	public final static int CHOOSE_CARD_REETURN_DATA_LENGTH = 19;
	
	//读卡时，返回byte数组的idManager数据长度
	public final static int READ_CARD_RETURN_DATA_LENGTH = 1295;
	
	private OutputStream os;
	private InputStream in;
	
	public ReadCardInfo(OutputStream os,InputStream in)
	{
		this.os = os;
		this.in = in;
	}
	
	/**
	 * 寻卡
	 * @return 寻卡返回的byte数据,这里返回AA AA AA 96 69 00 08 00 00 9F 00 00 00 00 97
	 * @throws IOException
	 * @throws ReadIDCardException 
	 */
	public byte[] findCard()
			throws IOException, ReadIDCardException
	{
		return findCard(FIND_CARD_BYTE_DATA);
	}
	
	/**
	 * 选卡
	 * @return 选卡返回的byte数据,这里返回AA AA AA 96 69 00 0C 00 00 90 00 00 00 00 00 00 00 00 9C
	 * @throws IOException
	 * @throws ReadIDCardException 
	 */
	public byte[] chooseCard()
			throws IOException, ReadIDCardException
	{
		return chooseCard(CHOOSE_CARD_BYTE_DATA);
	}
	
	/**
	 * 读卡
	 * @return 读卡，返回1295字节的数据.
	 * @throws IOException 
	 * @throws ReadIDCardException 
	 */
	public byte[] readCard()
			throws IOException, ReadIDCardException
	{
		return readCard(READ_CARD_BYTE_DATA);
	}
	
	private byte[] findCard(byte[] cmdByte)
		throws IOException, ReadIDCardException
	{
		return readCardInfo(cmdByte,FIND_CARD_RETURN_DATA_LENGTH);
	}
	
	private byte[] chooseCard(byte[] cmdByte)
		throws IOException, ReadIDCardException
	{
		return readCardInfo(cmdByte,CHOOSE_CARD_REETURN_DATA_LENGTH);
	}
	private byte[] readCard(byte[] cmdByte)
			throws IOException, ReadIDCardException
	{
		return readCardInfo(cmdByte,READ_CARD_RETURN_DATA_LENGTH);
	}
	
	private byte[] readCardInfo(byte[] cmdByte,int returnByteLength)
		throws IOException, ReadIDCardException
	{
		byte[] data = new byte[returnByteLength];
		
		os.write(cmdByte);
		
		int sleepTime = 1000;
		switch(returnByteLength)
		{
			case FIND_CARD_RETURN_DATA_LENGTH :
				sleepTime = 5;
				break;
			case CHOOSE_CARD_REETURN_DATA_LENGTH :
				sleepTime = 5;
				break;
			case READ_CARD_RETURN_DATA_LENGTH :
				sleepTime = 700; //这里必须延迟650ms才能把1295字节数据读取完整
				break;
		}
		try
		{
			Thread.sleep(sleepTime);
		} catch (InterruptedException e)
		{
			Log.e(TAG, e.toString());
		}
		
		if(in.read(data)!=returnByteLength)//如果读取的数据不完整
		{
			throw new ReadIDCardException("数据读取不完整，请拿起身份证重刷！！！");
		}
		
		return data;
	}
	
	public void closeStream() 
			throws IOException
	{
		if(os!=null)
			os.close();
		if(in!=null)
			in.close();
		
	}
	
	public void setOs(OutputStream os)
	{
		this.os = os;
	}

	public void setIn(InputStream in)
	{
		this.in = in;
	}
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////
