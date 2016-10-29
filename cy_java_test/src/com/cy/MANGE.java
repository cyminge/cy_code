package com.cy;

import java.util.List;

public class MANGE {
	
	public List<Style> style; 
	
	
	public class Style {
		public String background_color;
	}
	
	public List<Action> action;
	
	public class Action {
		public String label;
		public String type;
		public String data;
	}
	
	public List<Menu> menu;
	
	public class Menu {
		public List<String> action;
	}
	
	
//	{
//	    "style": [
//	        {
//	            "background-color": "000000"
//	        }
//	    ],
//	    "action": [
//	        {
//	            "label": "Clicks",
//	            "type": "wap",
//	            "data": "http://c.vserv.mobi/delivery/ck.php?p=2__b=337131__zoneid=137251__OXLCA=1__cb=f38465a4__dc=1800__cd=a_apsoutheast1a-1433753461__c=48003__vr=202__aid=89c9aa90dd05b1a5__mser=cdn__vuid=Aeab3b25c752b7c2e8c410f9284d62738__fc=1__zt=a__sa=A__pid=48003__r=http%3A%2F%2Ftracking.affilimob.com%2Faff_c%3Fms%3D41%26offer_id%3D6343%26aff_id%3D1%26ad%3D451720%26pid%3D5b0a64c0%26vserv%3D5b0a64c0_337131_f38465a4&app=1"
//	        }
//	    ],
//	    "menu": [
//	        [
//	            {
//	                "action": 0
//	            }
//	        ]
//	    ],
//	    "render": [
//	        {
//	            "type": "image",
//	            "data": "http://img.vserv.mobi/i/800x480_7/705fd08dccf0ed038c8b861d2172b715.jpg?137251_337131_f38465a4",
//	            "notify": [
//	                "http://rq.vserv.mobi/delivery/lg.php?b=337131&c=48003&zoneid=137251&cb=f38465a4&OXLIA=1&vr=202&aid=89c9aa90dd05b1a5&mser=cdn&vuid=Aeab3b25c752b7c2e8c410f9284d62738&fc=1&zt=a&sa=A&pid=48003&app=1"
//	            ]
//	        }
//	    ],
//	    "advs": [
//	        {
//	            "render": 0,
//	            "style": 0,
//	            "select": "",
//	            "cancel": "",
//	            "key": [
//	                {
//	                    "menu": 0,
//	                    "only-first": "true",
//	                    "label": "Clicks"
//	                },
//	                {
//	                    "hide": "false",
//	                    "label": "",
//	                    "label_mid": "",
//	                    "label_end": ""
//	                }
//	            ]
//	        }
//	    ]
//	}

}
