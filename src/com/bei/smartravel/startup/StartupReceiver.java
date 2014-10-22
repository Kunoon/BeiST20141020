package com.bei.smartravel.startup;

import com.bei.smartravel.wicket.BeiSTWicket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 实现开机自启程序
 * @author YongKun
 */
public class StartupReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
    		Intent myIntent = new Intent(context, BeiSTWicket.class);
    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //将intent以startActivity传送给操作系统
            context.startActivity(myIntent);
        }
        
//        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
//    		Intent myIntent = new Intent(context, BeiSTWicket.class);
//    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //将intent以startActivity传送给操作系统
//            context.startActivity(myIntent);
//        }
        
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
    		Intent myIntent = new Intent(context, BeiSTWicket.class);
    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            //将intent以startActivity传送给操作系统  
            context.startActivity(myIntent); 
        }
        
        if (intent.getAction().equals("android.intent.action.PACKAGE_CHANGED")) {
    		Intent myIntent = new Intent(context, BeiSTWicket.class);  
    		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            //将intent以startActivity传送给操作系统  
            context.startActivity(myIntent); 
        }
	}
}
