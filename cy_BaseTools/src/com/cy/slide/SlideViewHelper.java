package com.cy.slide;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.utils.Utils;

public class SlideViewHelper extends AbstractSlideViewHelper<AdItem> {

	private static final int PIC_COUNT_PER_ITEM = 2;
	
	private static final String SP_NAME = "sp_name_slide_view";

	public interface DataParsedListener {
		public void onSlideParseResult(boolean succ);
	}

	private Context mContext;
	private Bitmap mDefaultBmp;
	private SlideView2 mSlideView;

	private DataParsedListener mDataParsedlistener;
	
	private IconManager mShowingIconManager;
	
	private ArrayList<AdItem> mslideNewData = new ArrayList<AdItem>();

	protected SlideViewHelper(SlideView2 slideView, Context context) {
		mSlideView = slideView;
		mContext = context;
	}

	protected boolean setDataSource(String source, JSONObject json, DataParsedListener listener) {
		mDataParsedlistener = listener;

		resetState();

		JSONArray array = json.optJSONArray(SlideConstant.SLIDE_ITEMS);
		if (null == array || array.length() == 0) {
			onParseResult(listener, false);
			return false;
		}

		setDataList(json, source);

		if (mSlideShowingList.isEmpty()) {
			onParseResult(listener, !mSlideShowingList.isEmpty());
			return false;
		}

		mSlideView.postDelayed(new Runnable() {
			@Override
			public void run() {
				initSlideBitmap();
//				mSlideView.resetIndexWidth();
//				mSlideView.prepareAnimation();
			}
		}, Constant.MILLIS_500);
		
		return true;
	}

	private void onParseResult(DataParsedListener listener, boolean hasData) {
		if (null == listener) {
			return;
		}
		listener.onSlideParseResult(hasData);
	}

	private void resetState() {
		onParseResult(mDataParsedlistener, true);
		mSlideView.resetState();
		destroy();
	}

	private void setDataList(JSONObject json, String source) {
		mslideNewData = createAdItems(json);
		if (mSlideShowingList.isEmpty()) {
			mSlideShowingList = createAdItems(getStoredJson(source));
		}
		
		if(mSlideShowingList.isEmpty()) {
			init(mslideNewData);
			storeDataSource(source, json);
		}
		

		if (update(mslideNewData)) {
			storeDataSource(source, json);
		}
	}

	/**
	 * you must overwrite AdItem's equals method when you want to use contains
	 * method.
	 */
	@Override
	protected boolean isSameData(List<AdItem> slideData) {
		return mSlideShowingList.contains(slideData);
	}

	protected void initSlideBitmap() {
		if (mSlideView.getViewWidth() == 0 || mSlideView.getViewHeight() == 0) {
			return;
		}

//		if (mSlideShowingList.isEmpty() || mNewDataList == null) {
//			return;
//		}

		initDefaultBmp();
		initIconsManager();
		obtainBitmap();
	}

	private void initDefaultBmp() {
		if (BitmapUtil.isBitmapEmpty(mDefaultBmp)) {
			mDefaultBmp = BitmapManager.generateDefaultBmp(mSlideView.getViewWidth(),
					mSlideView.getViewHeight(), R.color.default_bitmap_bg_color,
					mSlideView.getViewPaddingBottom());
			if (mSlideView.isExit()) {
				BitmapUtil.recycleBitmap(mDefaultBmp);
			}
		}
	}

	private void initIconsManager() {
//		if (mShowingIconsManager == null) {
//			mShowingIconsManager = new SlideIconsManager();
//		}
//		if (mNewDataIconsManager == null) {
//			mNewDataIconsManager = new SlideIconsManager();
//		}
	}

	private void obtainBitmap() {
//		for (int i = 0; i < mShowingList.size(); i++) {
//			downBitmap(mShowingIconsManager, getShowingAdItem(i), i * PIC_COUNT_PER_ITEM);
//		}
//		for (int i = 0; i < mNewDataList.size(); i++) {
//			downBitmap(mNewDataIconsManager, mNewDataList.get(i), i * PIC_COUNT_PER_ITEM);
//		}
	}

	private void downBitmap(IconManager iconsManager, AdItem item, int index) {
//		iconsManager.getBitmap(index, item.mImageUrl);
//		if (item.mArgs != null) {
//			iconsManager.getBitmap(index + 1, item.mArgs.mIconUrl);
//		}
	}

	private ArrayList<AdItem> createAdItems(JSONObject object) {
		ArrayList<AdItem> slideList = new ArrayList<AdItem>();
		if (object == null) {
			return slideList;
		}

		
		try {
			final JSONArray slideArray = object.getJSONArray(SlideConstant.SLIDE_ITEMS);
			for (int i = 0; i < slideArray.length(); i++) {
				JSONObject slideObject = slideArray.getJSONObject(i);
				AdItem adItem = new AdItem();
				adItem.mId = slideObject.getString(SlideConstant.AD_ID);
				adItem.mTitle = slideObject.getString(JsonConstant.TITLE);
				adItem.mImageUrl = slideObject.getString(SlideConstant.IMAGE_URL);
				if (Utils.isUrlInvalid(adItem.mImageUrl)) {
					adItem.mImageUrl = Constant.EMPTY;
				}
				adItem.mViewType = slideObject.getString(SlideConstant.VIEW_TYPE);
				adItem.mParam = slideObject.getString(SlideConstant.PARAM);
				slideList.add(adItem);
			}
		} catch (JSONException e) {
			Log.w("cyTest", "parse slide data error !!");
		}
		return slideList;
	}

	private JSONObject getStoredJson(String source) {
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		String data = sp.getString(source, Constant.EMPTY);
		JSONObject object = null;
		try {
			object = new JSONObject(data);
		} catch (JSONException e) {
		}
		return object;
	}

	private void storeDataSource(String source, JSONObject json) {
		String data = json.toString();
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(source, data).apply();
	}

	protected Bitmap getDefaultBitmap() {
		return mDefaultBmp;
	}

	protected Bitmap getSlideBitmap(int index) {
		return mShowingIconManager.getBitmap(index * PIC_COUNT_PER_ITEM, getItem(index).mImageUrl);
	}

	protected void onItemClick() {
//		AdItem adItem = getItem(mCurrIndex);
//		String type = adItem.mViewType;
//		String param = adItem.mParam;
//		ViewTypeUtil.onClickNavigations(mContext, type, param, createSource(adItem.mId));
	}

	protected String createSource(String adid) {
		return "";
	}

    protected void checkAllImageDone() {
        if (isAllImageDone()) {
            onAllImageDone();
        }
    }

    private boolean isAllImageDone() {
        if (mNewDataList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < mNewDataList.size(); i++) {
            if (!mNewDataIconsManager.hasBitmap(i)) {
                return false;
            }
        }
        return true;
    }

    private void onAllImageDone() {
        checkCurIndex();
        replaceShowingList();
        mSlideView.resetIndexWidth();
        mSlideView.prepareAnimation();
        mSlideView.postInvalidate();
    }

    private void replaceShowingList() {
        mShowingList.clear();
        mShowingList.addAll(mNewDataList);
        mShowingIconsManager.recycle();
        for (int i = 0; i < mNewDataList.size(); i++) {
            Bitmap bitmap = mNewDataIconsManager.getBitmap(i, null);
            mShowingIconsManager.putToIconsMap(i, bitmap);
        }
        mNewDataList.clear();
    }

	private void checkCurIndex() {
		if (mCurrIndex >= mSlideShowingList.size()) {
			mCurrIndex = 0;
		}
	}

}
