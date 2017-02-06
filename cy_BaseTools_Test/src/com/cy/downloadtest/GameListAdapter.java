package com.cy.downloadtest;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.ButtonStatusManager;
import com.cy.frame.downloader.controller.DownloadClickHelper;
import com.cy.frame.downloader.controller.DownloadClickHelper.DownloadClickCallback;
import com.cy.frame.downloader.controller.SingleDownloadManager.SingleDownloadListener;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.entity.GameBean;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.global.WatchDog;
import com.cy.test.R;

public class GameListAdapter extends BaseAdapter {

    private Context mContext;

    public ArrayList<GameBean> mDataList = new ArrayList<GameBean>();

    private DownloadClickHelper mClickHelper;

    public GameListAdapter(Context context) {
        mContext = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GameViewHolder holder = null;
        if (convertView == null) {
            holder = new GameViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.game_list_adapter, null);
            holder.progressButton = (IProgressButton) convertView.findViewById(R.id.game_list_button);
            holder.gameName = (TextView) convertView.findViewById(R.id.game_name);

            convertView.setTag(holder);
        } else {
            holder = (GameViewHolder) convertView.getTag();
        }

        holder.progressButton.setTag(position);
        holder.setButtonState(mDataList.get(position));
        holder.progressButton.setOnClickListener(mHolderClickListener);
        holder.gameName.setText(((mDataList.get(position))).name);

        return convertView;
    }

    public class GameViewHolder {
        IProgressButton progressButton;
        TextView gameName;

        /**
         * 设置按钮状态
         * 
         * @param data
         */
        public void setButtonState(Object data) {
            GameBean listData = (GameBean) data;
            listData.mStatus = getButtonStatus(listData);
            progressButton.setButton(listData, listData.mStatus, ButtonStatusManager.getProgress(listData));
        }

        public int getButtonStatus(GameBean data) {
            return ButtonStatusManager.getButtonStatus(data);
        }

        /**
         * 获取当前位置
         * 
         * @return
         */
        public int getHolderPosition() {
            return (Integer) progressButton.getTag();
        }
    }

    View.OnClickListener mHolderClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            onViewHolderClick(view, id);
        }
    };

    protected void onViewHolderClick(View view, int id) {
        
        if (!(view instanceof IProgressButton)) {
            return;
        }

        if (mClickHelper == null) {
            mClickHelper = new DownloadClickHelper((Activity) mContext, new DownloadClickCallback() {
                @Override
                public void onResumeDownload() {
                }

                @Override
                public void onPauseDownload() {
                }

                @Override
                public void onInstall(boolean result, IProgressButton button, DownloadArgs data) {
                    manualInstall(button, data, result);
                }

                @Override
                public SingleDownloadListener getSingleDownloadListener(IProgressButton button, DownloadArgs data) {
                    return createDownloadListener(button, data);
                }
            });
        }

        Integer position = (Integer) view.getTag();
        Object data = mDataList.get(position);

        GameBean dataItem = (GameBean) data;
        mClickHelper.clickHandle((IProgressButton) view, dataItem);
    }
    
    private void manualInstall(IProgressButton button, DownloadArgs data, boolean success) {
        if (data instanceof GameBean) {
        	GameBean listData = (GameBean) data;
            if (success) {
                listData.mStatus = ButtonStatusManager.BUTTON_STATUS_INSTALLING;
            } else {
                listData.mStatus = ButtonStatusManager.BUTTON_STATUS_DOWNLOAD;
            }
            button.setButton(listData, listData.mStatus, ButtonStatusManager.getProgress(listData));
            notifyDataSetChanged();
        }
    }

    private SingleDownloadListener createDownloadListener(final IProgressButton button,
            final DownloadArgs data) {
        return new SingleDownloadListener() {

            @Override
            public void onStartDownload(Long downId, DownloadArgs downloadArgs) {
                // showDownloadAnimation((View) button, data);
                WatchDog.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, Constant.DOWNLOAD_ANIMATION_TIME);
                // RewardDialogMgr.onStartDownload(getActivity(), data, new
                // DialogInterface.OnDismissListener() {
                // @Override
                // public void onDismiss(DialogInterface dialog) {
                // GNBaseActivity activity = getActivity();
                // if (activity != null) {
                // activity.showGiftToast((GameListData) data);
                // }
                // }
                // });
            }

            @Override
            public void onResetDownload(DownloadArgs downloadArgs) {
                notifyDataSetChanged();
            }

            @Override
            public void onInstalling(DownloadArgs downloadArgs) {
                button.setText("安装中");
            }
        };
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public GameBean getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        synchronized (mDataList) {
            return mDataList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
