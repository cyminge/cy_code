package com.cy.uiframetest.main;

import com.cy.uiframe.main.BaseLoadDataHelper;
import com.cy.uiframe.main.load.ILoadDataHelper;
import com.cy.uiframe.main.load.IUrlBean;

public class UIFrameLoadDataHelper extends BaseLoadDataHelper {

	public UIFrameLoadDataHelper(IUrlBean urlBean, ILoadDataHelper loadDataHelper) {
		super(urlBean, loadDataHelper);
	}
	
	@Override
	public void startLoad() {
		clearCacheData(); // 测试用，每次都清空数据，验证数据加载流程
		super.startLoad();
	}
	
}
