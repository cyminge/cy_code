package com.cy.uiframetest.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.cy.test.R;
import com.cy.uiframe.main.parse.Parser;
import com.cy.uiframetest.bean.ChunkDissertationData;
import com.cy.uiframetest.bean.ChunkListData;
import com.cy.uiframetest.bean.ChunkSimpleBanner;
import com.cy.uiframetest.receclerview.ChunkBannerViewHolder;
import com.cy.uiframetest.receclerview.ChunkDissertationViewHolder;
import com.google.gson.Gson;

public class UIFrameParser extends Parser<ChunkListData> {
	
	public static final String ITEM_TYPE_BANNER = "SimpleBanner";
	public static final String ITEM_TYPE_HOME_DISSERTATION_GAMES = "DissertationGames";
			
	private static final SparseArray<Class<?>> mChunkDataArray;
	private static final HashMap<String, Integer> mChunkListItemTypeArray;
	private static final SparseIntArray mChunkItemLayoutArray;
	private static final SparseArray<Class<?>> mChunkViewHolderArray;
	
	public enum CHUNK_LIST_TYPE {
		SimpleBanner(1),
		Dissertation(2);
		
		private int mListType;
		
		private CHUNK_LIST_TYPE(int type) {
			mListType = type;
		}
		
		public int getListType() {
			return mListType;
	    }
	}
	
	static {
		mChunkDataArray = new SparseArray<Class<?>>();
		mChunkDataArray.put(CHUNK_LIST_TYPE.SimpleBanner.getListType(), ChunkSimpleBanner.class);
		mChunkDataArray.put(CHUNK_LIST_TYPE.Dissertation.getListType(), ChunkDissertationData.class);
		
		mChunkListItemTypeArray = new HashMap<String, Integer>();
		mChunkListItemTypeArray.put(ITEM_TYPE_BANNER, CHUNK_LIST_TYPE.SimpleBanner.getListType());
		mChunkListItemTypeArray.put(ITEM_TYPE_HOME_DISSERTATION_GAMES, CHUNK_LIST_TYPE.Dissertation.getListType());
		
		mChunkItemLayoutArray = new SparseIntArray();
		mChunkItemLayoutArray.put(CHUNK_LIST_TYPE.SimpleBanner.getListType(), R.layout.uiframe_chunk_banner_layout);
		mChunkItemLayoutArray.put(CHUNK_LIST_TYPE.Dissertation.getListType(), R.layout.uiframe_chunk_dissertation_layout);
		
		mChunkViewHolderArray = new SparseArray<Class<?>>();
		mChunkViewHolderArray.put(CHUNK_LIST_TYPE.SimpleBanner.getListType(), ChunkBannerViewHolder.class);
		mChunkViewHolderArray.put(CHUNK_LIST_TYPE.Dissertation.getListType(), ChunkDissertationViewHolder.class);
	}
	
	public static int getChunkItemLayoutId(int listType) {
		return mChunkItemLayoutArray.get(listType);
	}
	
	public static Class<?> getChunkViewHolder(int listType) {
		return mChunkViewHolderArray.get(listType);
	}
	
	public UIFrameParser(ParserCallBack<ChunkListData> callback) {
		super(callback);
	}

	@Override
	protected ArrayList<ChunkListData> createDataList(JSONArray list) throws JSONException {
		ArrayList<ChunkListData> dataList = new ArrayList<ChunkListData>();
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String itemType = item.optString("listItemType");
                addListItem(dataList, item, itemType);
        }
        return dataList;
	}
	
	private void addListItem(ArrayList<ChunkListData> dataList, JSONObject item, String itemType) {
		Integer listType = mChunkListItemTypeArray.get(itemType);
		if(null == listType) {
			return;
		}
		
		ChunkListData data = createItemData(listType, item);
		if(null == data) {
			return;
		}
		data.mListType = listType;
		dataList.add(data);
	}

	public ChunkListData createItemData(int listType, JSONObject item) {
		try {
			Gson gson = new Gson();
			return (ChunkListData) gson.fromJson(item.toString(), mChunkDataArray.get(listType));
		} catch (Exception e) {
			return null;
		}
	}
	
}
