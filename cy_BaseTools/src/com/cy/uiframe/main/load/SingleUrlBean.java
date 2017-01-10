package com.cy.uiframe.main.load;

import java.util.HashMap;

import com.cy.frame.downloader.util.JsonUtils;

public class SingleUrlBean implements IUrlBean {
	
	private String mUrl;
	
	public SingleUrlBean(String url) {
		mUrl = url;
	}

	@Override
	public String getCacheKey() {
		return mUrl;
	}

	@Override
	public String postData(HashMap<String, String> params) {
		String requestData = JsonUtils.postData(mUrl, params);
		return checkRequestData(requestData);
	}
	
	private String checkRequestData(String requestData) {
		return requestData;
	}

}
