package com.fatcoder.cordova.stomp;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("out", "-----------onReceive-----------");
		if(!isProcessRunning(context)){
			Log.i("out", "---------------onReceive 	isPushServiceRunning(xxxxx)---------------");
			Intent mIntent = new Intent();
			mIntent.setAction("com.fatcoder.cordova.stomp.PushService");//你定义的service的action
			mIntent.setPackage(context.getPackageName());//这里你需要设置你应用的包名
			context.startService(mIntent);
//			context.startService(new Intent(PushService.class.getName()));
		}else{
			Log.i("out", "---------------onReceive 	isPushServiceRunning(TRUE)---------------a");
		}
	}
	
	private  boolean isProcessRunning(Context context) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningAppProcessInfo service : manager.getRunningAppProcesses()) {
	        if ((context.getPackageName()+".push").equals(service.processName)) {
	            return true;
	        }
	    }
	    return false;
	}
}