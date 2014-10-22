
package com.example.pc700demo;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class SerialPort {

	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method
	 * close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	public SerialPort(String device, int baudrate, int flags)
			throws SecurityException, IOException {

		/* Check access permission */  
		String cmd = "chmod 777 " + device + "\n"
				+ "exit\n";
		if(RootCommand(cmd))
		{
			System.out.println("ok");
		}else
		{
			System.out.println("no");
		}
//		if (!device.canRead() || !device.canWrite()) {
//			try {
//				/* Missing read/write permission, trying to chmod the file */
//
//				Process su;
//				su = Runtime.getRuntime().exec("su");
//				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
//						+ "exit\n";
//
//				su.getOutputStream().write(cmd.getBytes());
//
//				if ((su.waitFor() != 0) || !device.canRead()
//						|| !device.canWrite()) {
//					// System.out.println("path:"+device.getAbsolutePath()+":baudrate:"+baudrate+" :flags:"+flags);
//					throw new SecurityException();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new SecurityException();
//			}
//		}

//		close();
		mFd = open(device, baudrate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
	private native static FileDescriptor open(String path, int baudrate, int flags);

	public native void close();
	
    public native void openpower();
    
    public native void closepower();

	static {
		System.loadLibrary("PC700Demo");
	}
}