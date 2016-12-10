package com.cy.uiframe;

import android.view.View;

public abstract class AbstractViewHolder {

	public abstract void initItemView(View convertView, View.OnClickListener onClickListener);

	public abstract void setItemView(int position, Object data);

	public int getHolderPosition() {
		return 0;
	}

	public void setButtonState(Object listData) {

	}

}
