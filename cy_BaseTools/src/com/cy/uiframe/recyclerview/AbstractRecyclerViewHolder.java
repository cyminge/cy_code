package com.cy.uiframe.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class AbstractRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

	public AbstractRecyclerViewHolder(View itemView) {
		super(itemView);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
		initItemView(itemView);
	}
	
	public abstract void initItemView(View itemView);
	
	public abstract void setItemView(T t);
	
	protected View.OnClickListener mHolderClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            onViewHolderClick(view, id);
        }
    };
    
    protected void onViewHolderClick(View view, int id) {

    }

}
