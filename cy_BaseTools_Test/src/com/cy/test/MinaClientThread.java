package com.cy.test;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.os.HandlerThread;

import com.cy.tracer.Tracer;

public class MinaClientThread extends HandlerThread implements IoHandler {
	
	private static final String TAG = "MinaClientThread";
	/* 线程名字 */
	private static final String MINA_THREAD_NAME = "mina-client";
	private Context mContext; 
	private InetSocketAddress mInetSocketAddress; // 连接地址
	private String mTypeName; // 网络类型：tcp/udp
	private String mIpAddr;
	private int mIpPort;
	private NioSocketConnector mTcpConnector;
	private NioDatagramConnector mUdpConnector; // udp连接
	
	private static ProtoBufCodec protocolFilter = null;
	
	private NetState mNetState;
	
	public static final String CONNECT_TYPE_NAME_TCP = "tcp";
	public static final String CONNECT_TYPE_NAME_UDP = "udp";

	public MinaClientThread(Context context, InetSocketAddress address, String typeName) {
		super(MINA_THREAD_NAME);
		mContext = context.getApplicationContext();
		mInetSocketAddress = address;
		mTypeName = typeName;
		
		setConnectAttributes();
		
//		keepAliveFilter = new PTTKeepAliveTransmittor(new PTTKeepAliveFactory(context),
//				mIdleStatus, context,
//				HEART_INTERVAL, HEART_TIMEOUT, this);

		protocolFilter = new ProtoBufCodec();
		addCodecFilter(protocolFilter); // 设置传输协议
		setNetState(NetState.BROKEN);
	}
	
	/**
	 * 添加业务层编解码过滤器，同步操作，需在connect前调用
	 * 
	 * @param filter 过滤器
	 */
	public void addCodecFilter(IoFilter filter) {
		if(mTypeName.equals(CONNECT_TYPE_NAME_TCP)) {
			mTcpConnector.getFilterChain().addFirst("codec", filter);
		} else {
			mUdpConnector.getFilterChain().addFirst("codec", filter);
		}
	}
	
	/**
	 * 设置长连接网络状态
	 * @param netState
	 */
	private void setNetState(NetState netState) {
		NetState oriState = this.mNetState; // 设置前的状态
		synchronized (mNetState) {
			this.mNetState = netState;
		}
		Tracer.i(TAG, "NetState Change to " + netState);

		if (netState == NetState.ESTABLISH) {
			if (oriState != NetState.ESTABLISH) {
//				boolean reload = keepAliveFilter.needReload();
//				MProxy.onTransPointEnable(tpName, true, reload); // 连接建立
			}
		} else {
			if (oriState == NetState.ESTABLISH) {
//				MProxy.onTransPointEnable(tpName, false, false); // 连接断开
			}
		}
	}

	/**
	 * ipAddr为域名时调用这个方法初始化,域名解析是网络操作，不能放到主线程处理
	 * @param context
	 * @param ipAddr
	 * @param ipPort
	 * @param typeName
	 */
	public MinaClientThread(Context context, String ipAddr, int ipPort, String typeName) {
		super(MINA_THREAD_NAME);
		mContext = context.getApplicationContext();
		mIpAddr = ipAddr;
		mIpPort = ipPort;
		mTypeName = typeName;
		
		setConnectAttributes();
	}
	
	/* PUSH连接状态枚举 */
	public static enum NetState {
		BROKEN, // 未连接状态
		CONNECTING, // 正在连接
		CONNECTED, // 已连接
		LOGINING, // 已经连接，正在登陆中，未完成登陆
		ESTABLISH, // 已经登陆成功
		LOGOUTING, // 正在注销
		CLOSING, // 正在关闭连接
		CHAOS, // 混乱状态，进入后，该连接不可用
	}
	
	/**
	 * 设置连接属性、参数
	 */
	private void setConnectAttributes() {
		if(mTypeName.equals(CONNECT_TYPE_NAME_TCP)) {
			mTcpConnector = new NioSocketConnector();
		} else {
			mUdpConnector = new NioDatagramConnector();
		}
		setConnectTimeout(3, 3); // 连接超时，重连次数
		setBufferSize(2 * 1024 * 1024, 1024 * 1024); // 设置缓冲
		setHandler(this); // 设置mina事件处理
	}
	
	/**
	 * 设置mina事件处理器，同步操作，需在connect前调用
	 * 
	 * @param handler
	 */
	private void setHandler(IoHandler handler) {
		if(mTypeName.equals(CONNECT_TYPE_NAME_TCP)) {
			mTcpConnector.setHandler(handler);
		} else {
			mUdpConnector.setHandler(handler);
		}
		
	}
	
	/**
	 * 配置读写缓冲大小，同步操作，需在connect前调用
	 * 
	 * @param readBufSize 读缓冲区大小
	 * @param writeBufSize 写缓冲区大小
	 */
	private void setBufferSize(int readBufSize, int writeBufSize) {
		if(mTypeName.equals(CONNECT_TYPE_NAME_TCP)) {
			mTcpConnector.getSessionConfig().setReadBufferSize(readBufSize);
			mTcpConnector.getSessionConfig().setSendBufferSize(writeBufSize);
		} else {
			mUdpConnector.getSessionConfig().setReadBufferSize(readBufSize);
			mUdpConnector.getSessionConfig().setSendBufferSize(writeBufSize);
		}
	}
	
	/**
	 * 设置连接超时，同步操作，需在connect前调用
	 * 
	 * @param seconds 超时时间，以秒为单位
	 * @param reconnectTimes 单次发起连接重连次数，0表示不重连，1表示重连一次，即可能会有两次连接
	 */
	private void setConnectTimeout(int seconds, int reconnectTimes) {
		if(mTypeName.equals(CONNECT_TYPE_NAME_TCP)) {
			mTcpConnector.setConnectTimeoutMillis(seconds * 1000);
			mTcpConnector.setConnectTimeoutCheckInterval(seconds / reconnectTimes * 1000);
		} else {
			mUdpConnector.setConnectTimeoutMillis(seconds * 1000);
			mUdpConnector.setConnectTimeoutCheckInterval(seconds / reconnectTimes * 1000);
		}
		
	}
	
	/**
	 * 创建session
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		Tracer.e(TAG, "-- mina sessionCreated --");
	}

	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		Tracer.e(TAG, "-- mina sessionOpened --");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Tracer.e(TAG, "-- mina sessionClosed --");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		Tracer.e(TAG, "-- mina sessionIdle --");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Tracer.e(TAG, "-- mina exceptionCaught --");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Tracer.e(TAG, "-- mina messageReceived --");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		Tracer.e(TAG, "-- mina messageSent --");
	}

}
