//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package com.wizarpos.drivertest.jniinterface;
/**
 * For XiaoBing
 * @author Yongkun
 *
 */
import android.util.Log;

public class PrinterInterface 
{
	/**
	 * open the device
	 * @return value  >= 0, success in starting the process; value < 0, error code
	 * */
	public native static int PrinterOpen();
	/**
	 * close the device
	 * @return value  >= 0, success in starting the process; value < 0, error code
	 * */
	
	public native static int PrinterClose();
	/**
	 * prepare to print
	 * @return value  >= 0, success in starting the process; value < 0, error code
	 * */
	
	public native static int PrinterBegin();
	/** end to print
	 *  @return value  >= 0, success in starting the process; value < 0, error code
	 * */
	
	public native static int PrinterEnd();
	/**
	 * write the data to the device
	 * @param arryData : data or control command
	 * @param nDataLength : length of data or control command
	 * @return value  >= 0, success in starting the process; value < 0, error code
	 * */
	
	public native static int PrinterWrite(byte arryData[], int nDataLength);
	
	static {
        try {
        	System.loadLibrary("wizarposHAL");
        	System.loadLibrary("wizarpos_printer");
        } catch (UnsatisfiedLinkError e) {
            Log.d("wizarposHAL and wizarpos_printer", "libwizarposHAL and libwizarpos_printer library not found!");
        }
    }
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////
