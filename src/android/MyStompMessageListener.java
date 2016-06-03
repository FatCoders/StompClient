package com.fatcoder.cordova.stomp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyStompMessageListener implements StompMessageListener {
	Bundle mBundle = new Bundle(); 
	@Override
	public void onReadHeader(String newMessage) {
		// TODO Auto-generated method stub
//		Log.i("xiaof", newMessage);
	}

	@Override
	public void onReadMessage(Context context,String newMessage) {
		Log.i("xiaof", newMessage);
		mBundle.clear();
		mBundle.putString("msg", newMessage);
		Log.i("xiaof", context.getPackageName());
		Log.i("xiaof", context.getPackageName());
		Log.i("xiaof", "2222");
        Intent mIntent=new Intent(context.getPackageName()+".MSG");
        mIntent.putExtras(mBundle);
        context.sendBroadcast(mIntent);
	}

}

