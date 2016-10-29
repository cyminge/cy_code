package com.cy.config;

/**
 * 放置全局静态变量
 * @author zhanmin
 */
public class MessageModel {
	
	public static final String FREQENCY_POINT_ROOT = "freqencypoint_root";
	public static final String FREQENCY_POINT_FILE_NAME = "DVB_C_TP_INFO_QUICK.txt";
	
	public static final int VIEW_DISAPPEAR_DELAY_MILLIS = 3000;
	public static final int DVB_VIEW_SLIDE_ANIMATION_MILLIS = 2000;
	
	/**
	 * DVB节目类型
	 */
	public static final int DVB_PROGRAM_TYPE_TV = 1;
	public static final int DVB_PROGRAM_TYPE_RADIO = 2;
	public static final int DVB_PROGRAM_TYPE_MY_TV = 3;
	public static final int DVB_PROGRAM_TYPE_MY_RADIO = 4;
	
	/**
	 * 动画显示类型
	 */
	public static final int DVB_ANIMATION_TYPE_SLIDE_IN_LEFT = 0;
	public static final int DVB_ANIMATION_TYPE_SLIDE_IN_RIGHT = 1;
	public static final int DVB_ANIMATION_TYPE_SLIDE_IN_TOP = 2;
	public static final int DVB_ANIMATION_TYPE_SLIDE_IN_BOTTOM = 3;
	public static final int DVB_ANIMATION_TYPE_SLIDE_OUT_LEFT = 4;
	public static final int DVB_ANIMATION_TYPE_SLIDE_OUT_RIGHT = 5;
	public static final int DVB_ANIMATION_TYPE_SLIDE_OUT_TOP = 6;
	public static final int DVB_ANIMATION_TYPE_SLIDE_OUT_BOTTOM = 7;
	
	
	//1**handler Message 消息处理
	// add by zhanmin 13.07.15
	/** 节目信息面板消失消息 ***/
	public static final int RESET_PROGRAM_INFO_NO_USE_HANDLER = 1001;
	/** 初始化DVB底层数据 ***/
	public static final int DVB_INIT_DVB_DATA = 1002;
	/** 开始播放节目 ***/
	public static final int DVB_START_PLAY_PROGRAM = 1003;
	/** 根据节目类型获取节目列表 ***/
	public static final int DVB_GET_PROGRAM_LIST_BY_TYPE_TV = 1004;
	public static final int DVB_GET_PROGRAM_LIST_BY_TYPE_RADIO = 1005;
	
	// add by zhanmin 13.07.15 end

}
