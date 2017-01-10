package com.cy.uiframetest.bean;

public class BrickDissertationData extends BrickItemData<GameBean> {
	public float mX = 0f;
	public String mSource = "dissertation";
	
	public boolean hasMore() {
		if(null == subItems || subItems.isEmpty()) {
			return false;
		}
		
		return total > subItems.size();
	}
	
}
