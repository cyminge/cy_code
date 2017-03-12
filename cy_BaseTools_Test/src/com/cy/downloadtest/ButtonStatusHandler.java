package com.cy.downloadtest;

import android.app.Activity;
import android.widget.ListView;

import com.cy.downloadtest.GameListAdapter.GameViewHolder;
import com.cy.frame.downloader.core.DownloadManager;
import com.cy.frame.downloader.core.DownloadManager.DownloadChangeListener;
import com.cy.frame.downloader.entity.GameBean;

/**
 * 下载按钮状态的处理
 * @author JLB6088
 *
 */
public class ButtonStatusHandler {

    private Activity mActivity;
    private ListView mView;
    private GameListAdapter mAdapter;

    private boolean mOnFlingScroll = false;

    /**
     * 是否正在滑动
     * @param isFlingScroll
     */
    public void setFlingScroll(boolean isFlingScroll) {
        mOnFlingScroll = isFlingScroll;
    }

    public ButtonStatusHandler(Activity activity, ListView view, GameListAdapter adapter) {
        mActivity = activity;
        mView = view;
        mAdapter = adapter;
    }

    /**
     * 下载进度回调
     */
    private DownloadChangeListener mChangeListener; // StatusChangeListener

    public void initStatusChangeListener() {
        if (mChangeListener == null) {
            mChangeListener = new DownloadChangeListener() {
                @Override
                public void onDownloadChange() {
                    if (mActivity == null || !mActivity.hasWindowFocus()) {
                        return;
                    }
                    updateDownloadStatus();
                }
            };
        }
        DownloadManager.getNormalInstance().addChangeListener(mChangeListener);
    }

    private void updateDownloadStatus() {
        if (mOnFlingScroll || mActivity.isFinishing()) {
            return;
        }

        mView.post(new Runnable() {
            @Override
            public void run() {
                updateDownloadButtonStatus();
            }
        });
        
    }

    private void updateDownloadButtonStatus() {
        int firstVisiblePosition = mView.getFirstVisiblePosition();
        int lastVisiblePosition = mView.getLastVisiblePosition();
        for (int position = firstVisiblePosition; position <= lastVisiblePosition; position++) {
            try {
                updateDownloadButton(position);
            } catch (Exception e) {
            }
        }
    }

    private void updateDownloadButton(int position) {
        int headerViewCount = mView.getHeaderViewsCount();
        int realPosition = position - headerViewCount;
        if (realPosition < 0) {
            return;
        }
        GameBean gameListDataCategory = mAdapter.getItem(realPosition);
        if (gameListDataCategory == null) {
            return;
        }

        Object listData = getItemData(realPosition);
        if (listData == null) {
            return;
        }

        Object viewHolderTag = mView.getChildAt(position - mView.getFirstVisiblePosition()).getTag();
        if (viewHolderTag == null) {
            return;
        }
        updateProgressButton(viewHolderTag, listData, realPosition);
    }

    private Object getItemData(int holderPosition) {
        return mAdapter.getItem(holderPosition);
    }

    private void updateProgressButton(Object viewHolderTag, Object listData, int position) {
        GameViewHolder viewHolder = (GameViewHolder) viewHolderTag;
        if (canUpdateProgressButton(viewHolder, position)) {
            viewHolder.setButtonState(listData);
        }
    }

    private boolean canUpdateProgressButton(GameViewHolder viewHolder, int currentPos) {
        int pos = getProgressButtonTag(viewHolder);
        return pos == currentPos;
    }

    private int getProgressButtonTag(GameViewHolder viewHolder) {
        if (viewHolder == null) {
            return -1;
        }
        return viewHolder.getHolderPosition();
    }

}
