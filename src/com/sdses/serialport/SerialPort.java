/** 
* All rights Reserved, Designed By SDSES
* @Title: SerialPort.java 
* @Package com.sdses.serialport 
* @Description: 串口声明类，具体实现在native C库中
* @author: 孟强
* @date: 2013-4-23 下午2:12:51 
* @version V1.0
*/ 

package com.sdses.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import android.util.Log;

/**
 * @author meng
 *
 */
public class SerialPort {
	private static final String TAG = "SerialPort";
	static {
		try {
			System.loadLibrary("serial_port");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;

	public SerialPort(File device, int baudrate,int flags) throws SecurityException, IOException {
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su = null;  
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
						+ "exit\n";  
				su = Runtime.getRuntime().exec("/system/bin/su");
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
					Log.w(TAG, "串口设备仍然不可读写");
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.w(TAG, "异常信息:"+e);
				throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate,flags);
		if (mFd == null) {
			throw new IOException();
		}
	}

	// JNI
	public native static int PowerOn();
	public native static int PowerOff();
	private native static FileDescriptor open(String path, int baudrate,int flags);
	public native int close();
	public native int oneCommand(byte[] pbuff,int sentLen, int recvLen,int timeout);

}
