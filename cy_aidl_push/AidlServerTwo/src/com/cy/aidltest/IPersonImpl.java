package com.cy.aidltest;

import android.os.RemoteException;
import android.util.Log;

import com.cy.aidltest.ITest.Stub;

/**
 * 暴露给客户端程序的数据
 * @author zhanmin
 *
 */
public class IPersonImpl extends Stub {

	private int age;
	private String name;
	
	@Override
	public String display() throws RemoteException {
		return "name = "+"敏哥"+" ...... age = "+30;
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.name = name;
		
	}

	@Override
	public void setAge(int age, String[] str) throws RemoteException {
		Log.i("cyTest", "server.str="+str[0]);
		str[0]="abcd";
	}

	@Override
	public void testCallback(IPersonCallback callback) throws RemoteException {
	    if(null != callback) {
	        String info = callback.getCYmingeInfo();
	        Log.i("cyTest", "info =" + info);
	    }
		
	}

}
