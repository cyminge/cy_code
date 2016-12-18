package com.cy.uiframe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可以添加多个透视图、尾视图的适配器 只需重写简单方法即可 需要 DmlViewHolder 配合 Created by WangGang on
 * 2015/6/27.
 */
public abstract class AbstractAdapter<T> extends RecyclerView.Adapter<AbstractViewHolder<T>> {

	protected Context mContext;
	protected List<T> mDatas;

	private ArrayList<View> mHeaderViews = new ArrayList<>();
	private ArrayList<View> mFooterViews = new ArrayList<>();

	private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
	private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();
	
	public AbstractAdapter(Context context, List<T> datas) {
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
			mHeaderViewTypes.add(position * 1000);
			return position * 1000;
		}

		if (getFooterViewsCount() > 0 && position >= getItemCount() - getFooterViewsCount()) {
			mFooterViewTypes.add(position * 1000);
			return position * 1000;
		}

		if (getHeaderViewsCount() > 0) {
			return getAdvanceViewType(position - mHeaderViews.size());
		}

		return getAdvanceViewType(position);
	}

	/**
	 * @param position
	 * @return need not be zero
	 */
	public abstract int getAdvanceViewType(int position);
	

	@Override
	public AbstractViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
		if (mHeaderViewTypes.contains(viewType)) {
			return createHeaderHolder(viewType);
		}

		if (mFooterViewTypes.contains(viewType)) {
			return createFootHolder(viewType);
		}

		return createDefaultViewHolder(viewType);
	}

	public abstract AbstractViewHolder<T> createHeaderHolder(int viewType);

	public abstract AbstractViewHolder<T> createFootHolder(int viewType);

	public abstract AbstractViewHolder<T> createDefaultViewHolder(int viewType);

	@Override
	public void onBindViewHolder(AbstractViewHolder<T> viewholder, int position) {
		if (getFooterViewsCount() > 0 && (position >= getItemCount() - getFooterViewsCount())) {
			return;
		}

		if (getHeaderViewsCount() > 0) {
			if (position < getHeaderViewsCount()) {
				return;
			}
			viewholder.setItemView(getItemData(position - getHeaderViewsCount()));
			return;
		}

		viewholder.setItemView(getItemData(position));
	}

	public T getItemData(int position) {
		if (position < getHeaderViewsCount() || position >= getItemCount()-getFooterViewsCount()) {
			return null;
		}
		
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
	 * 返回第一个FoView
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
		return getHeaderViewsCount() > 0 ? mHeaderViews.get(viewType/1000) : null;
	}
	
	public View getFooterView(int viewType) {
		return getFooterViewsCount() > 0 ? mFooterViews.get(viewType/1000) : null;
	}
}
