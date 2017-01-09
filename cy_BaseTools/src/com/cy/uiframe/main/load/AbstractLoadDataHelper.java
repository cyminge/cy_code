package com.cy.uiframe.main.load;

import java.util.HashMap;

import android.annotation.SuppressLint;

import com.cy.constant.Constant;
import com.cy.frame.downloader.util.TimeUtils;
import com.cy.global.WatchDog;
import com.cy.threadpool.NormalThreadPool;
import com.cy.uiframe.main.cache.UIDataCache;
import com.cy.uiframe.main.utils.TimeDelayUtil;
import com.cy.utils.Utils;

/**
 * 跟账号相关的数据，怎么在切换账号和登陆的时候清除缓存
 * 
 * @author JLB6088
 * 
 */
@SuppressLint("NewApi")
public abstract class AbstractLoadDataHelper {

	private boolean mHasLoad = false; // 是否已经加载过
	private boolean mNeedReset = false; // 是否重置数据
	private IUrlBean mUrlBean;

	private static final String UUID_DIVIDER = "U_U"; // 缓存数据添加UUID分隔符
	private static final String TIME_DIVIDER = "@_@"; // 上次拉取数据时间戳分隔符

	private static final int DEFULAT_DATA_EXPIRED_TIME = Constant.HOUR_1;
	
	private long mStartTime;
	private static final long PARSE_DATA_MIN_TIME = 300;
	
	public AbstractLoadDataHelper(IUrlBean urlBean) {
		mUrlBean = urlBean;
		mStartTime = System.currentTimeMillis();
	}

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
	
	private void checkDataByLoading() {
		showLoadingView();
		startCheck();
	}
	
	/**
	 * 展示进度条 or others
	 */
	protected abstract void showLoadingView();
	
	public void checkDataByPull() {
		startCheck();
    }
	
	protected void startCheck() {
		NormalThreadPool.getInstance().post(new Runnable() {

            @Override
            public void run() {
                String data = doPost(); 
                if (isRequestDataSucc(data)) { //?? 这里是否需要把解析的工作交给Parser??.
                    onLoadDataSucc(data);
                } else {
                    WatchDog.post(new Runnable() {
                        @Override
                        public void run() {
                        	onLoadDataError();
                        }
                    });
                }
            }
        });
	}
	
	protected void onLoadDataError() {
		if(isShowingLoadingView()) {
			showNetTimeoutOrDataErrorView();
		} else {
			showContentView();
		}
	}
	
	protected String doPost() {
		return mUrlBean.postData(getPostMap());
	}

	protected HashMap<String, String> getPostMap() {
        return new HashMap<String, String>();
    }
	
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

	private void onCacheEmptyOrInvalide() {
		mNeedReset = false;
		clearCacheData();
		checkNet();
	}

	protected void clearCacheData() {
		UIDataCache.INSTANCE.remove(mUrlBean.getCacheKey());
	}

	private void onCacheValid(String cacheData) {
		String realCacheData = getRealCacheData(cacheData);
		boolean valid = onGetRealCacheData(realCacheData); // 如果缓存有效，优先展示数据
		
		if(!isDataExpired(cacheData) || !Utils.hasNetwork()) {
			return;
		}

		if (valid) {
			startPullRefresh();
		} else {
			checkDataByLoading();
		}
	}
	
	private String getRealCacheData(String cacheData) {
		return cacheData.substring(cacheData.indexOf(TIME_DIVIDER) + TIME_DIVIDER.length());
	}

	protected boolean onGetRealCacheData(String data) {
		boolean succ = onParseData(data);
		onParseDataResult(succ);
		return succ;
	}

	protected void onParseDataResult(final boolean success) {
		TimeDelayUtil.start(mStartTime, PARSE_DATA_MIN_TIME, new TimeDelayUtil.Callback() {
            @Override
            public void onTimeOut() {
                parseResult(success);
            }
        });
	}
	
	private void parseResult(boolean success) {
        if (success) {
        	showContentView();
        } else {
            onDataEmpty();
        }
    }

    protected void onDataEmpty() {
    	showNoDataView();
    	
        if (isNeedCache()) { //??
            clearCacheData();
        }
    }
    
	public void exit() {
		unregisterListener();
	}

	protected void registerListener() {

	}

	protected void unregisterListener() {

	}
	
    /**
     * 数据的主体部分解析出错，数据有误，需要展示给用户??
     */
    protected abstract void showNoDataView();

    /**
	 * 需要加载新的数据
	 */
	protected abstract void startPullRefresh();
	
	/**
	 * 是否请求数据有效， 对请求数据的通用结构进行解析。
	 * 比如是否有数据，是否来自正确的请求地址。
	 * （为了防公共wifi、运营商网络劫持，一般数据会加一个sign表示这是来自哪里的数据，如果没有这个sign就应该是被劫持了。）
	 * @param result
	 * @return
	 */
	protected abstract boolean isRequestDataSucc(String result);
	
	/**
	 * 数据加载超时或者服务器数据有误
	 */
	protected abstract void showNetTimeoutOrDataErrorView();
	
	/**
	 * 是否正在展示loadingView
	 * @return
	 */
	protected abstract boolean isShowingLoadingView();
	
	/**
	 * 缓存数据是否关联账号信息
	 * @return
	 */
	protected abstract boolean isCacheAssociatedWithAccount();
	
	/**
	 * 获取当前账号的UUID或者账号的唯一标示
	 * 
	 * @return
	 */
	protected abstract String getCurrentAccount();
	
	/**
	 * 主体数据解析
	 * @param data
	 * @return
	 */
	protected abstract boolean onParseData(String data);
	
	/**
	 * 显示内容view
	 */
	protected abstract void showContentView();
	
	/**
	 * 显示无网络view
	 */
	protected abstract void showNoNetworkView();

}
