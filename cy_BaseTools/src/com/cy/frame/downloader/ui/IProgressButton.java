package com.cy.frame.downloader.ui;

import android.view.View;

import com.cy.frame.downloader.download.entity.DownloadArgs;

public interface IProgressButton {
    public void setTag(Object object);

    public Object getTag();

    public void setText(CharSequence charSequence);

    public void setButton(DownloadArgs args, int status, float progress);

    public void setOnClickListener(View.OnClickListener onClickListener);
}
