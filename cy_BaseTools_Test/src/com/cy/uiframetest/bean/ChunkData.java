package com.cy.uiframetest.bean;

public class ChunkData {

	public int mListItemType;  // 对应的是RecyclerView or ListView 的一个ItemViewType ， 自定义
    public Param param; // 跳转所需参数
    public String viewType; // 对应的是一个跳转的类型，比如跳转到哪个Activity
    public String listItemType; // 对应的是RecyclerView or ListView 的一个ItemViewType，服务器传递下来的，跟mListItemType 一一对应。
    public boolean isNeedLogin;
    public int total;
    public String itemBg;
    public String title;
    public String titleBg;

    public class Param {
    	public String url; // "url": "http://game.gionee.com/client/subject/detail?id=587&intersrc=SUBJECT587",
    	public String contentId; // "contentId": "587",
    	public String viewType; // "viewType": "TopicDetailView",
    	public String subViewType; // "subViewType": "WebView",
    	public String source; // "source": "subject587"
    	public String gameId; // "gameId": "",
    	public String title; // "title": "睡什么，起来HIGH！"	
    	public String gamePackage; // "package": "com.dxxd1.am",
    }
    
}
