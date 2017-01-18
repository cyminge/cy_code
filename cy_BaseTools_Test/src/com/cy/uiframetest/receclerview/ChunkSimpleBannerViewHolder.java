package com.cy.uiframetest.receclerview;

import android.view.View;

import com.cy.imageloader.ImageLoader;
import com.cy.imageloader.ui.AlphaAnimImageView;
import com.cy.test.R;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;
import com.cy.uiframetest.bean.ChunkSimpleBannerData;

public class ChunkSimpleBannerViewHolder extends AbstractRecyclerViewHolder<ChunkSimpleBannerData> {

	private AlphaAnimImageView mBannerIcon;
	
	public ChunkSimpleBannerViewHolder(View itemView) {
		super(itemView);
	}

	@Override
	public void initItemView(View itemView) {
        mBannerIcon = (AlphaAnimImageView) itemView.findViewById(R.id.banner_icon);
	}

	@Override
	public void setItemView(ChunkSimpleBannerData data) {
		ImageLoader.INSTANCE.displayImage(data.bannerImg, mBannerIcon, R.drawable.icon_samll_round_bg);
	}

}
