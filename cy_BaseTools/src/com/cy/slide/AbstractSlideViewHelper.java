package com.cy.slide;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cy.constant.Constant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

@SuppressLint("NewApi") 
public abstract class AbstractSlideViewHelper<T> implements ISlideHelper<T> {

	protected List<T> mSlideShowingList = new ArrayList<T>();
	protected int mCurrIndex = 0;
	
	protected Context mContext;
	protected SlideView mSlideView;
	
	public AbstractSlideViewHelper(Context context, SlideView slideView) {
		mContext = context;
		mSlideView = slideView;
		mSlideView.init(context, this, SlideView.ANIMATION_EFFECTS_TYPE_1);
	}
	
	protected JSONObject getStoredJson(String slideSharePrefKey) {
		SharedPreferences sp = mContext.getSharedPreferences(getSlideSharePrefName(), Context.MODE_PRIVATE); 
		String data = sp.getString(slideSharePrefKey, Constant.EMPTY);
		if(TextUtils.isEmpty(data)) {
			return null;
		}
		
		JSONObject object = null;
		try {
			object = new JSONObject(data);
		} catch (JSONException e) {
		}
		
		return object;
	}

	protected void storeDataSource(String slideSharePrefKey, JSONObject json) {
		String data = json.toString();
		SharedPreferences sp = mContext.getSharedPreferences(getSlideSharePrefName(), Context.MODE_PRIVATE);
		sp.edit().putString(slideSharePrefKey, data).apply();
	}
	
	abstract String getSlideSharePrefName();
	
	@Override
	public void initSlideData(List<T> slideData) {
		if (null == slideData || slideData.isEmpty()) {
			return;
		}

		mSlideShowingList.addAll(slideData);
		
	}

	@Override
	public boolean updateSlideData(List<T> slideData) {
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
	public boolean switchImage(boolean isLeft) {
		if (isLeft) {
			mCurrIndex = getPreIndex();
		} else {
			mCurrIndex = getNextIndex();
		}
		
		return true;
	}
	
	@Override
	public void switchNext() {
		switchImage(false);
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
	
	abstract void initSlideBitmap();
	abstract Bitmap getSlideBitmap(int index);
	abstract Bitmap getDefaultBitmap();
	abstract void onItemClick();

}
