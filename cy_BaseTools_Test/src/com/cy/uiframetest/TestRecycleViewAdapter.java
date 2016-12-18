package com.cy.uiframetest;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cy.test.R;
import com.cy.uiframe.AbstractAdapter;
import com.cy.uiframe.AbstractViewHolder;

public class TestRecycleViewAdapter extends AbstractAdapter<BaseBean> {
	
	public static final int LIST_TYPE_FIRST = 0x1;
    public static final int LIST_TYPE_SECOND = 0x2;
    public static final int BRICK_LIST_TYPE_COUNT = LIST_TYPE_SECOND;
    
	public TestRecycleViewAdapter(Context context, List<BaseBean> datas) {
		super(context, datas);
//		SlideView slideView = new SlideView(context);
//		addHeaderView(slideView);
	}

	@Override
	public int getAdvanceViewType(int position) {
		BaseBean bean = getItemData(position);
		if(null == bean && getHeaderViewsCount() > 0) {
			return 1;
		}
		
		return bean.mItemType;
	}

	@Override
	public AbstractViewHolder<BaseBean> createHeaderHolder(int viewType) {
		return new Holder(getHeaderView(viewType));
	}
	
	@Override
	public AbstractViewHolder<BaseBean> createFootHolder(int viewType) {
		return null;
	}

	@Override
	public AbstractViewHolder<BaseBean> createDefaultViewHolder(int viewType) {
		return new Holder(LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), null));
	}
	
	protected int getItemLayoutId(int viewType) {
		switch(viewType) {
		case LIST_TYPE_FIRST :
			return R.layout.adapter_first_item_type;
		case LIST_TYPE_SECOND :
			return R.layout.adapter_second_item_type;
		default :
			return 0;
		}
	}
	
	class Holder extends AbstractViewHolder<BaseBean> {

		public Holder(View itemView) {
			super(itemView);
		}

		@Override
		public void initItemView(View itemView) {
			
		}

		@Override
		public void setItemView(BaseBean t) {
			
		}
	}
}
