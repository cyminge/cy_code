package com.cy.uiframe.main;

import java.util.HashMap;

import android.annotation.SuppressLint;

import com.cy.constant.Constant;
import com.cy.frame.downloader.util.TimeUtils;
import com.cy.global.WatchDog;
import com.cy.threadpool.NormalThreadPool;
import com.cy.uiframe.main.cache.UIDataCache;
import com.cy.utils.Utils;

/**
 * 跟账号相关的数据，怎么在切换账号和登陆的时候清除缓存
 * 
 * @author JLB6088
 * 
 */
@SuppressLint("NewApi")
public abstract class AbstractViewHelper implements IViewHelper {

	private boolean mHasLoad = false; // 是否已经加载过
	private boolean mNeedReset = false; // 是否重置数据
	private IUrlBean mUrlBean;

	private static final String UUID_DIVIDER = "U_U"; // 缓存数据添加UUID分隔符
	private static final String TIME_DIVIDER = "@_@"; // 上次拉取数据时间戳分隔符

	private static final int DEFULAT_DATA_EXPIRED_TIME = Constant.HOUR_1;

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
			onCacheEmptyOrInvalide();
		} else {
			onCacheValid(cacheData);
		}
	}

	protected boolean isNeedCache() {
		return true;
	}

	private void checkNet() {
		if (!Utils.hasNetwork()) {
			showNoNetworkView();
			return;
		}

		checkDataByLoading();
	}
	
	protected abstract void showNoNetworkView();

	private void checkDataByLoading() {
		onCheckDataByLoading();
		startCheck();
	}
	
	/**
	 * 展示进度条
	 */
	protected abstract void onCheckDataByLoading();
	
	private void startCheck() {
		NormalThreadPool.getInstance().post(new Runnable() {

            @Override
            public void run() {
                String data = doPost();
                if (isRequestDataSuccess(data)) {
                    onLoadDataSucc(data);
                } else {
                    WatchDog.post(new Runnable() {
                        @Override
                        public void run() {
                        	onNetworkError();
                        }
                    });
                }
            }
        });
	}
	
	protected abstract void onNetworkError();
	
	protected String doPost() {
		return mUrlBean.postData(getPostMap());
	}

	protected HashMap<String, String> getPostMap() {
        return new HashMap<String, String>();
    }
	
	/**
	 * 是否请求数据有效
	 * @param result
	 * @return
	 */
	protected abstract boolean isRequestDataSuccess(String result);
	
	protected void onLoadDataSucc(final String data) {
        WatchDog.post(new Runnable() {
            @Override
            public void run() {
                boolean success = onGetRealCacheData(data);
                if (success && isNeedCache()) {
                    putCacheData(data);
                }
            }
        });
    }
	
	private void putCacheData(String jsonData) {
        String timeStampData = System.currentTimeMillis() + TIME_DIVIDER + jsonData;
        UIDataCache.INSTANCE.putString(mUrlBean.getCacheKey(), isCacheAssociatedWithAccount() ?
                combineCacheData(timeStampData) : timeStampData);
    }

    private String combineCacheData(String timeStampData) {
        return getCurrentAccount() + UUID_DIVIDER + timeStampData;
    }
	
	private String getCacheData() {
		return UIDataCache.INSTANCE.getString(mUrlBean.getCacheKey(), Constant.EMPTY);
	}
	
	protected abstract boolean isCacheAssociatedWithAccount();

	private boolean isCacheInvalid(String cacheData) {
		if (cacheData.isEmpty()) {
			return true;
		}

		if (isCacheHasAccount(cacheData)) {
			if (isAccountChanged(cacheData)) {
				return true;
			}

		}

		return (mNeedReset && isDataExpired(cacheData)); // isDataExpired ??
	}

	private boolean isDataExpired(String cacheData) {
		long lastCheckTime = getLastCheckTime(cacheData);
		return TimeUtils.isExceedLimitTime(lastCheckTime, System.currentTimeMillis(), getDataExpiredTime());
	}

	/**
	 * 默认的缓存数据过期时间 以毫秒为单位
	 * 
	 * @return
	 */
	protected int getDataExpiredTime() {
		return DEFULAT_DATA_EXPIRED_TIME;
	}

	private long getLastCheckTime(String cacheData) {
		int timeIndex = isCacheHasAccount(cacheData) ? cacheData.indexOf(UUID_DIVIDER)
				+ UUID_DIVIDER.length() : 0;
		String time = cacheData.substring(timeIndex, cacheData.indexOf(TIME_DIVIDER));
		return Long.parseLong(time);
	}

	private boolean isCacheHasAccount(String cacheData) {
		if (cacheData.contains(UUID_DIVIDER)) {
			return true;
		}

		return false;
	}

	private boolean isAccountChanged(String cacheData) {
		String cacheUUID = cacheData.substring(0, cacheData.indexOf(UUID_DIVIDER));
		String currentUUID = getCurrentAccount();
		return !cacheUUID.equals(currentUUID);
	}

	/**
	 * 获取当前账号的UUID
	 * 
	 * @return
	 */
	protected abstract String getCurrentAccount();

	private void onCacheEmptyOrInvalide() {
		mNeedReset = false;
		clearCacheData();
		checkNet();
	}

	private void clearCacheData() {
		UIDataCache.INSTANCE.remove(mUrlBean.getCacheKey());
	}

	private void onCacheValid(String cacheData) {
		String realCacheData = getRealCacheData(cacheData);
		boolean valid = onGetRealCacheData(realCacheData); // 如果缓存有效，有限展示数据
		
		if(!isDataExpired(cacheData) || !Utils.hasNetwork()) {
			return;
		}

		if (valid) {
			startPullRefresh();
		} else {
			checkDataByLoading();
		}
	}
	
	/**
	 * 需要加载新的数据
	 */
	protected abstract void startPullRefresh();

	private String getRealCacheData(String cacheData) {
		return cacheData.substring(cacheData.indexOf(TIME_DIVIDER) + TIME_DIVIDER.length());
	}

	protected boolean onGetRealCacheData(String data) {
		boolean succ = onParseData(data);
		onParseDataResult(succ);
		return succ;
	}

	protected abstract boolean onParseData(String data);

	protected void onParseDataResult(final boolean success) {
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
