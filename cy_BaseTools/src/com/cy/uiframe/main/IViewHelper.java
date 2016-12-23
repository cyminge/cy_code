package com.cy.uiframe.main;

public interface IViewHelper {
	
	public void checkDataByPull();
	public void pullRefreshBegin();
	public void pullRefreshComplete();
	public boolean isReadyForPullStart();
	
	
}
