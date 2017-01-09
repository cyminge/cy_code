package com.cy.uiframetest.receclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.test.R;
import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;

public class TestRecycleViewAdapter extends AbstractRececlerAdapter<BaseBean> {
	
	public static final int LIST_TYPE_FIRST = 0x1;
    public static final int LIST_TYPE_SECOND = 0x2;
    public static final int BRICK_LIST_TYPE_COUNT = LIST_TYPE_SECOND;
    
	public TestRecycleViewAdapter(Context context) {
		super(context);
	}

	@Override
	public int getAdvanceViewType(int position) {
		BaseBean bean = getItemData(position);
		if(null == bean) { //?
			return 1;
		}
		
		return bean.mItemType;
	}

	@Override
	public AbstractRecyclerViewHolder<BaseBean> createHeaderHolder(ViewGroup parent, int viewType) {
		return new Holder(getHeaderView(viewType));
	}
	
	@Override
	public AbstractRecyclerViewHolder<BaseBean> createFootHolder(ViewGroup parent, int viewType) {
		Log.e("cyTest", "createFootHolder");
		return null;
	}

	@Override
	public AbstractRecyclerViewHolder<BaseBean> createDefaultViewHolder(ViewGroup parent, int viewType) {
		return new Holder(LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), null, false));
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
	
	class Holder extends AbstractRecyclerViewHolder<BaseBean> {
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