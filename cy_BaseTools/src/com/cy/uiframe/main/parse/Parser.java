package com.cy.uiframe.main.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.cy.global.WatchDog;
import com.cy.uiframe.main.ViewHelper;
import com.cy.utils.ToastUtil;

public class Parser<T> {
	
	protected int mCurPage = 0;
	protected boolean mHasNextPage = true;
	protected int mTotalCount;
	
	private ArrayList<T> mLeftData = new ArrayList<T>();
	private ParserCallBack<T> mParserCallBack;
	
	public interface ParserCallBack<T> {
		/**
		 * 数据解析完成回调
		 * @param list
		 * @param exceptionType
		 */
		void onParse(ArrayList<T> list, int exceptionType);
	}
	
	public Parser(ParserCallBack<T> callback) {
		init(callback);
	}
	
	private void init(ParserCallBack<T> callback) {
		mParserCallBack = callback;
	}
	
	public boolean parse(String data) {
		try {
			if(isNeedCheckAccountStatus() && isLoginExpired(data)) { //?? 这里的逻辑是否可以放到IUrlBean里面去做
				onLoginExpired();
				return false;
			}
			
			JSONArray list = createJSONArray(data);
			if (list == null) {
				onGetData(null, ViewHelper.TYPE_NO_ACTION);
				return false;
			}

			ArrayList<T> dataList = createDataList(list);
			if (dataList.isEmpty()) {
				onGetDataFail();
				return false;
			}

			onGetData(dataList);
			return !dataList.isEmpty() || hasNextPage();
		} catch(Exception e) {
			onGetDataFail();
			return false;
		}
		
	}

	protected boolean isNeedCheckAccountStatus() {
		return false;
	}
	
	protected boolean isLoginExpired(String data) {
		return false;
	}
	
	protected void onLoginExpired() {
		ToastUtil.showToast(WatchDog.getContext(), "账号被提出登陆了，需要重新登陆！", Toast.LENGTH_SHORT);
	}
	
	private void onGetDataFail() {
		int exceptionType = isFirstPageData() ? ViewHelper.TYPE_NO_DATA : ViewHelper.TYPE_LIST_BOTTOM;
		onGetData(null, exceptionType);
	}
	
	private void onGetData(ArrayList<T> dataList) {
		int size = dataList.size();
		dataFilter(dataList);
		addLeftData(dataList);
		if (dataList.isEmpty() && isLastPage()) {
			onGetDataFail();
			return;
		}

		if (isDataEnough(dataList.size(), size) || isLastPage()) {
			onGetData(dataList, ViewHelper.TYPE_NO_EXCEPTION);
			return;
		}

		mLeftData.addAll(dataList);
		onGetData(null, ViewHelper.TYPE_NOT_ENOUGH_DATA);
	}
	
	private boolean isDataEnough(int newSize, int oldSize) {
		return newSize >= oldSize / 2;
	}
	
	public void onGetData(ArrayList<T> list, int exceptionType) {
		if(null == mParserCallBack) {
			return;
		}
		
		mParserCallBack.onParse(list, exceptionType);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	protected JSONArray createJSONArray(String data) throws JSONException {
		JSONObject json = new JSONObject(data);
		JSONObject dataJson = new JSONObject(json.getString("data"));
		mCurPage = dataJson.optInt("curpage", 1);
		mHasNextPage = dataJson.optBoolean("hasnext", false);
		mTotalCount = dataJson.optInt("totalCount", 0);
		return dataJson.getJSONArray("list");
	}
	
	protected ArrayList<T> createDataList(JSONArray list) throws JSONException {
		return new ArrayList<T>();
	}
	
	/**
	 * 过滤游戏、服务器推送的游戏如果已经有安装或者其他因为其他原因不就展示已安装游戏。
	 * @param dataList
	 */
	protected void dataFilter(ArrayList<T> dataList) {
	}
	
	private void addLeftData(ArrayList<T> dataList) {
		if (mLeftData.isEmpty()) {
			return;
		}

		if (!isFirstPageData()) {
			dataList.addAll(0, mLeftData);
		}
		mLeftData.clear();
	}
	
	public int getTotalCount() {
		return mTotalCount;
	}

	public boolean hasNextPage() {
		return mHasNextPage;
	}

	public boolean isLastPage() {
		return !mHasNextPage;
	}

	public boolean isFirstPageData() {
		return mCurPage == 1;
	}
	
	public void deInit() {
		mParserCallBack = null;
	}
	
}
