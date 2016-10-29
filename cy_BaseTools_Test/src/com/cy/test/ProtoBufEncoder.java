package com.cy.test;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ProtoBufEncoder implements ProtocolEncoder {
	private static final String TAG = "ProtoBufEncoder";

//	private static final Logger LOG = LoggerFactory.getLogger(ProtoBufCodec.class);

	/**
	 * 往服务器发包时，如果encode接收到的是IoBuffer，会有问题，原因待查
	 */
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		/*
		 * Tracer.writeToFile(StringUtil.formatCurrDate("HH:mm:ss ") +
		 * ">>> TestEncoder.encode 编码发送protobuf数据给服务器" + "\n");
		 * Tracer.writeToFile("当前UIN = " + SipLoginAccountInfo.getUin());
		 * Tracer.writeToFile("当前sessionKey = " +
		 * HeartBeatControl.getSessionKey().toStringUtf8()); if (message
		 * instanceof NetPack.Request) { NetPack.Request request =
		 * (NetPack.Request) message; byte[] b = request.serialize();
		 * Tracer.writeToFile("+++ 发给服务器的包长度  = " + b.length); IoBuffer buf =
		 * IoBuffer.allocate(b.length, true); buf.setAutoExpand(true); // buf =
		 * IoBuffer.wrap(b); buf.put(b); buf.flip(); out.write(buf); }
		 * Tracer.writeToFile("encode end ... ");
		 */
		
		
//		if (message instanceof MinaMessage) {
//			MinaMessage minaMessage = (MinaMessage) message;
//			if (minaMessage.message instanceof GeneratedMessage) {
//				Integer cmdId = ConstantsProtocal.getMessageId(minaMessage.message.getClass());
//
//				if (cmdId == null) {
//					Tracer.e(TAG, "ProtoBufEncoder cant get clazz id");
//					return;
//				}
//
//				GeneratedMessage protoMsg = (GeneratedMessage) minaMessage.message;
//				NetPack.Request protoReq = new NetPack.Request(StringUtil.parseIntNotEmpty(minaMessage.msg_id, 0),
//						cmdId, protoMsg.toByteArray());
//				IoBuffer buffer = IoBuffer.wrap(protoReq.serialize());
//				out.write(buffer);
//				
//				String sessionKey = "";
//
//				try {
//					Method getBaseReqMethod = minaMessage.message.getClass().getMethod("getBaseRequest");
//					BaseRequest baseReq = (BaseRequest) getBaseReqMethod.invoke(minaMessage.message);
//					sessionKey = baseReq.getSessionKey().toStringUtf8();
//				} catch (Exception e) {
//					
//				}
//
//				MinaLog.x("==> [" + sessionKey + ":" + minaMessage.msg_id + "]" + ConstantsProtocal.getProtoObjDesc((GeneratedMessage) minaMessage.message));
//			} else {
//				Tracer.e(TAG, "mina message do not contain GeneratedMessage instance");
//			}
//		} else {
//			Tracer.e(TAG, "process message, not MinaMessage instance");
//		}
	}

	public void dispose(IoSession arg0) throws Exception {
	}

}
