package com.fatcoder.cordova.stomp;

import android.content.Context;

public interface StompMessageListener {

	void onReadHeader(String newMessage);

	void onReadMessage(Context context, String newMessage);

}
