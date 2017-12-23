package com.cy.uiframe.refactor.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class GioneeRecyclerViewHolder extends RecyclerView.ViewHolder {
	
	public GioneeRecyclerViewHolder(View itemView) {
		super(itemView);
	}
	
	public abstract void initItemView(View itemView);
	
	public abstract <T> void setItemView(T data);
	
	protected OnClickListener mHolderClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            onViewHolderClick(view, id);
        }
    };
    
    protected void onViewHolderClick(View view, int id) {
    }
}
