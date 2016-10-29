package com.cy.slide;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSlideViewHelper<T> implements ISlideHelper<T> {

	protected List<T> mSlideShowingList = new ArrayList<T>();
	private int mCurrIndex = 0;

	@Override
	public void init(List<T> slideData) {
		if (null == slideData || slideData.isEmpty()) {
			return;
		}

		mSlideShowingList.addAll(slideData);
	}

	@Override
	public boolean update(List<T> slideData) {
		if (null == slideData || slideData.isEmpty()) {
			return false;
		}

		if (mSlideShowingList.isEmpty()) {
			mSlideShowingList.addAll(slideData);
			return true;
		}

		if(isSameData(slideData)) {
			return false;
		}
		
		mSlideShowingList.clear();
		mSlideShowingList.addAll(slideData);
		
		return true;
	}

	protected abstract boolean isSameData(List<T> slideData);
	
	@Override
	public T getItem(int index) {
		if (index < 0 || index >= mSlideShowingList.size()) {
			index = 0;
		}
		return mSlideShowingList.get(index);
	}

	@Override
	public boolean isEmpty() {
		return mSlideShowingList.isEmpty();
	}

	@Override
	public boolean isSingle() {
		return mSlideShowingList.size() == 1;
	}
	
	@Override
	public int getCount() {
		return mSlideShowingList.size();
	}

	@Override
	public int getCurrIndex() {
		return mCurrIndex;
	}

	@Override
	public int getPreIndex() {
		int preIndex = mCurrIndex - 1;
		if (preIndex < 0) {
			preIndex = mSlideShowingList.size() - 1;
		}

		return preIndex;
	}

	@Override
	public int getNextIndex() {
		int nextIndex = mCurrIndex + 1;
		if (nextIndex >= mSlideShowingList.size()) {
			nextIndex = 0;
		}

		return nextIndex;
	}
	
	@Override
	public void switchImage(boolean isLeft) {
		if (isLeft) {
			mCurrIndex = getPreIndex();
		} else {
			mCurrIndex = getNextIndex();
		}
	}

	@Override
	public void recycle() {

	}

	@Override
	public void destroy() {
		recycle();
		mSlideShowingList.clear();
		mCurrIndex = 0;
	}

	// public interface DataParsedListener {
	// public void onSlideParseResult(boolean succ);
	// }
	//
	// private int mCurIndex = 0;
	// private Context mContext;
	// private SlideView mSlideView;
	// private DownloadClickHelper mClickHelper;
	//
	// private volatile List<T> mShowingList = new ArrayList<T>();
	// private IconManager mShowingIconsManager;
	//
	// private ArrayList<T> mNewDataList = new ArrayList<T>();
	// private IconManager mNewDataIconsManager;
	//
	// protected AbstractSlideViewHelper(SlideView slideView, Context context) {
	// mSlideView = slideView;
	// mContext = context;
	// }
	//
	// protected boolean initDataList(List<T> datas, Context context) {
	// mShowingList.addAll(datas);
	// return true;
	// }
	//
	//
	// protected boolean setDataSource(String source, JSONObject json,
	// DataParsedListener listener) {
	// resetState();
	//
	// JSONArray array = json.optJSONArray(JsonConstant.SLIDE_ITEMS);
	// if (array == null || array.length() == 0) {
	// onParseResult(listener, false);
	// return false;
	// }
	//
	// initDataList(json, source);
	//
	// boolean isEmpty = mShowingList.isEmpty();
	// onParseResult(listener, !isEmpty);
	// if (isEmpty) {
	// return false;
	// }
	//
	// mSlideView.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// initSlideBitmap();
	// mSlideView.resetIndexWidth();
	// mSlideView.prepareAnimation();
	// }
	// }, Constant.MILLIS_500);
	// return true;
	// }
	//
	// private void onParseResult(DataParsedListener listener, boolean hasData)
	// {
	// Utils.setViewVisible(mSlideView, hasData);
	// if (listener != null) {
	// listener.onSlideParseResult(hasData);
	// }
	// }
	//
	// private void resetState() {
	// mSlideView.resetState();
	// mCurIndex = 0;
	// Utils.setViewVisible(mSlideView, true);
	// resetIconsManager();
	// clearData();
	// }
	//
	// private void initDataList(JSONObject json, String source) {
	// mNewDataList.addAll(createAdItems(json));
	// mShowingList.addAll(createAdItems(getStoredJson(source)));
	// if (!mNewDataList.isEmpty()) {
	// storeDataSource(source, json);
	// }
	// if (directShowNewList()) {
	// mShowingList.clear();
	// mShowingList.addAll(mNewDataList);
	// mNewDataList.clear();
	// }
	// }
	//
	// private boolean directShowNewList() {
	// if (mShowingList.isEmpty()) {
	// return true;
	// }
	//
	// for (int i = 0; i < mNewDataList.size(); i++) {
	// T newData = mNewDataList.get(i);
	// if (isShowingContain(newData)) {
	// continue;
	// }
	// return false;
	// }
	// return true;
	// }
	//
	// protected abstract boolean isShowingContain(T newData);
	//
	// protected boolean isSingle() {
	// if (mShowingList == null) {
	// return false;
	// }
	//
	// return mShowingList.size() == 1;
	// }
	//
	// protected boolean isEmpty() {
	// if (mShowingList == null) {
	// return true;
	// }
	//
	// return mShowingList.isEmpty();
	// }
	//
	// protected boolean switchImage(boolean isLeft) {
	// if (isLeft) {
	// mCurIndex = getPreIndex();
	// } else {
	// mCurIndex = getNextIndex();
	// }
	// return true;
	// }
	//
	// protected int getCount() {
	// if (mShowingList == null) {
	// return 0;
	// }
	//
	// return mShowingList.size();
	// }
	//
	// protected int getCurIndex() {
	// return mCurIndex;
	// }
	//
	// protected int getPreIndex() {
	// int preIndex = mCurIndex - 1;
	// if (preIndex < 0) {
	// preIndex = mShowingList.size() - 1;
	// }
	// return preIndex;
	// }
	//
	// protected int getNextIndex() {
	// int nextIndex = mCurIndex + 1;
	// if (nextIndex >= mShowingList.size()) {
	// nextIndex = 0;
	// }
	// return nextIndex;
	// }
	//
	// protected void switchNext() {
	// mCurIndex = getNextIndex();
	// }
	//
	// protected void recycle() {
	// resetIconsManager();
	// }
	//
	// protected void exit() {
	//
	// if (mShowingIconsManager != null) {
	// mShowingIconsManager.exit();
	// }
	// if (mNewDataIconsManager != null) {
	// mNewDataIconsManager.exit();
	// }
	// clearData();
	// }
	//
	// private void resetIconsManager() {
	// if (mShowingIconsManager != null) {
	// mShowingIconsManager.recycle();
	// }
	// if (mNewDataIconsManager != null) {
	// mNewDataIconsManager.recycle();
	// }
	// }
	//
	// private void clearData() {
	// if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
	// doClear();
	// } else {
	// mSlideView.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// doClear();
	// }
	// });
	// }
	// }
	//
	// private void doClear() {
	// mShowingList.clear();
	// mNewDataList.clear();
	// }
	//
	// protected void checkAllImageDone() {
	// if (isAllImageDone()) {
	// onAllImageDone();
	// }
	// }
	//
	// private boolean isAllImageDone() {
	// if (mNewDataList.isEmpty()) {
	// return false;
	// }
	// for (int i = 0; i < mNewDataList.size(); i++) {
	// if (!mNewDataIconsManager.hasBitmap(i)) {
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// private void onAllImageDone() {
	// checkCurIndex();
	// replaceShowingList();
	// mSlideView.resetIndexWidth();
	// mSlideView.prepareAnimation();
	// mSlideView.postInvalidate();
	// }
	//
	// private void replaceShowingList() {
	// mShowingList.clear();
	// mShowingList.addAll(mNewDataList);
	// mShowingIconsManager.recycle();
	// for (int i = 0; i < mNewDataList.size(); i++) {
	// Bitmap bitmap = mNewDataIconsManager.getBitmap(i, null);
	// mShowingIconsManager.putToIconsMap(i, bitmap);
	// }
	// mNewDataList.clear();
	// }
	//
	// private void checkCurIndex() {
	// if (mCurIndex >= mShowingList.size()) {
	// mCurIndex = 0;
	// }
	// }
	//
	// private T getShowingAdItem(int index) {
	// if (index < 0 || index >= mShowingList.size()) {
	// index = 0;
	// }
	//
	// return mShowingList.get(index);
	// }

}
