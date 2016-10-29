package com.cy.slide;

public class AdItem {

	public String mId = "";
	public String mTitle = "";
	public String mImageUrl = "";
	public String mViewType = "";
	public String mParam = "";

	@Override
	public boolean equals(Object obj) {
		if (null != obj && obj instanceof AdItem) {
			AdItem adItem = (AdItem) obj;
			return mId.equals(adItem.mId) && mTitle.equals(adItem.mTitle)
					&& mImageUrl.equals(adItem.mImageUrl);
		}
		return false;
	}

}
