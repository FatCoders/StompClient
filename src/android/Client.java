package com.fatcoder.cordova.stomp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class Client {
	private final int STATE_OPEN = 1;// socket打开1
	private final int STATE_CLOSE = 1 << 1;// socket关闭2
	private final int STATE_CONNECT_START = 1 << 2;// 开始连接server4
	private final int STATE_CONNECT_SUCCESS = 1 << 3;// 连接成功8
	private final int STATE_CONNECT_FAILED = 1 << 4;// 连接失败16
	private final int STATE_CONNECT_WAIT = 1 << 5;// 等待连接

	private int state = STATE_CONNECT_START;

	private Thread conn = null;
	
	SharedPreferences SharedPref;
	SharedPreferences.Editor editor;

	
	private Context context;
    StompCommand stompClient= new StompCommand();
	MyStompMessageListener myListener=new MyStompMessageListener();
	private long lastConnTime = 0;
	
	public Client(Context context) {
		this.context = context;
		SharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);;
	}
	public synchronized void open() {
		reconn();
	}
	
	public synchronized void reconn() {
		if (System.currentTimeMillis() - lastConnTime < 2000) {
			Log.i("xiaof","System.currentTimeMillis()");
			return;
		}
		lastConnTime = System.currentTimeMillis();

		close();
		state=STATE_OPEN;
		conn = new Thread(new Conn());
		conn.start();
	}
	
	public synchronized void close() {
		Log.i("xiaof","close()");
		try {
			if (state != STATE_CLOSE) {
				try {
					if (null != conn && conn.isAlive()) {
						Log.i("xiaof","conn.interrupt()");
						conn.interrupt();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Log.i("xiaof","conn = null");
					conn = null;
				}
				Log.i("xiaof","state = STATE_CLOSE");
				state = STATE_CLOSE;
				stompClient.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isClientConnected() {
		return (state == STATE_CONNECT_SUCCESS)
				&& (null != conn && conn.isAlive());
	}
	
	private class Conn implements Runnable {
		public void run() {
			Log.i("xiaof", "run()");
			try {
				while(state!=STATE_CLOSE)
				{
					try {

						state=STATE_CONNECT_START;
						String sSubject=SharedPref.getString("subject", null);
						stompClient.newCommand();
						
						stompClient.setHost(SharedPref.getString("host", "haostay.com"), Integer.parseInt(SharedPref.getString("port", "61613")));
				        stompClient.setHeader("login", SharedPref.getString("login", "haostay"));
				        stompClient.setHeader("passcode", SharedPref.getString("passcode", "haostay1"));
						stompClient.connect();
						Log.i("xiaof", "connect()");
						if(!(sSubject==null)){
							 String[] aSubject = sSubject.split(",");
							 for(int i=0;i<aSubject.length;i++){
								 stompClient.setHeader("destination", aSubject[i]); 
								 stompClient.subject();
							 }
						}
				        state=STATE_CONNECT_SUCCESS;
						stompClient.read(context,myListener);
					} catch (Exception e) {
						e.printStackTrace();
						state=STATE_CONNECT_FAILED;
						stompClient.disconnect();
					}
					if(state!=STATE_CONNECT_SUCCESS)
					{		
						state=STATE_CONNECT_WAIT;
						//如果有网络没有连接上，则定时取连接，没有网络则直接退出
						if(NetworkUtil.isNetworkAvailable(context))
						{
							try {
									Thread.sleep((Integer.parseInt(SharedPref.getString("isNetworkAvailableSleepTime", "1")))*60*1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
								break;
							}
						}
						else
						{
							try {
								Thread.sleep((Integer.parseInt(SharedPref.getString("isNetworkNotAvailableSleepTime", "60")))*60*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							break;
						}
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

			
		}
	}
}
