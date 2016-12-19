package com.cy.uiframe.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

	public AbstractViewHolder(View itemView) {
		super(itemView);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
		initItemView(itemView);
	}
	
	public abstract void initItemView(View itemView);
	
	public abstract void setItemView(T t);

}
