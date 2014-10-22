//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package com.alibaba.cn.idcard;
/**
 * For XiaoBing
 * @author Yongkun
 *
 */
public class ReadIDCardException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public ReadIDCardException()
	{}
	
	public ReadIDCardException(String msg)
	{
		super(msg);
	}
	
	public ReadIDCardException(String msg,Throwable e)
	{
		super(msg,e);
	}
	
	public ReadIDCardException(Throwable e)
	{
		super(e);
	}
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////
