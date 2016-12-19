package com.cy.uiframe.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

public abstract class PullToRefreshBase<T extends View> extends RelativeLayout {
	
	public static final int MILLIS_500 = 500;
	public static final int MILLIS_200 = 200;

    private static final float FRICTION = 2.0f;
    private static final int HEADER_LAYOUT_ID = 0x123456;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionY;
    private float mDownMotionY;
//    private float mRefreshingDownScrollY;
//    private float mRefreshingDownY;
//    private boolean mRefreshingScrolling = false;

    protected boolean mPullRefreshEnable = true;

    private boolean mIsBeingDragged = false;
    private State mState = State.RESET;

    protected T mRefreshableView;

    private Interpolator mScrollAnimationInterpolator;

    private LoadingLayout mHeaderLayout;

    private OnRefreshListener<T> mOnRefreshListener;

    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    
    private Context mContext;

    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs, T contentView) {
        super(context, attrs);
        init(context, attrs, contentView);
    }

    public final T getRefreshableView() {
        return mRefreshableView;
    }

    public final boolean isPullToRefreshOverScrollEnabled() {
        return VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD && isAndroidOverScrollEnabled(mRefreshableView);
    }

    @SuppressLint("NewApi") 
    private boolean isAndroidOverScrollEnabled(View view) {
        return view.getOverScrollMode() != View.OVER_SCROLL_NEVER;
    }

    public final boolean isRefreshing() {
        return mState == State.REFRESHING || mState == State.MANUAL_REFRESHING;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDownMotionY = event.getY();
            // onRefreshingDownEvent(event);
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // onRefreshingUpEvent();
        }

        if (action == MotionEvent.ACTION_MOVE && isRefreshing()) {
            // onRefreshingMoveEvent(event);
        } else if (isReadyForPullStart() && isReadyForPull(event)) {
            mLastMotionY = mDownMotionY;
            onInterceptTouchEvent(event);
        } else if (mIsBeingDragged) {
            return onTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

//    private void onRefreshingDownEvent(MotionEvent event) {
//        if (isRefreshing()) {
//            mRefreshingDownScrollY = mHeaderLayout.getScrollY();
//            mRefreshingDownY = event.getY();
//            mHeaderLayout.checkRefreshing();
//        }
//        mRefreshingScrolling = false;
//    }
//
//    private void onRefreshingUpEvent() {
//        if (!isRefreshing()) {
//            return;
//        }
//
//        if (!isReadyForPullStart()) {
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    setHeaderScroll(0);
//                }
//            }, Constant.MILLIS_100);
//        } else if (mHeaderLayout.getScrollY() < 0) {
//            smoothScrollTo(-getHeaderSize());
//        }
//    }
//
//    private void onRefreshingMoveEvent(MotionEvent event) {
//        int distance = (int) (mRefreshingDownY - event.getY());
//        if (!isRefreshingScrolling(distance)) {
//            return;
//        }
//        if (distance - getHeaderSize() >= 0) {
//            setHeaderScroll(0);
//            return;
//        }
//
//        final int maxPullScroll = getMaxPullScroll();
//        float curScrollY = mRefreshingDownScrollY;
//        if (curScrollY <= -getHeaderSize() || curScrollY == 0) {
//            if (distance < -curScrollY - maxPullScroll) {
//                mRefreshingDownY = event.getY() - curScrollY - maxPullScroll;
//                setHeaderScroll(-maxPullScroll);
//                return;
//            } else if (!isReadyForPullStart()) {
//                mRefreshingDownY = event.getY() - curScrollY;
//                return;
//            }
//
//            setHeaderScroll(distance + (int) curScrollY);
//        }
//    }
//
//    private boolean isRefreshingScrolling(int distance) {
//        if (Math.abs(distance) > mTouchSlop) {
//            mRefreshingScrolling = true;
//        }
//        return mRefreshingScrolling;
//    }

    private boolean isReadyForPull(MotionEvent event) {
        int action = event.getAction();
        float diff = event.getY() - mDownMotionY;

        return diff >= mTouchSlop && !mIsBeingDragged && action == MotionEvent.ACTION_MOVE;
    }

    public final boolean onInterceptTouchEvent(MotionEvent event) {

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (isRefreshing()) {
                    return false;
                }
                if (isReadyForPullStart()) {
                    final float y = event.getY();
                    final float x = event.getX();
                    final float diff;
                    final float oppositeDiff;
                    final float absDiff;
                    diff = y - mLastMotionY;
                    oppositeDiff = x - mLastMotionX;
                    absDiff = Math.abs(diff);
                    if (absDiff > mTouchSlop && (absDiff > Math.abs(2 * oppositeDiff))) {
                        if (diff >= 1f) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (isReadyForPullStart()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                    mLastMotionX = event.getX();
                    mIsBeingDragged = false;
                }
                break;
            default:
                break;
        }
        return mIsBeingDragged;
    }

    public final void onRefreshComplete() {
        if (isRefreshing()) {
            setState(State.RESET);
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    pullRefreshComplete();
                }
            }, getCompletedDelay());
        }
    }

    protected int getCompletedDelay() {
        return MILLIS_500;
    }

    public final boolean onTouchEvent(MotionEvent event) {

        if (isRefreshing()) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    pullEvent();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mInitialMotionY = event.getY();
                    mLastMotionY = mInitialMotionY;
                    mLastMotionX = event.getX();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;

                    if (mState == State.RELEASE_TO_REFRESH && (null != mOnRefreshListener)) {
                        setState(State.REFRESHING);
                        return true;
                    }

                    setState(State.RESET);

                    return true;
                }
                break;
            }
        }

        return false;
    }

    public void setLongClickable(boolean longClickable) {
        getRefreshableView().setLongClickable(longClickable);
    }

    public final void setOnRefreshListener(OnRefreshListener<T> listener) {
        mOnRefreshListener = listener;
    }

    private void setState(State state) {
        if (mState == state) {
            return;
        }
        mState = state;
        switch (mState) {
            case RESET:
                onReset();
                break;
            case PULL_TO_REFRESH:
                onPullToRefresh();
                break;
            case RELEASE_TO_REFRESH:
                onReleaseToRefresh();
                break;
            case REFRESHING:
            case MANUAL_REFRESHING:
                onRefreshing();
                break;
            case OVERSCROLLING:
                // NO-OP
                break;
        }

    }

    public void startPullRefreshing() {
        setState(State.REFRESHING);
        smoothScrollTo(-getHeaderSize(), getPullToRefreshScrollDuration());
    }

    protected LoadingLayout createLoadingLayout(Context context) {
        LoadingLayout layout = new LoadingLayout(context);
        layout.setVisibility(View.INVISIBLE);
        return layout;
    }

    protected abstract T createRefreshableView(Context context, AttributeSet attrs);

    public final int getHeaderSize() {
        return mHeaderLayout.getContentSize();
    }

    protected int getPullToRefreshScrollDuration() {
        return MILLIS_200;
    }

    protected abstract boolean isReadyForPullStart();

    protected abstract void pullRefreshBegin();

    protected abstract void pullRefreshComplete();

    protected void onPullToRefresh() {
        mHeaderLayout.pullToRefresh();
    }

    protected void onRefreshing() {
        mHeaderLayout.refreshing();

        OnSmoothScrollFinishedListener listener = new OnSmoothScrollFinishedListener() {
            // @Override
            public void onSmoothScrollFinished() {
                callRefreshListener();
            }
        };
        smoothScrollTo(-getHeaderSize(), listener);
    }

    protected void onReleaseToRefresh() {
        mHeaderLayout.releaseToRefresh();
    }

    protected void onReset() {
        mIsBeingDragged = false;

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mHeaderLayout.reset();
            }
        }, getPullToRefreshScrollDuration());

        smoothScrollTo(0);
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        refreshLoadingViewsSize();

        post(new Runnable() {
            // @Override
            public void run() {
                requestLayout();
            }
        });
    }

    protected final void refreshLoadingViewsSize() {
        final int maxPullScroll = (int) (getMaxPullScroll() * 1.2f);

        int pLeft = getPaddingLeft();
        int pTop = -mHeaderLayout.getRefreshingIconSize();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();

        mHeaderLayout.setHeight(maxPullScroll);
        mHeaderLayout.setPadding(pLeft, pTop, pRight, pBottom);
    }

    protected final void setHeaderScroll(int value) {
        final int maximumPullScroll = getMaxPullScroll();
        value = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, value));

        if (value < 0) {
            mHeaderLayout.setVisibility(View.VISIBLE);
        } else {
            mHeaderLayout.setVisibility(View.INVISIBLE);
        }

        mHeaderLayout.offsetYChange(value);
        mHeaderLayout.scrollTo(0, value);
    }

    protected final void smoothScrollTo(int scrollValue) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDuration());
    }

    protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDuration(), 0, listener);
    }

    protected void updateUIForMode() {
        removeView(mHeaderLayout);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mHeaderLayout.setId(HEADER_LAYOUT_ID);
        addView(mHeaderLayout, params);

        refreshLoadingViewsSize();
    }

    private void addRefreshableView(T refreshableView) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(refreshableView, params);
    }

    private void init(Context context, AttributeSet attrs, T contentView) {
    	mContext = context;
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        if (contentView != null) {
            mRefreshableView = contentView;
        } else {
            mRefreshableView = createRefreshableView(context, attrs);
        }

        mHeaderLayout = createLoadingLayout(context);
        prepareDrawLayout();
        addRefreshableView(mRefreshableView);
        updateUIForMode();
    }

    protected void prepareDrawLayout() {
    }

    private void callRefreshListener() {
        if (null != mOnRefreshListener) {
            mOnRefreshListener.onRefresh(this);
            pullRefreshBegin();
            if (hasNetwork()) {
                sendStatis();
            }
        }
    }
    
    private NetworkInfo getActiveNetworkInfo() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivity.getActiveNetworkInfo();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasNetwork() {
        return getActiveNetworkInfo() != null;
    }

    /**
     * 什么飞机???????
     */
    private void sendStatis() {
//        String pageNum = StatisValue.PAGE_COUNT + 1;
//        String object = StatisValue.combine(StatisSourceManager.getInstance().getCurSource(), pageNum);
//        StatisHelper.get().send(StatisValue.DOWN_REFRESH, object);
    }

    private void pullEvent() {
        final int newScrollValue;
        final int itemDimension;
        final float initialMotionValue;
        final float lastMotionValue;

        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;
        newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);
        itemDimension = getHeaderSize();

        setHeaderScroll(newScrollValue);
        if (newScrollValue != 0 && !isRefreshing()) {
            if (mState != State.PULL_TO_REFRESH && itemDimension >= Math.abs(newScrollValue)) {
                setState(State.PULL_TO_REFRESH);
            } else if (mState != State.RELEASE_TO_REFRESH && itemDimension < Math.abs(newScrollValue)) {
                setState(State.RELEASE_TO_REFRESH);
            }
        }
    }

    private int getMaxPullScroll() {
        return Math.round(getHeight() / FRICTION);
    }

    private void smoothScrollTo(int scrollValue, long duration) {
        smoothScrollTo(scrollValue, duration, 0, null);
    }

    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
            OnSmoothScrollFinishedListener listener) {
        if (null != mCurrentSmoothScrollRunnable) {
            mCurrentSmoothScrollRunnable.stop();
        }

        final int oldScrollValue = mHeaderLayout.getScrollY();

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration,
                    listener);

            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            } else {
                post(mCurrentSmoothScrollRunnable);
            }
        } else {
            if (null != listener) {
                listener.onSmoothScrollFinished();
            }
        }
    }

    public static interface OnRefreshListener<V extends View> {

        public void onRefresh(final PullToRefreshBase<V> refreshView);

    }

    public static enum Orientation {
        VERTICAL, HORIZONTAL
    }

    public static enum State {

        RESET(0x0),

        PULL_TO_REFRESH(0x1),

        RELEASE_TO_REFRESH(0x2),

        REFRESHING(0x8),

        MANUAL_REFRESHING(0x9),

        OVERSCROLLING(0x10);

        private int mIntValue;

        State(int intValue) {
            mIntValue = intValue;
        }

        int getIntValue() {
            return mIntValue;
        }
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private boolean mListenerCalled = false;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float calculations. We use 1000 as it
                 * gives us good accuracy and small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                setHeaderScroll(mCurrentY);
            }

            if (mContinueRunning && mScrollToY != mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16L);
            } else {
                mListenerCalled = true;
                if (null != mListener) {
                    mListener.onSmoothScrollFinished();
                }
            }
        }

        public void stop() {
            mContinueRunning = false;
            if (!mListenerCalled && null != mListener) {
                mListener.onSmoothScrollFinished();
            }
            removeCallbacks(this);
        }
    }

    static interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    public void setPullRefreshEnable(boolean enable) {
        mPullRefreshEnable = enable;
    }

}
