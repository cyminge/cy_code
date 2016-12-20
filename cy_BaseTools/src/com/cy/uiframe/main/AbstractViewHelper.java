package com.cy.uiframe.main;

import android.annotation.SuppressLint;

import com.cy.constant.Constant;
import com.cy.frame.downloader.util.TimeUtils;
import com.cy.uiframe.main.cache.UIDataCache;
import com.cy.utils.Utils;

/**
 * 跟账号相关的数据，怎么再切换账号和登陆的时候清除缓存
 * 
 * @author JLB6088
 * 
 */
@SuppressLint("NewApi") public abstract class AbstractViewHelper implements IViewHelper {

	private boolean mHasLoad = false; // 是否已经加载过
	private boolean mNeedReset = false; // 是否重置数据
	private IUrlBean mUrlBean; 
	
    private static final String UUID_DIVIDER = "U_U"; // 缓存数据添加UUID分隔符
    private static final String TIME_DIVIDER = "@_@"; // 上次拉取数据时间戳分隔符

	private AbstractViewHelper(IUrlBean urlBean) {
		mUrlBean = urlBean;
	}

	@Override
	public void initLoad() {
		if (mHasLoad) {
			return;
		}
		mHasLoad = true;
		registerListener();
		startLoad();
	}

	public void startLoad() {
		if (!isNeedCache()) {
			checkNet();
			return;
		}

		String cacheData = getCacheData();
		if (isCacheInvalid(cacheData)) {
			onCacheEmpty();
		} else {
			onCacheValid(cacheData);
		}
	}

	protected boolean isNeedCache() {
		return true;
	}

	private void checkNet() {
		if (!Utils.hasNetwork()) {
			// TODO
			return;
		}

		checkDataByLoading();
	}

	private void checkDataByLoading() {

	}

	private String getCacheData() {
		return UIDataCache.INSTANCE.getString(mUrlBean.getCacheKey(), Constant.EMPTY);
	}

	private boolean isCacheInvalid(String cacheData) {
		if(cacheData.isEmpty()) {
			return true;
		}
		
		if (isCacheAssociatedWithAccount(cacheData)) {
			if(isUUIDChanged(cacheData)) {
				return true;
			}
			
		}
		
		return (mNeedReset && isDataExpired(cacheData));
	}
	
	private boolean isDataExpired(String cacheData) {
        long lastCheckTime = getLastCheckTime(cacheData);
        return TimeUtils.isExceedLimitTime(lastCheckTime, System.currentTimeMillis(), Constant.HOUR_1);
    }

    protected long getLastCheckTime(String cacheData) {
        int timeIndex = isCacheAssociatedWithAccount(cacheData) ? cacheData.indexOf(UUID_DIVIDER) + UUID_DIVIDER
                .length() : 0;
        String time = cacheData.substring(timeIndex, cacheData.indexOf(TIME_DIVIDER));
        return Long.parseLong(time);
    }
	
	private boolean isCacheAssociatedWithAccount(String cacheData) {
		if(cacheData.contains(UUID_DIVIDER)) {
			return true;
		}
		
		return false;
	}
	
	private boolean isUUIDChanged(String cacheData) {
        String cacheUUID = cacheData.substring(0, cacheData.indexOf(UUID_DIVIDER));
        String currentUUID = getCurrentUUID();
        return !cacheUUID.equals(currentUUID);
    }
	
	/**
	 * 获取当前账号的UUID
	 * @return
	 */
	protected abstract String getCurrentUUID();

	private void onCacheEmpty() {

	}

	private void onCacheValid(String cacheData) {

	}

	@Override
	public void exit() {
		unregisterListener();
	}

	protected void registerListener() {

	}

	protected void unregisterListener() {

	}

}
