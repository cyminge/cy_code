package com.cy.uiframe.main.load;

import java.util.HashMap;

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
	public String postData(HashMap<String, String> map) {
		return null;
	}

}
