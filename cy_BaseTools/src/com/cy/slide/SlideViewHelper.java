package com.cy.slide;

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
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import com.cy.R;
import com.cy.constant.Constant;
import com.cy.imageloader.ImageLoader;
import com.cy.utils.StringUtil;
import com.cy.utils.bitmap.BitmapUtils;

@SuppressLint("NewApi") public class SlideViewHelper extends AbstractSlideViewHelper<AdItem> {

	private static final int PIC_COUNT_PER_ITEM = 2;

	private static final String SP_NAME = "sp_name_slide_view";

	public interface DataParsedListener {
		public void onSlideParseResult(boolean succ);
	}

	private Context mContext;
	private Bitmap mDefaultBmp;
	private SlideView2 mSlideView;

	private DataParsedListener mDataParsedlistener;
	private ImageLoader mImageLoader;

	protected SlideViewHelper(SlideView2 slideView, Context context) {
		mSlideView = slideView;
		mContext = context;
		mImageLoader = new ImageLoader(context);
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
				// mSlideView.resetIndexWidth();
				// mSlideView.prepareAnimation();
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
		ArrayList<AdItem> slideData = createAdItems(json);
		if (mSlideShowingList.isEmpty()) {
			init(slideData);
			storeDataSource(source, json);
			return;
		}

		if (update(slideData)) {
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

		// if (mSlideShowingList.isEmpty() || mNewDataList == null) {
		// return;
		// }

		initDefaultBmp();
		obtainBitmap();
	}

	private void initDefaultBmp() {
		if (BitmapUtils.isBitmapEmpty(mDefaultBmp)) {
			mDefaultBmp = generateDefaultBmp(mSlideView.getViewWidth(), mSlideView.getViewHeight(),
					R.color.slide_view_default_bitmap_bg_color, mSlideView.getViewPaddingBottom());
			if (mSlideView.isExit()) {
				BitmapUtils.recycleBitmap(mDefaultBmp);
			}
		}
	}

	private Bitmap generateDefaultBmp(int width, int height, int colorId, int paddingBottom) {
		if (width <= 0 || height <= 0) {
			return null;
		}
		Bitmap defaultBmp;
		Resources res = mContext.getResources();
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
//		for (int i = 0; i < mNewDataList.size(); i++) {
//			downBitmap(mNewDataIconsManager, mNewDataList.get(i), i * PIC_COUNT_PER_ITEM);
//		}
	}

	private void downBitmap(AdItem item, int index) {
		mImageLoader.displayImage(iconUrl, view, defBitmapId);.getBitmap(index, item.mImageUrl);
		if (item.mArgs != null) {
			iconsManager.getBitmap(index + 1, item.mArgs.mIconUrl);
		}
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
				adItem.mTitle = slideObject.getString(SlideConstant.TITLE);
				adItem.mImageUrl = slideObject.getString(SlideConstant.IMAGE_URL);
				if (!StringUtil.isUrlValid(adItem.mImageUrl)) {
					adItem.mImageUrl = "";
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
		String data = sp.getString(source, "");
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
		return mShowingIconsManager.getBitmap(index * PIC_COUNT_PER_ITEM, getShowingAdItem(index).mImageUrl);
	}

	protected Bitmap getIconBitmap(int index) {
		return mShowingIconsManager.getBitmap(index * PIC_COUNT_PER_ITEM + 1,
				getShowingAdItem(index).mArgs.mIconUrl);
	}

	protected String getSlideTextView(int index) {
		return getShowingAdItem(index).mTitle;
	}

	protected void switchNext() {
		mCurIndex = getNextIndex();
	}

	protected void onItemClick() {
		if (mShowingList.isEmpty()) {
			return;
		}

		AdItem adItem = getShowingAdItem(mCurIndex);
		String type = adItem.mViewType;
		String param = adItem.mParam;
		ViewTypeUtil.onClickNavigations(mContext, type, param, createSource(adItem.mId));
	}

	private String createSource(String adid) {
		String preSource = StatisValue.AD1 + (mCurIndex + 1);
		String curSource = StatisValue.AD_ID + adid;
		String source = StatisValue.combine(preSource, curSource);
		return combinePageSource(source);
	}

	private String combinePageSource(String source) {
		String pageSource = StatisSourceManager.getInstance().getCurSource();
		if (TextUtils.isEmpty(source) || pageSource.contains(StatisValue.HOME)) {
			return source;
		}

		return StatisValue.combine(pageSource, source);
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
		if (mCurIndex >= mShowingList.size()) {
			mCurIndex = 0;
		}
	}

}
