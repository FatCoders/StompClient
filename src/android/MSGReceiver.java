package com.fatcoder.cordova.stomp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.haostay.mobile.schools.*;

import org.json.JSONObject;


public class MSGReceiver extends BroadcastReceiver {
	@Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(context.getPackageName()+".MSG")) {
        	try {
              Bundle bundle = intent.getExtras();
          	if (bundle != null){
                if(isRunningForeground(context)){
                	//应用再打开的时候
//                	processAPPInsideMessage(context, bundle);
                }else{
                     JSONObject msg = new JSONObject(bundle.getString("msg"));
                     String alert= msg.getString("alert");
          		 Intent resultIntent = new Intent(context, MainActivity.class);
                 resultIntent.putExtras(bundle);
                 PendingIntent pendingIntent =
                     PendingIntent.getActivity(context, 0, resultIntent,
                         PendingIntent.FLAG_UPDATE_CURRENT);
                 NotificationCompat.Builder mBuilder =
                     new NotificationCompat.Builder(context)
                         .setSmallIcon(R.mipmap.icon)
                         .setContentTitle(
                                 context.getResources().getString(R.string.app_name))
                         .setContentText(alert)
                         .setDefaults(Notification.DEFAULT_ALL)
                         .setTicker(alert);
                 mBuilder.setContentIntent(pendingIntent);
                 mBuilder.setAutoCancel(true);
         
                 int mNotificationId = 58883;
                 NotificationManager mNotifyMgr =
                     (NotificationManager) context
                         .getSystemService(
                             Context.NOTIFICATION_SERVICE);
                 mNotifyMgr.notify(mNotificationId, mBuilder.build());

                	}   
                } 		
          	} catch (Exception e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
  		
        }
      }
	
    private void processAPPInsideMessage(Context context, Bundle bundle) {
//        Intent mIntent=new Intent(MainActivity.ACTION_INSIDE_RECEIVER);
//        mIntent.putExtras(bundle);
//        context.sendBroadcast(mIntent);
    }
    
    @SuppressWarnings("deprecation")
	private boolean isRunningForeground (Context context)  
    // 每次APP唤醒 
    {  
    	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
        String currentPackageName = cn.getPackageName();  
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())&&pm.isScreenOn())  
        {  
            return true ;  
        }  
       
        return false ;  
    }  

}