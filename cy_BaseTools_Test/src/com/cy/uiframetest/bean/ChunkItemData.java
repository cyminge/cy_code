package com.cy.uiframetest.bean;

import java.util.ArrayList;

public class ChunkItemData<T> extends ChunkListData {
	public ArrayList<T> subItems;
	
	public boolean hasMore() {
		if(null == subItems || subItems.isEmpty()) {
			return false;
		}
		
		return total > subItems.size();
	}
}
