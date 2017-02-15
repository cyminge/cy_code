package com.cy.uiframe.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.cy.constant.Constant;
import com.cy.frame.downloader.controller.DownloadClickHelper;
import com.cy.frame.downloader.controller.DownloadClickHelper.DownloadClickCallback;
import com.cy.frame.downloader.controller.SingleDownloadManager.SingleDownloadListener;
import com.cy.frame.downloader.download.entity.DownloadArgs;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.global.WatchDog;

public abstract class AbstractRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
	
	private DownloadClickHelper mClickHelper;

	public AbstractRecyclerViewHolder(View itemView) {
		super(itemView);
	}
	
	public abstract void initItemView(View itemView);
	
	public abstract void setItemView(T data);
	
	protected View.OnClickListener mHolderClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            onViewHolderClick(view, id);
        }
    };
    
    protected void onViewHolderClick(View view, int id) {
    	if(view instanceof IProgressButton) {
    		IProgressButton btn = (IProgressButton) view;
    		onDownloadBtnClick(btn);
    	} 
    }

    private void onDownloadBtnClick(IProgressButton btn) {
    	if (mClickHelper == null) {
            mClickHelper = new DownloadClickHelper(new DownloadClickCallback() {
                @Override
                public void onResumeDownload() {
                }

                @Override
                public void onPauseDownload() {
                }

                @Override
                public void onInstall(boolean result, IProgressButton button, DownloadArgs data) {
//                    manualInstall(button, data, result);
                }

                @Override
                public SingleDownloadListener getSingleDownloadListener(IProgressButton button, DownloadArgs data) {
                    return createDownloadListener(button, data);
                }
            });
        }
    	
    	mClickHelper.clickHandle(btn, (DownloadArgs)btn.getTag());
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
//                        notifyDataSetChanged();
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
//                notifyDataSetChanged();
            }

            @Override
            public void onInstalling(DownloadArgs downloadArgs) {
                button.setText("安装中");
            }
        };
    }
    
}
