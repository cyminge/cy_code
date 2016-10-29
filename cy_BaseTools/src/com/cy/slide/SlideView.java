package com.cy.slide;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.cy.R;
import com.cy.slide.AnimationComputer.OnAnimFinishListener;
import com.cy.slide.SlideViewHelper.DataParsedListener;

public class SlideView extends RecyclableView implements ISlideView {

    private static final long NEXT_SWITCH_DELAY = DateUtils.SECOND_IN_MILLIS * 4;
    private static final int SWITCH_ANIMATION_TIME = 500;
    private static final String TEXT_SUFFIX = "...";
    private int mIndexGap;
    private int mIndexPadding;
    private int mIndexTop;
    private int mIndexWidth;
    private int mTextMarginBottom;
    private int mMaxTextNum;
    private int mPaddingBottom;
    private Rect mRect;
    private AnimationComputer mAnimationComputer;
    private Paint mTextPaint;
    private Drawable mIndexDefault;
    private Drawable mIndexCurrent;
    private SlideViewEventAdapter mEventAdapter;
    private float mTextSize;
    private float mTextMarginHorizental;

    private RectF mDownloadRect;
    private Paint mDownloadPaint;
    private int mIconSize;
    private int mDownloadHeight;
    private int mDownloadMarginLeft;
    private int mDownloadMarginTop;
    private int mIconMarginTop;
    private float mTextOffset;
    private boolean mCanDownload;
    private int mSaveDoloadLayerCount;
    private boolean mInDownload = false;

    private int mHeight;
    private int mWidth;

    private SlideViewHelper mHelper;
    private DownloadChangeListener mChangeListener;

    private OnAnimFinishListener mFinishListener = new OnAnimFinishListener() {

        @Override
        public void onFinish(float curPosition) {
            postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
        }
    };

    private Runnable mSwitchCommand = new Runnable() {

        @Override
        public void run() {
            startAnimation();
        }
    };

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        synchronized (this) {
            mHelper = new SlideViewHelper(this, context);
            mAnimationComputer = new AnimationComputer(new DecelerateInterpolator(), mFinishListener);
            mEventAdapter = new SlideViewEventAdapter(getContext(), this, true, mAnimationComputer);

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChosenSlideView);
            mPaddingBottom = (int) typedArray.getDimension(R.styleable.ChosenSlideView_paddingBottom, 0);
            mCanDownload = typedArray.getBoolean(R.styleable.ChosenSlideView_canDownload, true);
            typedArray.recycle();

