package com.cy.uiframetest.receclerview;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cy.uiframe.recyclerview.AbstractRececlerAdapter;
import com.cy.uiframe.recyclerview.AbstractRecyclerViewHolder;
import com.cy.uiframetest.bean.ChunkData;
import com.cy.uiframetest.main.UIFrameParser;

public class TestRecycleViewAdapter extends AbstractRececlerAdapter<ChunkData> {
	
	public TestRecycleViewAdapter(Context context) {
		super(context);
	}

	@Override
	public int getAdvanceViewType(int position) {
		ChunkData listData = getItemData(position);
		if(null == listData) { //?
			return 1;
		}
		
		return listData.mListType;
	}
	
//	@Override
//	public ChunkListData getItemData(int position) {
//		ChunkListData listData = getItemData(position);
//		if(null == listData) {
//			return null;
//		}
//		
//		
//	}

	@Override
	public AbstractRecyclerViewHolder<ChunkData> createHeaderHolder(ViewGroup parent, int viewType) {
		return new DefaultHolder(getHeaderView(viewType));
	}
	
	@Override
	public AbstractRecyclerViewHolder<ChunkData> createFootHolder(ViewGroup parent, int viewType) {
		Log.e("cyTest", "createFootHolder");
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractRecyclerViewHolder<ChunkData> createDefaultViewHolder(ViewGroup parent, int viewType) {
		Class<?> c = (Class<AbstractRecyclerViewHolder<ChunkData>>) UIFrameParser.getChunkViewHolder(viewType);
		Class<?>[] parameterTypes={View.class};   
		Constructor<?> constructor = null;
		try {
			constructor = c.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Log.e("cyTest", "獲取ViewHolder構造函數參數失敗!!");
		}  
		View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), null, false);
		try {
			return (AbstractRecyclerViewHolder<ChunkData>) constructor.newInstance(view);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}  
		Log.e("cyTest", "初始化ViewHolder構造函數失敗!!");
		return null;
//		return new Holder(LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), null, false));
	}
	
	protected int getItemLayoutId(int viewType) {
		return UIFrameParser.getChunkItemLayoutId(viewType);
	}
	
	class DefaultHolder extends AbstractRecyclerViewHolder<ChunkData> {

		public DefaultHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void initItemView(View itemView) {
		}

		@Override
		public void setItemView(ChunkData t) {
		}
	}
}
