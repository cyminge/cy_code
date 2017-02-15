package com.cy.uiframetest.main;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView.OnScrollListener;

import com.cy.frame.downloader.core.DownloadInfoMgr;
import com.cy.frame.downloader.core.DownloadInfoMgr.DownloadChangeListener;
import com.cy.global.WatchDog;
import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerView;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;

/**
 * 下载按钮状态的处理
 * @author JLB6088
 *
 */
public class ButtonStatusHandler<T> {

    private Activity mActivity;
    private AbstractRecyclerView<T> mView;
    private AbstractRececlerAdapter<T> mAdapter;

    private boolean mOnFlingScroll = false;

    /**
     * 是否正在滑动
     * @param isFlingScroll
     */
    private void setFlingScroll(boolean isFlingScroll) {
        mOnFlingScroll = isFlingScroll;
    }

    public ButtonStatusHandler(Activity activity, AbstractRecyclerView<T> view, AbstractRececlerAdapter<T> adapter) {
        mActivity = activity;
        mView = view;
        mAdapter = adapter;
        
        initScrollListener();
        initDownloadStatusChangeListener();
    }
    
    private void initScrollListener() {
    	mView.setOnScrollListener(new RecyclerView.OnScrollListener() {
    		@Override
    		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    			boolean state = (newState == OnScrollListener.SCROLL_STATE_FLING);
                setFlingScroll(state);
    		}
		});
    }

    /**
     * 下载进度回调
     */
    private DownloadChangeListener mChangeListener; // StatusChangeListener

    private void initDownloadStatusChangeListener() {
        if (mChangeListener == null) {
            mChangeListener = new DownloadChangeListener() {
                @Override
                public void onDownloadChange() {
                    if (mActivity == null || mActivity.isFinishing()) {
                        return;
                    }
                    updateDownloadStatus();
                }
            };
        }
        DownloadInfoMgr.getNormalInstance().addChangeListener(mChangeListener);
    }

    private void updateDownloadStatus() {
        if (mOnFlingScroll) {
            return;
        }

        WatchDog.post(new Runnable() {
            @Override
            public void run() {
            	mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 销毁
     */
    public void deinit() {
    	DownloadInfoMgr.getNormalInstance().removeChangeListener(mChangeListener);
    	mAdapter = null;
    	mView = null;
    	mActivity = null;
    }
}
