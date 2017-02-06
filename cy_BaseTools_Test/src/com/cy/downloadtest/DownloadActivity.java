package com.cy.downloadtest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.cy.frame.downloader.entity.GameBean;
import com.cy.test.R;

public class DownloadActivity extends Activity {

    private GameListAdapter mAdapter;

    private ButtonStatusHandler mButtonStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDownloaderView();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 9527:
                ArrayList<GameBean> list = DataManager.getData();
                Log.e("cyTest", "数据长度：" + list.size());
                if (!list.isEmpty()) {
                    mAdapter.mDataList = list;
                    mAdapter.notifyDataSetChanged();
                }
                break;
            }
        };
    };

    private void initDownloaderView() {
        setContentView(R.layout.activity_download_main);
        ListView download_list_view = (ListView) findViewById(R.id.download_list_view);
        mAdapter = new GameListAdapter(this);
        download_list_view.setAdapter(mAdapter);

        mButtonStatusHandler = new ButtonStatusHandler(this, download_list_view, mAdapter);
        mButtonStatusHandler.initStatusChangeListener();

        download_list_view.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                boolean state = (scrollState == OnScrollListener.SCROLL_STATE_FLING);
                mButtonStatusHandler.setFlingScroll(state);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {

            }
        });

        mHandler.sendEmptyMessageDelayed(9527, 1000);
    }

    
}
