package com.proxyServer.SocketIO;

import android.util.Log;

import com.proxyServer.Exception.FirstLineFormatErrorExpection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class HttpFirstLine
{
    public static final String TAG = "httpFirstLine";
	public String Method= "";
	public String Host= "";
	public String Uri= "";
	public String Version= "HTTP/1.1";
	public byte[] HP=null;
	public int Port=80;


	public boolean isSSL= false;

	public HttpFirstLine(String httpFirstLine) throws IOException, FirstLineFormatErrorExpection
	{
		String[] strArray= httpFirstLine.trim().split(" ");
		if(strArray.length!=3) { throw new FirstLineFormatErrorExpection(httpFirstLine);}
		//0 即为方法体
		this.Method= strArray[0];
		//2 即为版本信息
		this.Version= strArray[2];
		//包含了uri 可能包含host

		if(Method.toUpperCase(Locale.ENGLISH).equals("CONNECT"))
		{
			isSSL= true;
			parstHost(strArray[1]);
			return;
		}
		if(strArray[1].toLowerCase(Locale.ENGLISH).startsWith("http://"))
		{
			String str= strArray[1].substring(7);
			int index= str.indexOf('/');
			parstHost(str.substring(0, index));
			Uri= str.substring(index);
		}
		else
		{
			int index= strArray[1].indexOf('/');
			parstHost(strArray[1].substring(0, index));
			Uri= strArray[1].substring(index);
		}

		Log.d(TAG,"当前请求的地址为:"+Uri+" 是否是ssl:"+isSSL+" 方法体:"+this.Method +" 版本信息:"+this.Version+" Host:"+Host+" port:"+Port);
	}


	public void parstHost(String H) throws UnsupportedEncodingException
	{
		if(H.startsWith("10.0.0.172"))
			return;
		int index = H.indexOf(':');
		if(index>0)
		{
			Host = H.substring(0,index);
			Port = Integer.parseInt(H.substring(index+1));
		}
		else
		{
			Host = H;
			Port = isSSL ? 443 : 80;
		}
		HP   = H.getBytes("iso8859-1");
	}

}
