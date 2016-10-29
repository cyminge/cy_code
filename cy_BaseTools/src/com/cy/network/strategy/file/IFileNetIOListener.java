package com.cy.network.strategy.file;

/**
 * 下载回调接口
 * 
 * @author zhanmin
 * @time 2015-9-14
 */
public interface IFileNetIOListener {
	/* task progress percent , complete is 100 */
	public void onFileNetProgress(int progress);
	
	/* triggered when task is done */
	public void onFileNetTaskDone(String result);
}
