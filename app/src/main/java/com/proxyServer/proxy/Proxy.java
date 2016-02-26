package com.proxyServer.proxy;

import android.util.Log;

import com.proxyServer.HttpProxy.HttpSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy extends Thread {
	private static final String TAG = "proxy";
	private ServerSocket serverSocket;

	private int currentId = -1;

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = null;
				currentId++;
				socket = serverSocket.accept();
				Log.d(TAG, "proxy监听到socket");
				new HttpSession(socket, currentId);

			}
		} catch (Exception e) {
		}
	}

	public void BindPort() throws IOException {
		serverSocket = new ServerSocket(10080);
	}

	public void RelasePort() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
				Log.d(TAG, "proxy关闭");
			} catch (IOException e) {
			}
		}
	}

}