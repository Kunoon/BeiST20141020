//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package com.alibaba.cn.idcard.parse;
/**
 * For XiaoBing
 * @author Yongkun
 *
 */
import java.io.UnsupportedEncodingException;

import com.alibaba.cn.idcard.ReadIDCardException;
import com.alibaba.cn.idcard.entity.IDCardInfo;

public class ParseIDCard
{
	private IDCardInfo idCard;
	
	private byte[] cardData;//身份证信息
	private byte[] usernameData;//身份证中的用户信息
	
	public byte[] getUsernameData()
	{
		return usernameData;
	}

	private final static int HEAD_INFO_LENGTH = 14;
	private final static int USER_INFO_LENGTH = 256;
	private final static int IMAGE_INFO_LENGTH = 1024;
	private final static int CRC_INFO_LENGTH = 1;
	
	public ParseIDCard()
	{}
	
	public ParseIDCard(byte[] cardData) 
			throws UnsupportedEncodingException, ReadIDCardException
	{
		this.cardData = cardData;
		
		init();
		
	}
	
	public void setCardData(byte[] cardData) 
			throws UnsupportedEncodingException, ReadIDCardException
	{
		this.cardData = cardData;
		init();
	}

	private void init() 
			throws UnsupportedEncodingException, ReadIDCardException
	{
		if(idCard==null)
			idCard = new IDCardInfo();
		
		parseUsernameInfoByteData();//首先需要解析出身份在中的用户信息byte
		loadIDCardInfo();
	}
	
	private void loadIDCardInfo() 
			throws UnsupportedEncodingException, ReadIDCardException 
	{
		
		try
		{
			idCard.setSex(DecodeInfo.decodeSex(Integer.valueOf(new String(parseSex(),"UnicodeLittleUnmarked"))));
			idCard.setNation(DecodeInfo.decodeNation(Integer.valueOf(new String(parseNation(),"UnicodeLittleUnmarked"))));
		}
		catch(NumberFormatException e)
		{
			throw new ReadIDCardException("读取用户身份证数据错误!");
		}
		
		idCard.setName(new String(parseName(),"UnicodeLittleUnmarked"));
		idCard.setBirthday(new String(parseBirthday(),"UnicodeLittleUnmarked"));
		idCard.setAddress(new String(parseAddress(),"UnicodeLittleUnmarked"));
		idCard.setIdNumber(new String(parseIDNumber(),"UnicodeLittleUnmarked"));
		idCard.setSign(new String(parseSign(),"UnicodeLittleUnmarked"));
		idCard.setStartDate(new String(parseStartDate(),"UnicodeLittleUnmarked"));
		idCard.setEndDate(new String(parseEndDate(),"UnicodeLittleUnmarked"));
		idCard.setNewAddress(new String(parseNewAddress(),"UnicodeLittleUnmarked"));
		idCard.setImageByte(parseImageByteData());
		idCard.setCrcByte(parseCRCByteDate());
	}
	
	
	//解析身份证中的用户信息
	private void parseUsernameInfoByteData()
	{
		if(usernameData==null)
			usernameData = new byte[USER_INFO_LENGTH];
		
		for(int i=HEAD_INFO_LENGTH;
				 i<HEAD_INFO_LENGTH+USER_INFO_LENGTH;
				 i++)
		{
			usernameData[i-HEAD_INFO_LENGTH] = cardData[i];
		}
	}
	
	//解析身份证中的图片信息
	private byte[] parseImageByteData()
	{
		byte[] imageData = new byte[IMAGE_INFO_LENGTH];
		for(int i=HEAD_INFO_LENGTH+USER_INFO_LENGTH;
				 i<HEAD_INFO_LENGTH+USER_INFO_LENGTH+IMAGE_INFO_LENGTH;
				i++)
		{
			imageData[i-HEAD_INFO_LENGTH-USER_INFO_LENGTH] = cardData[i];
		}
		
		return imageData;
	}
	
	//解析身份证中的CRC信息
	private byte parseCRCByteDate()
	{
		return cardData[HEAD_INFO_LENGTH+USER_INFO_LENGTH+IMAGE_INFO_LENGTH+CRC_INFO_LENGTH -1];
	}
	
	//解析用户信息中的用户名byte
	private byte[] parseName()
	{
		byte[] nameByte = new byte[30];
		for(int i=0;i<30;i++)
		{
			nameByte[i] = usernameData[i];
		}
		
		return nameByte;
	}
	
	//解析用户信息中的性别byte
	private byte[] parseSex()
	{
		byte[] sexByte = new byte[2];
		for(int i=30;i<32;i++)
		{
			sexByte[i-30]  = usernameData[i];
		}
		return sexByte;
	}
	
	//解析用户信息中的民族byte
	private byte[] parseNation()
	{
		byte[] nationByte = new byte[4];
		for(int i=32;i<36;i++)
		{
			nationByte[i-32] = usernameData[i];
		}
		
		return nationByte;
	}
	
	//解析用户信息中的出生byte
	private byte[] parseBirthday()
	{
		byte[] birthdayByte = new byte[16];
		for(int i=36;i<52;i++)
		{
			birthdayByte[i-36]  = usernameData[i];
		}
		
		return birthdayByte;
	}
	
	//解析用户信息中的地址byte
	private byte[] parseAddress()
	{
		byte[] addressByte = new byte[70];
		for(int i=52;i<122;i++)
		{
			addressByte[i-52]  = usernameData[i];
		}
		
		return addressByte;
	}
	
	//解析用户信息中的身份证号码byte
	private byte[] parseIDNumber()
	{
		byte[] idByte = new byte[36];
		for(int i=122;i<158;i++)
		{
			idByte[i-122] = usernameData[i];
		}
		
		return idByte;
	}
	
	//解析用户信息中的签发机关byte
	private byte[] parseSign()
	{
		byte[] signByte = new byte[30];
		for(int i=158;i<188;i++)
		{
			signByte[i-158] = usernameData[i];
		}
		
		return signByte;
	}
	
	//解析用户信息中的有效期起始日期byte
	private byte[] parseStartDate()
	{
		byte[] startDateByte = new byte[16];
		for(int i=188;i<204;i++)
		{
			startDateByte[i-188] = usernameData[i];
		}
		
		return startDateByte;
	}
		
	//解析用户信息中的有效期截止日期byte
	private byte[] parseEndDate()
	{
		byte[] endDateByte = new byte[16];
		for(int i=204;i<220;i++)
		{
			endDateByte[i-204] = usernameData[i];
		}
		
		return endDateByte;
	}
	
	//解析用户信息中的最新住址byte
	private byte[] parseNewAddress()
	{
		byte[] newAddressByte = new byte[36];
		for(int i=220;i<256;i++)
		{
			newAddressByte[i-220] = usernameData[i];
		}
		return newAddressByte;
	}
	
	//获取身份证中byte信息对象
	public IDCardInfo getIdCard()
	{
		return idCard;
	}
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////