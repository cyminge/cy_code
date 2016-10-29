package com.cy.network.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.util.Log;

import com.cy.string.StringUtils;

public class AsyncTcpPipe {
	/* tcp请求线程池 */
	private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	private String host = "";
	private short port = 80;
	private Socket socket = null;
	private Handler handler = null;
	private boolean handlerRenew = false;

	public AsyncTcpPipe(String host, short port) {
		this.host = host;
		this.port = port;
	}

	class SendRunnable implements Runnable {
		String sendStr;
		AsyncTcpListener listener;

		public SendRunnable(String sendStr, AsyncTcpListener listener) {
			this.sendStr = sendStr;
			this.listener = listener;
		}

		private void transactionDone(String recvStr) {
//			if (handlerRenew) {
//				this.handler.getLooper().quit();
//			}
			if (listener != null) {
				listener.onReceive(recvStr);
			}
		}

		@Override
		public void run() {

			String recvStr = "";

			/* 无数据等待发送 */
			if (null == sendStr) {
				return;
			}
			/* socket 未初始化 */
			if (null == socket) {
				try {
					socket = new Socket(host, port);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			PrintWriter writer = null;
			InputStream is = null;
			/* 若 socket 初始化失败 */
			if (null != socket) {
				try {
					writer = new PrintWriter(socket.getOutputStream(), true);
					writer.write(sendStr);
					writer.flush();
					is = socket.getInputStream();
					byte[] bytes = new byte[is.available()];
					is.read(bytes);

					recvStr = new String(bytes);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						writer.close();
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					transactionDone(recvStr);
				}
			}
		}
	}

	public int send(final String str, final AsyncTcpListener listener) {
		/* 发送数据部位空 */
		if (StringUtils.isEmpty(str)) {
			return -1;
		}
//		/* 当前thread没有开启looper */
//		if (Looper.myLooper() == null) {
//			Looper.prepare();
//			handlerRenew = true;
//		}
//		/* 创建handler */
//		this.handler = new Handler();
//		if (handlerRenew) {
//			Looper.loop();
//		}
		cachedThreadPool.execute(new SendRunnable(str, listener));
		return 0;
	}

	public static void test() {
		for (int i = 0; i < 100; i++) { 
			AsyncTcpPipe pipe = new AsyncTcpPipe("192.168.0.226", (short) 9999);
			pipe.send("abcdefghijklmnopqrstuvwxyz\n", new AsyncTcpListener() {
				@Override
				public void onReceive(String msg) {
					Log.i("AsyncTcpPipe", msg);
				}
			});
		}
	}
}
