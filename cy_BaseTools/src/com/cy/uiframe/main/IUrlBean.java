package com.cy.uiframe.main;

import java.util.HashMap;

public interface IUrlBean {
	 public String getCacheKey();

     public String postData(HashMap<String, String> map);
}