            initTextValue();
            initIndexValue();
            initDownloadValue();
            initDownloadListener();
        }
    }

    private void initTextValue() {
        mRect = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setColor(Utils.getResources().getColor(R.color.default_bitmap_text_color));
        mTextSize = getResources().getDimension(R.dimen.slide_textview_size);
        mTextMarginHorizental = getResources().getDimension(R.dimen.slide_textview_margin_horizontal);
        mTextPaint.setTextSize(mTextSize);
        mTextMarginBottom = (int) getResources().getDimension(R.dimen.slide_textview_marginbottom);
    }

    private void initIndexValue() {
        Resources res = getResources();
        mIndexDefault = res.getDrawable(R.drawable.index_default);
        mIndexCurrent = res.getDrawable(R.drawable.index_current);
        mIndexGap = (int) res.getDimension(R.dimen.slideview_index_gap);
        mIndexPadding = (int) res.getDimension(R.dimen.slideview_index_padding);
        mIndexTop = (int) res.getDimension(R.dimen.slideview_index_top);
    }

    private void initDownloadValue() {
        mDownloadRect = new RectF();
        mDownloadPaint = new Paint();
        mDownloadPaint.setTextSize(Utils.getDimenPx(R.dimen.banner_download_text_size));
        mDownloadPaint.setAntiAlias(true);
        mIconSize = Utils.getDimenPx(R.dimen.game_download_width);
        mDownloadHeight = Utils.getDimenPx(R.dimen.game_download_height);
        mDownloadMarginLeft = Utils.getDimenPx(R.dimen.banner_download_margin_left);
        mDownloadMarginTop = Utils.getDimenPx(R.dimen.banner_download_margin_top);
        mIconMarginTop = Utils.getDimenPx(R.dimen.banner_icon_margin_top);
        mTextOffset = Utils.getTextY(mDownloadHeight, mDownloadPaint);
    }

    private void initDownloadListener() {
        if (!mCanDownload) {
            return;
        }

        if (mChangeListener == null) {
            mChangeListener = new DownloadChangeListener() {
                @Override
                public void onDownloadChange() {
                    postInvalidate();
                }
            };
        }
        DownloadInfoMgr.getNormalInstance().addChangeListener(mChangeListener);
    }

    public boolean setDataSource(String source, JSONObject json, DataParsedListener listener) {
        synchronized (this) {
            return mHelper.setDataSource(source, json, listener);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        synchronized (this) {
            mWidth = right - left;
            mHeight = bottom - top;
            mMaxTextNum = (int) ((mWidth - mTextMarginHorizental * 2) / mTextSize);
            mHelper.initSlideBitmap();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (this) {
            if (isDownloadIn(event)) {
                mInDownload = true;
                removeCallbacks(mSwitchCommand);
                invalidate();
                return true;
            }
            if (isDownloadOut(event)) {
                mInDownload = false;
                invalidate();
                return true;
            }
            if (isDownloadUp(event)) {
                mInDownload = false;
                startDownload();
                prepareAnimation();
                invalidate();
                return true;
            }

            if (mEventAdapter.onTouchEvent(event)) {
                return true;
            }
            return super.onTouchEvent(event);
        }
    }

    private void startDownload() {
        synchronized (this) {
            mHelper.startDownload();
        }
    }

    protected boolean isDownloadUp(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_UP && mInDownload;
    }

    private boolean isDownloadIn(MotionEvent event) {
        if (!canDownload(mHelper.getCurIndex()) || !mAnimationComputer.isFinish()
                || event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        return inDownloadView(event);
    }

    private boolean isDownloadOut(MotionEvent event) {
        if (!mInDownload) {
            return false;
        }
        return !inDownloadView(event);
    }

    protected boolean inDownloadView(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return x >= mWidth - mDownloadMarginLeft - mIconSize && x <= mWidth - mDownloadMarginLeft
                && y >= mDownloadMarginTop && y <= mDownloadMarginTop + mDownloadHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            if (mWidth == 0 || mHeight == 0 || mHelper.isEmpty()) {
                return;
            }
            float xOffset = caculateXOffset();
            drawSlideItem(canvas, xOffset);
            drawIndex(canvas);
            if (mAnimationComputer != null && !mAnimationComputer.isFinish()) {
                invalidate();
            } else {
                mHelper.checkAllImageDone();
            }
        }
    }

    private float caculateXOffset() {
        if (isSingle()) {
            return 0f;
        }
        if (mEventAdapter.isGestureSliding()) {
            return mEventAdapter.getOffsetX();
        }

        return mAnimationComputer.getCurrentPosition();
    }

    private void drawSlideItem(Canvas canvas, float xOffset) {
        drawItem(canvas, 0, mHelper.getCurIndex(), xOffset);
        if (isSingle()) {
            return;
        }

        if (FloatUtil.isPositive(xOffset)) {
            drawItem(canvas, -1, mHelper.getPreIndex(), xOffset);
        } else if (FloatUtil.isNegative(xOffset)) {
            drawItem(canvas, 1, mHelper.getNextIndex(), xOffset);
        }
    }

    private void drawItem(Canvas canvas, int pageOffset, int index, float xOffset) {
        Bitmap bitmap = mHelper.getSlideBitmap(index);
        if (BitmapUtil.isBitmapEmpty(bitmap)) {
            String title = mHelper.getSlideTextView(index);
            canvas.drawBitmap(mHelper.getDefaultBitmap(), xOffset + mWidth * pageOffset, 0,
                    BitmapManager.HIGH_PAINT);
            drawTextView(canvas, pageOffset, title, xOffset);
        } else {
            canvas.drawBitmap(bitmap, xOffset + mWidth * pageOffset, 0, BitmapManager.HIGH_PAINT);
        }
        drawDownload(canvas, pageOffset, index, xOffset);
    }

    private void drawDownload(Canvas canvas, int pageOffset, int index, float xOffset) {
        if (!canDownload(index)) {
            return;
        }

        mDownloadRect.top = mIconMarginTop;
        mDownloadRect.bottom = mDownloadRect.top + mIconSize;
        mDownloadRect.left = xOffset + mWidth * pageOffset + mWidth - mDownloadMarginLeft - mIconSize;
        mDownloadRect.right = mDownloadRect.left + mIconSize;
        drawIcon(canvas, index);
        drawBackground(canvas, index);
        drawProgressBar(canvas, index);
        drawProgressText(canvas, index);
        drawShade(canvas);
    }

    private void drawIcon(Canvas canvas, int index) {
        Bitmap bitmap = mHelper.getIconBitmap(index);
        if (!BitmapUtil.isBitmapEmpty(bitmap)) {
            canvas.drawBitmap(bitmap, null, mDownloadRect, BitmapManager.HIGH_PAINT);
        }
    }

    private void drawBackground(Canvas canvas, int index) {
        mDownloadRect.top = mDownloadMarginTop;
        mDownloadRect.bottom = mDownloadRect.top + mDownloadHeight;
        Bitmap bitmap = initBackgroundBitmap(index);
        mSaveDoloadLayerCount = canvas.saveLayer(null, mDownloadPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(bitmap, mDownloadRect.left, mDownloadRect.top, mDownloadPaint);

    }

    private Bitmap initBackgroundBitmap(int index) {
        GradientDrawable drawable = (GradientDrawable) Utils.getResources().getDrawable(mHelper
                .getDownloadBg(index));
        if (null == drawable) {
            return null;
        }
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 :
                Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(mIconSize, mDownloadHeight, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mIconSize, mDownloadHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    private void drawProgressBar(Canvas canvas, int index) {
        int progressBg = mHelper.getProgressBg(index);
        if (progressBg == 0) {
            return;
        }
        mDownloadPaint.setColor(progressBg);
        mDownloadPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        float progressLen = mHelper.getProgress(index) * mIconSize;
        mDownloadRect.right = mDownloadRect.left + progressLen;
        canvas.drawRect(mDownloadRect, mDownloadPaint);
        mDownloadPaint.setXfermode(null);
        canvas.restoreToCount(mSaveDoloadLayerCount);

    }

    private void drawShade(Canvas canvas) {
        /*if (mInDownload) {
            mDownloadPaint.setColor(mHelper.getShadeBg());
            mDownloadRect.right = mDownloadRect.left + mIconSize;
            canvas.drawRect(mDownloadRect, mDownloadPaint);
        }*/
    }

    private void drawProgressText(Canvas canvas, int index) {
        String content = mHelper.getDownloadText(index);
        mDownloadPaint.setColor(mHelper.getDownloadTextBg());
        mDownloadPaint.getTextBounds(content, 0, content.length(), mRect);
        float textWidth = mRect.width();
        canvas.drawText(content, mDownloadRect.left + (mIconSize - textWidth) / 2, mDownloadRect.top
                + mTextOffset, mDownloadPaint);
    }

    private void drawTextView(Canvas canvas, int pageOffset, String content, float xOffset) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        if (content.length() > mMaxTextNum) {
            content = content.substring(0, mMaxTextNum) + TEXT_SUFFIX;
        }
        mTextPaint.getTextBounds(content, 0, content.length(), mRect);
        float textHeight = mRect.height();
        float textWidth = mRect.width();
        canvas.drawText(content, xOffset + mWidth * pageOffset + (mWidth - textWidth) / 2, mHeight
                - textHeight - mTextMarginBottom - mPaddingBottom, mTextPaint);
    }

    private void drawIndex(Canvas canvas) {
        if (mHelper.isEmpty() || mHelper.isSingle()) {
            return;
        }

        int left = (mWidth - mIndexWidth) / 2;
        int top = mHeight - mIndexTop - mPaddingBottom;
        int indexWidth = mIndexDefault.getIntrinsicWidth();
        int indexHeight = mIndexDefault.getIntrinsicHeight();
        left += mIndexPadding;
        for (int i = 0; i < getCount(); i++) {
            Drawable drawable;
            if (i == mHelper.getCurIndex()) {
                drawable = mIndexCurrent;
            } else {
                drawable = mIndexDefault;
            }
            drawable.setBounds(left, top, left + indexWidth, top + indexHeight);
            drawable.draw(canvas);
            left += (indexWidth + mIndexGap);
        }
    }

    public void recycle() {
        mHelper.recycle();
    }

    @Override
    public void exit() {
        super.exit();
        mHelper.exit();
        if (mChangeListener != null) {
            DownloadInfoMgr.getNormalInstance().removeChangeListener(mChangeListener);
        }
    }

    public boolean isSingle() {
        synchronized (this) {
            return mHelper.isSingle();
        }
    }

    protected void resetState() {
        removeCallbacks(mSwitchCommand);
    }

    protected void resetIndexWidth() {
        int size = getCount();
        mIndexWidth = 2 * mIndexPadding + size * mIndexDefault.getIntrinsicWidth() + (size - 1) * mIndexGap;
    }

    protected void prepareAnimation() {
        removeCallbacks(mSwitchCommand);
        if (isSingle()) {
            return;
        }

        postDelayed(mSwitchCommand, NEXT_SWITCH_DELAY);
    }

    private void startAnimation() {
        synchronized (this) {
            mHelper.switchNext();
            mAnimationComputer.start(mWidth, 0, SWITCH_ANIMATION_TIME);
        }
        invalidate();
    }

    public void setGestureDetectorEnabled(boolean enable) {
        mEventAdapter.setGestureDetectorEnabled(enable);
    }

    public boolean switchImage(boolean isLeft) {
        synchronized (this) {
            mInDownload = false;
            mHelper.switchImage(isLeft);
        }
        return true;
    }

    public void onSingleTapUp() {
        onItemClick();
        prepareAnimation();
    }

    private void onItemClick() {
        synchronized (this) {
            mHelper.onItemClick();
        }
    }

    public void onBeginScroll() {
        synchronized (this) {
            mInDownload = false;
        }
        removeCallbacks(mSwitchCommand);
    }

    public int getViewWidth() {
        return mWidth;
    }

    public int getViewHeight() {
        return mHeight;
    }

    public int getViewPaddingBottom() {
        return mPaddingBottom;
    }

    public int getCurrentIndex() {
        synchronized (this) {
            return mHelper.getCurIndex();
        }
    }

    public int getCount() {
        synchronized (this) {
            return mHelper.getCount();
        }
    }

    private boolean canDownload(int index) {
        synchronized (this) {
            return mCanDownload && mHelper.canDownload(index);
        }
    }

    @Override
    public void update() {
        invalidate();
    }
}
