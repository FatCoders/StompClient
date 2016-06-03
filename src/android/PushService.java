package com.fatcoder.cordova.stomp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PushService extends Service {
	private Client mClient=null;

	@Override
	public void onCreate() {
		super.onCreate();
	Log.i("xiaof", "onCreate()");
		mClient=new Client(getApplicationContext());
	}
	@Override
	public void onDestroy() {
		Log.i("xiaof", "onDestroy()");
		// TODO Auto-generated method stub
		mClient.close();
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("xiaof", "onStartCommand()");
		handleCommand(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void handleCommand(Intent intent)
	{
		if(NetworkUtil.isNetworkAvailable(getApplicationContext()))
		{
			Log.i("xiaof", "isNetworkAvailable()");
			mClient.open();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i("xiaof", "onBind()");
		if(PushService.class.getName().equals(arg0.getAction()))
		{
			handleCommand(arg0);
		}
		return null;
	}

}
