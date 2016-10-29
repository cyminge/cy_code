package com.cy;

import java.util.ArrayList;

public class TipAdvertiseRateGson {
	
//	title=advertise_rate
//			content= {
//			           "vsn":"android"
//			           "content":{
//			                     "ad_rate":{
//			                              { "type":"notification","times":2,"seq":1 },
//			                              { "type":"banner","times":2,"seq":1 },
//			                              { "type":"appDialog","times":2,"seq":1 },
//			                              { "type":"ptp","times":2,"seq":1 },
//			                              { "type":"launch","times":2,"seq":1 },
//			                              { "type":"icon","times":2,"seq":1 },
//			                              { "type":"lock","times":2,"seq":1 },
//			                              { "type":"sysDialog","times":2,"seq":1 }
//			                               }
//			                     }
//			         }
	
	public String vsn = "";
	public AdvertiseRateContent content;
	
	public class AdvertiseRateContent {
		public ArrayList<AdvertiseRate> ad_rate;
	}
	
	public class AdvertiseRate {
		public String type;
		public int times;
		public int seq;
	}
	
}
