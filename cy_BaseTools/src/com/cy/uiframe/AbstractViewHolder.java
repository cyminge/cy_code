package com.cy.uiframe;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

	public AbstractViewHolder(View itemView) {
		super(itemView);
		
		initItemView(itemView);
	}
	
	public abstract void initItemView(View itemView);
	
	public abstract void setItemView(T t);

}
