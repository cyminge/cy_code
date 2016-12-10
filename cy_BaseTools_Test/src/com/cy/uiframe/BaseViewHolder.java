package com.cy.uiframe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cy.R;
import com.cy.frame.downloader.ui.IProgressButton;
import com.cy.imageloader.ui.AlphaAnimImageView;

public class BaseViewHolder extends AbstractViewHolder {
	
	protected AlphaAnimImageView mGameIcon;
    protected TextView mGameName;
    protected IProgressButton mButton;

	@Override
	public void initItemView(View convertView, OnClickListener onClickListener) {
		mGameIcon = (AlphaAnimImageView) convertView.findViewById(R.id.alpha_anim_icon);
        mGameName = (TextView) convertView.findViewById(R.id.game_name);
        mButton = (IProgressButton) convertView.findViewById(R.id.game_list_button);
        mButton.setOnClickListener(onClickListener);
	}

	@Override
	public void setItemView(int position, Object data) {
		
	}
	
}
