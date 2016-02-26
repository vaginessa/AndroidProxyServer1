package com.proxyServer.HttpProxy;

import android.util.Log;

import com.cfun.proxy.Service.ProxyService;
import com.proxyServer.SocketIO.Client2Proxy;

import java.io.IOException;
import java.net.Socket;





public class HttpSession extends Thread
{
	private int currentId;
	private String TAG = "httpSession_";
	private HttpConnection conn= null;

	public HttpSession(Socket clientSocket,int currentId) throws IOException
	{
		this.currentId = currentId;
		TAG=TAG+String.valueOf(currentId);
		conn= new HttpConnection();
		conn.setNewClient(clientSocket);
		this.setName("C2S");
		start();
	}

	public void run()
	{
		synchronized (ProxyService.workingThread)
		{
			ProxyService.workingThread ++;
		}
		try
		{
			Log.d(TAG,"当前有"+ProxyService.workingThread+"个工作线程");
			Log.d(TAG,"开启客户端代理");
			new Client2Proxy(conn,currentId).doRequest();
		} catch (IOException e)
		{
		}
		finally
		{
			synchronized (ProxyService.workingThread)
			{
				ProxyService.workingThread --;
			}
		}
	}
}
