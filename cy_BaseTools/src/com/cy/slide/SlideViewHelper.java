package com.cy.slide;

import gn.com.android.gamehall.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.util.Log;

import com.cy.R;
import com.cy.constant.Constant;
import com.cy.frame.downloader.util.JsonConstant;
import com.cy.global.WatchDog;
import com.cy.imageloader.ImageLoader;
import com.cy.utils.Utils;
import com.cy.utils.bitmap.BitmapCompress;
import com.cy.utils.bitmap.BitmapUtils;

@SuppressLint("NewApi")
public class SlideViewHelper extends AbstractSlideViewHelper<AdItem> {

	private static final int PIC_COUNT_PER_ITEM = 2;

	private static final String SP_NAME = "sp_name_slide_view";

	public interface DataParsedListener {
		public void onSlideParseResult(boolean succ);
	}

	private Bitmap mDefaultBmp;

	private DataParsedListener mDataParsedlistener;

	private ArrayList<AdItem> mslideNewData = new ArrayList<AdItem>();

	public SlideViewHelper(SlideView2 slideView, Context context) {
		super(context, slideView);
	}

	@Override
	String getSlideSharePrefName() {
		return SP_NAME;
	}

	protected boolean setDataSource(String slideSharePrefKey, JSONObject json, DataParsedListener listener) {
		mDataParsedlistener = listener;

		resetState();

		JSONArray array = json.optJSONArray(SlideConstant.SLIDE_ITEMS);
		if (null == array || array.length() == 0) {
			onParseResult(listener, false);
			return false;
		}

		setDataList(json, slideSharePrefKey);

		if (mSlideShowingList.isEmpty()) {
			onParseResult(listener, !mSlideShowingList.isEmpty());
			return false;
		}

		WatchDog.postDelayed(mInitRunnable, Constant.MILLIS_500);

		return true;
	}

	private Runnable mInitRunnable = new Runnable() {

		@Override
		public void run() {
			initSlideBitmap();
			mSlideView.prepareAnimation();
		}
	};

	private void onParseResult(DataParsedListener listener, boolean hasData) {
		if (null == listener) {
			return;
		}
		listener.onSlideParseResult(hasData);
	}

	/**
	 * 重置数据
	 */
	private void resetState() {
		onParseResult(mDataParsedlistener, true);
		mSlideView.resetState();
		destroy();
	}

	private void setDataList(JSONObject json, String slideSharePrefKey) {
		if (mSlideShowingList.isEmpty()) {
			mSlideShowingList = createBannerItems(getStoredJson(slideSharePrefKey));
		}

		mslideNewData = createBannerItems(json);
		if (mslideNewData.isEmpty()) {
			return;
		}

		if (mSlideShowingList.isEmpty()) {
			initSlideData(mslideNewData);
			storeDataSource(slideSharePrefKey, json);
			return;
		}

		if (updateSlideData(mslideNewData)) {
			storeDataSource(slideSharePrefKey, json);
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

		// if (mSlideShowingList.isEmpty() || mNewDataList == null) {
		// return;
		// }

		initDefaultBmp();
		obtainBitmap();
	}

	private void initDefaultBmp() {
		if (BitmapUtils.isBitmapEmpty(mDefaultBmp)) {
			mDefaultBmp = generateDefaultBmp(mSlideView.getViewWidth(), mSlideView.getViewHeight(),
					R.color.default_bitmap_bg_color, mSlideView.getViewPaddingBottom());
			if (mSlideView.isExit()) {
				BitmapUtils.recycleBitmap(mDefaultBmp);
			}
		}
	}

	public static Bitmap generateDefaultBmp(int width, int height, int colorId, int paddingBottom) {
		if (width <= 0 || height <= 0) {
			return null;
		}
		Bitmap defaultBmp;
		Resources res = Utils.getResources();
		Bitmap defaultIcon = BitmapUtils.decodeResource(res, R.drawable.slide_default_icon);
		defaultBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		int targetSize = (int) res.getDimension(R.dimen.slide_default_image_size);
		Canvas canvas = new Canvas(defaultBmp);
		canvas.drawColor(res.getColor(colorId));
		Rect rect = new Rect();
		rect.left = width / 2 - targetSize / 2;
		rect.right = rect.left + targetSize;
		rect.top = (height - targetSize - paddingBottom) / 2;
		rect.bottom = rect.top + targetSize;
		canvas.drawBitmap(defaultIcon, null, rect, BitmapUtils.HIGH_PAINT);
		BitmapUtils.recycleBitmap(defaultIcon);
		return defaultBmp;
	}

	private void obtainBitmap() {
		for (int i = 0; i < mSlideShowingList.size(); i++) {
			downBitmap(mShowingIconsManager, getShowingAdItem(i), i * PIC_COUNT_PER_ITEM);
		}
		for (int i = 0; i < mNewDataList.size(); i++) {
			downBitmap(mNewDataIconsManager, mNewDataList.get(i), i * PIC_COUNT_PER_ITEM);
		}
	}

	private void downBitmap(AdItem item, int index) {
//		 iconsManager.getBitmap(index, item.mImageUrl);
//		 if (item.mArgs != null) {
//		 iconsManager.getBitmap(index + 1, item.mArgs.mIconUrl);
//		 }
		ImageLoader.INSTANCE.getBitmap(item.mImageUrl, view);
	}

	private ArrayList<AdItem> createBannerItems(JSONObject object) {
		if (object == null) {
			return new ArrayList<AdItem>();
		}

		ArrayList<AdItem> slideList = new ArrayList<AdItem>();

		try {
			final JSONArray slideArray = object.getJSONArray(SlideConstant.SLIDE_ITEMS);
			int slideLength = slideArray.length();
			for (int i = 0; i < slideLength; i++) {
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

	protected Bitmap getDefaultBitmap() {
		return mDefaultBmp;
	}

	protected Bitmap getSlideBitmap(int index) {
		return mShowingIconManager.getBitmap(index * PIC_COUNT_PER_ITEM, getItem(index).mImageUrl);
	}

	protected void onItemClick() {
		// AdItem adItem = getItem(mCurrIndex);
		// String type = adItem.mViewType;
		// String param = adItem.mParam;
		// ViewTypeUtil.onClickNavigations(mContext, type, param,
		// createSource(adItem.mId));
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

	@Override
	public void destroy() {
		super.destroy();
		WatchDog.removeRunnable(mInitRunnable);
	}

}
