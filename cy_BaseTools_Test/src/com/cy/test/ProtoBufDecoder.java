/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package com.cy.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cy.tracer.Tracer;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufDecoder implements ProtocolDecoder {
	private static final String TAG = "ProtoBufDecoder";
	private static final Logger LOG = LoggerFactory.getLogger(ProtoBufCodec.class);

	/**
	 * 将IoBuffer转换成byte
	 * 
	 * @param str
	 */
	public static byte[] ioBufferToByte(Object message) {
		if (!(message instanceof IoBuffer)) {
			return null;
		}
		IoBuffer ioBuffer = (IoBuffer) message;
		byte[] b = new byte[ioBuffer.limit()];
		ioBuffer.get(b);
		return b;
	}

	public static final AttributeKey BUF_BYTE = new AttributeKey(ProtoBufDecoder.class, "bufb");

	public void decode(final IoSession ioSession, final IoBuffer ioBuffer, final ProtocolDecoderOutput out) throws InvalidProtocolBufferException {

		// 要不要考虑断网的情况？？？
		IoBuffer ioBufTmp = null;
		byte[] byDataOld = (byte[]) ioSession.getAttribute(BUF_BYTE);
		if (byDataOld == null) {
//			Tracer.writeToFile(">>>2、当前没有要处理的数据,现在的包长度 = " + ioBuffer.remaining());
			ioBufTmp = (IoBuffer) ioBuffer;
		} else {
//			Tracer.writeToFile(">>>2、合并尚未处理的数据，缓存的包长度 = " + byDataOld.length);
			ioBufTmp = IoBuffer.allocate(byDataOld.length + ioBuffer.remaining());
			ioBufTmp.setAutoExpand(true);
			ioBufTmp.put(byDataOld);
			ioBufTmp.put(ioBuffer);
			ioBufTmp.flip();
		}

//		Tracer.writeToFile(">>>3、待处理包的总长度  = " + ioBufTmp.remaining());
		ioSession.removeAttribute(BUF_BYTE);

		int msgSeq = 0;
		int error = 0;
		int retCode = 0; // 请求返回错误码：例如session time out

		int dataLen = 0;
		byte[] byteData = null;
		while (ioBufTmp.remaining() >= 4) { // 循环处理数据包
			dataLen = ioBufTmp.getInt(ioBufTmp.position());
//			Tracer.writeToFile(">>>4、 解包需要的包的长度  = " + dataLen);
			if (ioBufTmp.remaining() < dataLen) {
				break;
			}

			byteData = new byte[dataLen];
			ioBufTmp.get(byteData);

			NetPack.Response response = new NetPack.Response();
			try {
				ByteArrayInputStream bintput = new ByteArrayInputStream(byteData);
				DataInputStream dintput = new DataInputStream(bintput);

				boolean flag = response.unserialize(dintput);
				if (!flag) {
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

//			msgSeq = response.head().seq;
//			error = 0;
//			retCode = 0;
//			Class<?> clazz = ConstantsProtocal.getClassType(response.head().opCmd);
//			GeneratedMessage parsedMsg = null;
//			if (null == clazz) {
//				Tracer.e(TAG, "receive not protocol: opCmd:" + response.head().opCmd + ", seq:" + msgSeq);
//			} else {
//				try {
//					Method parseMethod = clazz.getMethod("parseFrom", new Class[] { byte[].class });
//					parsedMsg = (GeneratedMessage) parseMethod.invoke(null, response.body());
//
//					/* 此处抛出异常，则retCode不会赋值，也不会调用 */
//					Method getResponseMethod = parsedMsg.getClass().getMethod("getBaseResponse", new Class[] {});
//					BaseResponse baseRsp = (BaseResponse) getResponseMethod.invoke(parsedMsg);
//					retCode = baseRsp.getRet();
//				} catch (NoSuchMethodException e) {
////					e.printStackTrace();	
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					if (e.getTargetException() instanceof InvalidProtocolBufferException) {
////						Tracer.writeToFile("receive buffer not protocol");
//						Tracer.e(TAG, "receive buffer not protocol!!! opCmd:"+response.head().opCmd);
//					} else {
////						Tracer.writeToFile("unknown decode exception");
//						Tracer.e(TAG, "unknown decode exception!!! opCmd:"+response.head().opCmd);
//					}
//					e.printStackTrace();
//				} finally {
//					if (null != parsedMsg) {
//						MinaMessage minaMessage = new MinaMessage(String.valueOf(msgSeq), parsedMsg);
//						out.write(minaMessage);
//						Tracer.e(TAG, "<== [" + minaMessage.msg_id + "]" + ConstantsProtocal.getProtoObjDesc(parsedMsg));
//
//						if (parsedMsg instanceof PingResponse) {
//							Tracer.e(TAG, "xx-mina", "<== [" + minaMessage.msg_id + "]" + ConstantsProtocal.getProtoObjDesc(parsedMsg));
//						}
//					}
//				}
//			}
			
			Tracer.d(TAG, "request " + response.head().opCmd + " return code :" + retCode);
		}

		if (ioBufTmp.hasRemaining()) { // 如果有剩余的数据，则放入Session中
//			Tracer.writeToFile("5、有剩余的数据放入Session中 :" + ioBufTmp.remaining());
			byte[] tmpb = new byte[ioBufTmp.remaining()];
			ioBufTmp.get(tmpb);
			ioSession.setAttribute(BUF_BYTE, tmpb);
		} else {
		}

	}

	@Override
	public void finishDecode(final IoSession session, final ProtocolDecoderOutput out) throws Exception {
	}

	@Override
	public void dispose(final IoSession session) throws Exception {
		session.removeAttribute(BUF_BYTE);
	}

}
