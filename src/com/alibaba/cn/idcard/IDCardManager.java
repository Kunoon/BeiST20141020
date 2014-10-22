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
import java.io.UnsupportedEncodingException;

import com.alibaba.cn.idcard.entity.IDCardInfo;
import com.alibaba.cn.idcard.parse.ParseIDCard;

public class IDCardManager
{
	private InputStream in;
	private OutputStream os;

	public IDCardManager(OutputStream os,InputStream in)
	{
		this.in = in;
		this.os = os;
	}
	
	public boolean init() 
	{
		boolean flag = false;
		initObj();
		try
		{
			initDevice();
			flag = true;
		} catch (ReadIDCardException e)
		{
			flag = false;
		}
		catch (IOException e)
		{
			flag = false;
		}
		return flag;
	}
	
	private ReadCardInfo cardInfo;
	private ParseIDCard parseIdCard;
	//初始化需要使用到的对象
	private void initObj()
	{
		if(cardInfo==null)
		{
			cardInfo = new ReadCardInfo(os, in);
		}
		else
		{
			cardInfo.setIn(in);
			cardInfo.setOs(os);
		}
	}
	
	//初始化身份证卡
	private void initDevice() 
			throws IOException, ReadIDCardException
	{
		cardInfo.findCard();//寻卡
		cardInfo.chooseCard();//选卡
	
	}
	
	//解析从身份证中读取到的数据并返回
	//如果读取到数据返回ParseIDCard实例，否则返回null
	public IDCardInfo parseIDCardData() 
	{
		try{
			if(parseIdCard==null)
			{
				parseIdCard = new ParseIDCard(cardInfo.readCard());
			}
			else
			{
				parseIdCard.setCardData(cardInfo.readCard());
			}
			return parseIdCard.getIdCard();
		}
		catch (UnsupportedEncodingException e){} 
		catch (IOException e){} 
		catch (ReadIDCardException e){}
		
		return null;
	}
	
	public void closeStream()
	{
		try
		{
			cardInfo.closeStream();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////

