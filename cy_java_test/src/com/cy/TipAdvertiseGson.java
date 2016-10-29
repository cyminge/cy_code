package com.cy;


public class TipAdvertiseGson {
	

//title=advertise
//content={
//	"vsn":"android"
//	"content":{
//		  "ad": {
//			"adid":9527,  // 广告ID
//			"type":6,   // 数字内容或运算，表示2|4，Banner+App弹窗
//			"start":1428453784, // 有效起始时间戳
//			"end":1428483784,   // 结束时间戳
//			"action":1,        // 点击触发操作：下载或跳转
//			"url":"",           // 下载或跳转url
//			"text":"",          // 文本内容，直接下发
//			"frequency":5,      // 广告展示次数，在有效时间段超过展示次数就不展示了
//			"image":｛"banner":"","dialog":"","launch":"","icon":""｝         // 图片url，可为空
//			}
//		  }	
//	}
	
	
	public String vsn = "";
	public AdvertiseContent content;
	
	public class AdvertiseContent {
		public Advertise ad;
	}
	
	public class Advertise {
		public int adid;               // 广告ID
		public int type;               // 数字内容或运算，表示2|4，Banner+App弹窗
		public int start;              // 有效起始时间戳
		public int end;                // 结束时间戳
		public int action;             // 点击触发操作：跳转或下载 (1跳转,2下载)
		public String url = "";             // 下载或跳转url
		public String text = "";            // 文本内容，直接下发
		public int frequency;          // 广告展示次数，在有效时间段超过展示次数就不展示了
		public ImageBean image;        // 图片url，可为空
		
		@Override
		public boolean equals(Object obj) {
			if (null != obj && obj instanceof Advertise) {
				Advertise bean = (Advertise) obj;
				return adid == bean.adid;
			}
			return false;
		}
	}
	 
	
	public class ImageBean {
		public String banner = ""; // 通知栏或banner图片 
		public String dialog = ""; // 弹框图片
		public String launch = ""; // 启动图片
		public String icon = "";   // 系统主界面广告图标
	}
	
	

}
