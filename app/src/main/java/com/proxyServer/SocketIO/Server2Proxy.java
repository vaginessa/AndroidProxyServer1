package com.proxyServer.SocketIO;

import android.util.Log;

import com.cfun.proxy.Service.ProxyService;
import com.proxyServer.HttpProxy.HttpConnection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Server2Proxy extends Thread {
	private int currentId = 0;
	private String TAG = "Server2Proxy_";
	//	private HttpClient2Server Brother;
	private byte[] buffer = new byte[20480];
	private HttpConnection conn = null;
	private BufferedOutputStream oStream;
	private BufferedInputStream iStream;

	public Server2Proxy(HttpConnection conn,int currentId) {
		this.conn = conn;
		this.currentId = currentId;
		TAG = TAG+String.valueOf(currentId);
		setPriority(Thread.MIN_PRIORITY);
		oStream = conn.getClientOUT();
		iStream = conn.getServerIN();
	}

	public void run() {
		synchronized (ProxyService.workingThread) {
			ProxyService.workingThread++;
		}
		try {
			Log.d(TAG, "真正开始传到代理服务器,并且返回oStream");
			int byteRead;
			String serverInput = convertStreamToString(iStream);
			Log.w(TAG,"服务器真正返回什么:"+serverInput);
			while ((byteRead = iStream.read(buffer)) > 0) {
				oStream.write(buffer, 0, byteRead);
				oStream.flush();
			}

		} catch (Exception e) {
			Log.e(TAG,"error",e.fillInStackTrace());
		} finally {
			synchronized (ProxyService.workingThread) {
				ProxyService.workingThread--;
			}
			Log.d(TAG, "关掉");
			conn.allClose(); //既然服务器已无数据返回，那客户端的生存没有意义，所以全部关闭
		}

	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return sb.toString();
	}
}
