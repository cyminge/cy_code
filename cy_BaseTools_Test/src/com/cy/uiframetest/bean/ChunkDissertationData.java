package com.cy.uiframetest.bean;

public class ChunkDissertationData extends ChunkItemData<GameBean> {
	public float mX = 0f;
	public String mSource = "dissertation";
	
	public boolean hasMore() {
		if(null == subItems || subItems.isEmpty()) {
			return false;
		}
		
		return total > subItems.size();
	}
	
}
