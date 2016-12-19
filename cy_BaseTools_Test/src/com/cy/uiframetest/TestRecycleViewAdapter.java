package com.cy.uiframetest;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.test.R;
import com.cy.uiframe.AbstractAdapter;
import com.cy.uiframe.AbstractViewHolder;

public class TestRecycleViewAdapter extends AbstractAdapter<BaseBean> {
	
	public static final int LIST_TYPE_FIRST = 0x1;
    public static final int LIST_TYPE_SECOND = 0x2;
    public static final int BRICK_LIST_TYPE_COUNT = LIST_TYPE_SECOND;
    
	public TestRecycleViewAdapter(Context context, List<BaseBean> datas) {
		super(context, datas);
	}

	@Override
	public int getAdvanceViewType(int position) {
		BaseBean bean = getItemData(position);
		if(null == bean && getHeaderViewsCount() > 0) {
			return 1;
		}
		Log.e("cyTest", "bean.mItemType:"+bean.mItemType);
		return bean.mItemType;
	}

	@Override
	public AbstractViewHolder<BaseBean> createHeaderHolder(ViewGroup parent, int viewType) {
		Log.e("cyTest", "createHeaderHolder");
		return new Holder(getHeaderView(viewType));
	}
	
	@Override
	public AbstractViewHolder<BaseBean> createFootHolder(ViewGroup parent, int viewType) {
		Log.e("cyTest", "createFootHolder");
		return null;
	}

	@Override
	public AbstractViewHolder<BaseBean> createDefaultViewHolder(ViewGroup parent, int viewType) {
		Log.e("cyTest", "createDefaultViewHolder");
		return new Holder(LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), null, false));
//		return new Holder(getHeaderView());
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

		TextView name;
		TextView age;
		
		
		public Holder(View itemView) {
			super(itemView);
		}

		@Override
		public void initItemView(View itemView) {
			name = (TextView) itemView.findViewById(R.id.name);
			age = (TextView) itemView.findViewById(R.id.age);
		}

		@Override
		public void setItemView(BaseBean t) {
			name.setText(t.mName);
			age.setText(t.mAge);
			Log.e("cyTest", "name width:"+name.getWidth());
		}
	}
}
