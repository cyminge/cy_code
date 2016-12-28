package com.cy.uiframe.recyclerview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可以添加多个头视图、尾视图的适配器
 * @author zf
 * @param <T>
 */
public abstract class AbstractRececlerAdapter<T> extends RecyclerView.Adapter<AbstractRecyclerViewHolder<T>> {

	private static final int HEAD_OR_FOOT_POSITION_RIDE = 1000;
	
	protected Context mContext;
	protected List<T> mDatas;

	private ArrayList<View> mHeaderViews = new ArrayList<>();
	private ArrayList<View> mFooterViews = new ArrayList<>();

	private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
	private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();
	
	public AbstractRececlerAdapter(Context context, List<T> datas) {
		mContext = context;
		mDatas = datas;
	}
	
	@Override
	public int getItemCount() {
		return getHeaderViewsCount() + getFooterViewsCount() + mDatas.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position < getHeaderViewsCount()) {
			mHeaderViewTypes.add(position * HEAD_OR_FOOT_POSITION_RIDE);
			return position * HEAD_OR_FOOT_POSITION_RIDE;
		}

		if (getFooterViewsCount() > 0 && position >= getItemCount() - getFooterViewsCount()) {
			mFooterViewTypes.add(position * HEAD_OR_FOOT_POSITION_RIDE);
			return position * HEAD_OR_FOOT_POSITION_RIDE;
		}

		if (getHeaderViewsCount() > 0) {
			return getAdvanceViewType(position - getHeaderViewsCount());
		}

		return getAdvanceViewType(position);
	}

	/**
	 * @param position
	 * @return need not be zero
	 */
	public abstract int getAdvanceViewType(int position);
	

	@Override
	public AbstractRecyclerViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
		if (mHeaderViewTypes.contains(viewType)) {
			return createHeaderHolder(parent, viewType);
		}

		if (mFooterViewTypes.contains(viewType)) {
			return createFootHolder(parent, viewType);
		}

		return createDefaultViewHolder(parent, viewType);
	}

	public abstract AbstractRecyclerViewHolder<T> createHeaderHolder(ViewGroup parent, int viewType);

	public abstract AbstractRecyclerViewHolder<T> createFootHolder(ViewGroup parent, int viewType);

	public abstract AbstractRecyclerViewHolder<T> createDefaultViewHolder(ViewGroup parent, int viewType);

	@Override
	public void onBindViewHolder(AbstractRecyclerViewHolder<T> viewholder, int position) {
		if (getFooterViewsCount() > 0 && (position >= getItemCount() - getFooterViewsCount())) {
			return;
		}

		if (getHeaderViewsCount() > 0) {
			if (position < getHeaderViewsCount()) {
				return;
			}
			Log.e("zz", "position:"+position+", getHeaderViewsCount():"+getHeaderViewsCount()+", data:"+getItemData(position - getHeaderViewsCount()));
			viewholder.setItemView(getItemData(position - getHeaderViewsCount()));
			return;
		}

		viewholder.setItemView(getItemData(position));
	}

	/**
	 * 去掉头尾视图数据
	 * @param position
	 * @return
	 */
	public T getItemData(int position) {
//		if (position < getHeaderViewsCount() || position >= getItemCount()-getFooterViewsCount()) {
//			return null;
//		}
		
		synchronized (mDatas) {
			return mDatas.get(position);
		}
	}

	public void addHeaderView(View header) {
		if (header == null) {
			throw new RuntimeException("header is null");
		}
		mHeaderViews.add(header);
		this.notifyDataSetChanged();
	}

	public void addFooterView(View footer) {
		if (footer == null) {
			throw new RuntimeException("footer is null");
		}
		mFooterViews.add(footer);
		this.notifyDataSetChanged();
	}

	public int getHeaderViewsCount() {
		return mHeaderViews.size();
	}

	public int getFooterViewsCount() {
		return mFooterViews.size();
	}

	public boolean isHeader(int position) {
		return getHeaderViewsCount() > 0 && position == 0;
	}

	public boolean isFooter(int position) {
		int lastPosition = getItemCount() - 1;
		return getFooterViewsCount() > 0 && position == lastPosition;
	}

	/**
	 * 返回第一个FooterView
	 * 
	 * @return
	 */
	public View getFooterView() {
		return getFooterViewsCount() > 0 ? mFooterViews.get(0) : null;
	}

	/**
	 * 返回第一个HeaderView
	 * 
	 * @return
	 */
	public View getHeaderView() {
		return getHeaderViewsCount() > 0 ? mHeaderViews.get(0) : null;
	}

	public void removeHeaderView(View view) {
		mHeaderViews.remove(view);
		this.notifyDataSetChanged();
	}

	public void removeFooterView(View view) {
		mFooterViews.remove(view);
		this.notifyDataSetChanged();
	}
	
	public View getHeaderView(int viewType) {
		View view = getHeaderViewsCount() > 0 ? mHeaderViews.get(viewType/HEAD_OR_FOOT_POSITION_RIDE) : null;
		return view;
	}
	
	public View getFooterView(int viewType) {
		return getFooterViewsCount() > 0 ? mFooterViews.get(viewType/HEAD_OR_FOOT_POSITION_RIDE) : null;
	}
}
