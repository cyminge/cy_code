package com.cy.uiframe.refactor.recyclerview;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cy.uiframe.refactor.multitype.BinderNotFoundException;
import com.cy.uiframe.refactor.multitype.ItemViewBinder;
import com.cy.uiframe.refactor.multitype.MultiTypePool;
import com.cy.uiframe.refactor.multitype.TypePool;

/**
 * 可以添加多个头视图、尾视图的适配器
 * @author zf
 */
public class GioneeRecyclerAdapter extends RecyclerView.Adapter<GioneeRecyclerViewHolder> {

	private static final String TAG = GioneeRecyclerAdapter.class.getSimpleName();

	private static final int HEAD_OR_FOOT_POSITION_RIDE = 1000;
	private @NonNull ArrayList mDatas;
	private @NonNull TypePool typePool;

	private ArrayList<View> mHeaderViews = new ArrayList<>();
	private ArrayList<View> mFooterViews = new ArrayList<>();

	private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
	private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();
	
	private LayoutInflater mInflater;
	
	public GioneeRecyclerAdapter() {
		this(new ArrayList<>());
	}

	public GioneeRecyclerAdapter(@NonNull ArrayList<?> items) {
		this(items, new MultiTypePool());
	}

	public GioneeRecyclerAdapter(@NonNull ArrayList<?> items, int initialCapacity) {
		this(items, new MultiTypePool(initialCapacity));
	}

	public GioneeRecyclerAdapter(@NonNull ArrayList<?> items, @NonNull TypePool pool) {
		this.mDatas = items;
		this.typePool = pool;
	}
	
	@Override
	public int getItemCount() {
		return getHeaderViewsCount() + getFooterViewsCount() + mDatas.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public final long getItemId(int position) {
		Object item = mDatas.get(position);
		int itemViewType = getItemViewType(position);
		ItemViewBinder binder = typePool.getItemViewBinder(itemViewType);
		return binder.getItemId(item);
	}

	private @NonNull ItemViewBinder getRawBinderByViewHolder(@NonNull RecyclerView.ViewHolder holder) {
		return typePool.getItemViewBinder(holder.getItemViewType());
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void onViewRecycled(@NonNull GioneeRecyclerViewHolder holder) {
		getRawBinderByViewHolder(holder).onViewRecycled(holder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final boolean onFailedToRecycleView(@NonNull GioneeRecyclerViewHolder holder) {
		return getRawBinderByViewHolder(holder).onFailedToRecycleView(holder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void onViewAttachedToWindow(@NonNull GioneeRecyclerViewHolder holder) {
		getRawBinderByViewHolder(holder).onViewAttachedToWindow(holder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void onViewDetachedFromWindow(@NonNull GioneeRecyclerViewHolder holder) {
		getRawBinderByViewHolder(holder).onViewDetachedFromWindow(holder);
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
	 * @return need not be zero ??
	 */
	private int getAdvanceViewType(int position) {
		Object item = mDatas.get(position);
		return indexInTypesOf(position, item);
	}

	public int indexInTypesOf(int position, @NonNull Object item) throws BinderNotFoundException {
		int index = typePool.firstIndexOf(item.getClass());
		if (index != -1) {
			return index;
		}
		throw new BinderNotFoundException(item.getClass());
	}

	public void setTypePool(@NonNull TypePool typePool) {
		this.typePool = typePool;
	}

	public @NonNull TypePool getTypePool() {
		return typePool;
	}
	

	@Override
	public GioneeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (mHeaderViewTypes.contains(viewType)) {
			return createHeaderHolder(parent, viewType);
		}

		if (mFooterViewTypes.contains(viewType)) {
			return createFootHolder(parent, viewType);
		}

		return createDefaultViewHolder(parent, viewType);
	}

	public GioneeRecyclerViewHolder createHeaderHolder(ViewGroup parent, int viewType){
		return null;
	}

	public GioneeRecyclerViewHolder createFootHolder(ViewGroup parent, int viewType){
		return null;
	}

	public GioneeRecyclerViewHolder createDefaultViewHolder(ViewGroup parent, int indexViewType){
	    if(null == mInflater) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(typePool.getLayoutId(indexViewType), parent, false);
//      View root = inflater.inflate(R.layout.item_rich, parent, false);
        ItemViewBinder<?, ?> binder = typePool.getItemViewBinder(indexViewType);
        return binder.onCreateViewHolder(view);
	}

	@Override
	public void onBindViewHolder(GioneeRecyclerViewHolder viewHolder, int position) {
		if (getFooterViewsCount() > 0 && (position >= getItemCount() - getFooterViewsCount())) {
			return;
		}

		if (getHeaderViewsCount() > 0) {
			if (position < getHeaderViewsCount()) {
				return;
			}
			Log.e("zz", "position:"+position+", getHeaderViewsCount():"+getHeaderViewsCount()+", data:"+getItemData(position - getHeaderViewsCount()));
			Object item = getItemData(position - getHeaderViewsCount());
			ItemViewBinder binder = typePool.getItemViewBinder(viewHolder.getItemViewType());
			binder.onBindViewHolder(viewHolder, item);
			return;
		}
		Object item = getItemData(position);
		ItemViewBinder binder = typePool.getItemViewBinder(viewHolder.getItemViewType());
		binder.onBindViewHolder(viewHolder, item);
	}

	/**
	 * 去掉头尾视图数据
	 * @param position
	 * @return
	 */
	public Object getItemData(int position) {
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
	
	public void clearData() {
    	mDatas.clear();
    }

	/**
	 * best to call on main thread
	 * @param data
	 */
    public void updateData(ArrayList<?> data) {
    	mDatas.clear();
    	addData(data);
    }

    /**
	 * best to call on main thread
	 * @param data
	 */
    public void addData(ArrayList<?> data) {
    	mDatas.addAll(data);
    }

	public @NonNull List<?> getDatas() {
		return mDatas;
	}

	public <T> void register(Class<? extends T> clazz, ItemViewBinder<T, ?> binder, int layoutId) {
		checkAndRemoveAllTypesIfNeeded(clazz);
		typePool.register(clazz, binder, layoutId);
		binder.adapter = this;
	}

	private void checkAndRemoveAllTypesIfNeeded(@NonNull Class<?> clazz) {
		if (typePool.unregister(clazz)) {
			Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
					"It will override the original binder(s).");
		}
	}

	public void registerAll(@NonNull final TypePool pool) {
		final int size = pool.size();
		for (int i = 0; i < size; i++) {
			registerWithoutChecking(
					pool.getClass(i),
					pool.getItemViewBinder(i),
					pool.getLayoutId(i)
			);
		}
	}

	@SuppressWarnings("unchecked")
	private void registerWithoutChecking(@NonNull Class clazz, @NonNull ItemViewBinder binder, @NonNull int layoutId) {
		checkAndRemoveAllTypesIfNeeded(clazz);
		register(clazz, binder, layoutId);
	}
}
