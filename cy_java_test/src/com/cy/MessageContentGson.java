package com.cy;


/**
 * 聊天消息内容
 * 
 * @author zhanmin
 * @time 2015-9-10
 */
public class MessageContentGson {

	public String type;    // 类型：文字、图片、语音、其他
	public String content; // 如果类型是图片，则这里是原始图片Url
	public String simpleContent; // 如果类型是图片，则这里是缩略图片Url
	public long size; // 如果是图片、语音，需要给出长度
	
	// type
	/*messageMap.put("locale", ContentLocale.class.getName());
	messageMap.put("image", ContentImage.class.getName());
	messageMap.put("text", ContentRichText.class.getName());
	messageMap.put("voice", ContentVoice.class.getName());
	messageMap.put("go_there", ContentGoThere.class.getName());
	messageMap.put("follow_me", ContentFollowMe.class.getName());*/
	
}
