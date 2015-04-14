package com.fatcoder.cordova.stomp;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;

import com.gigamore.libs.service.PushService;

public class StompClient extends  CordovaPlugin  {

	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		if (action.equalsIgnoreCase("reload")) {
			
			try {
				cordova.getActivity().startService(new Intent(PushService.class.getName()));
					return true;
			} catch (Exception e) {
				callbackContext.error("Could Not start service " + e.getMessage());
				return false;
			}

		}else {
			callbackContext.error("error in method");
			return false;
		}
	}
	
}
