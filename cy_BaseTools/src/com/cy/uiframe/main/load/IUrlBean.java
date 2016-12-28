package com.cy.uiframe.main.load;

import java.util.HashMap;

public interface IUrlBean {
	 public String getCacheKey();

     public String postData(HashMap<String, String> map);
}
