package com.cy.jackson.bean;

import java.util.List;

/**
 * 与json数据格式一一对应
 * @author zhanmin
 */
public class ModelPageBean {
	public int page;
	public int pagesize;
	public int total;
	public ForumBean forum;
	public PhoneTypeBean phoneType;
	public List<TopicBean> bigTopTopicList;
	public List<TopicBean> topTopicList;
	public List<TopicBean> topicList;
	public String outmsg;
}
