package com.cy.uiframetest.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

import com.cy.uiframe.main.parse.Parser;
import com.cy.uiframetest.bean.ChunkDissertationData;
import com.cy.uiframetest.bean.ChunkListData;
import com.cy.uiframetest.bean.ChunkSimpleBanner;
import com.google.gson.Gson;

public class UIFrameParser extends Parser<ChunkListData> {
	
	public enum LIST_TYPE {
		simpleBanner,
		Dissertation;
		
		public int toInt() {
			return ordinal();
	    }
	}
	
	public static final String ITEM_TYPE_BANNER = "SimpleBanner";
	public static final String ITEM_TYPE_HOME_DISSERTATION_GAMES = "DissertationGames";
			
	private static final SparseArray<Class<?>> mBrickArray;
	
	private static final HashMap<String, Integer> mListItemTypeArray;
	
	static {
		mBrickArray = new SparseArray<Class<?>>();
		mBrickArray.put(LIST_TYPE.simpleBanner.toInt(), ChunkSimpleBanner.class);
		mBrickArray.put(LIST_TYPE.Dissertation.toInt(), ChunkDissertationData.class);
		
		mListItemTypeArray = new HashMap<String, Integer>();
		mListItemTypeArray.put(ITEM_TYPE_BANNER, LIST_TYPE.simpleBanner.toInt());
		mListItemTypeArray.put(ITEM_TYPE_HOME_DISSERTATION_GAMES, LIST_TYPE.Dissertation.toInt());
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
		Integer listType = mListItemTypeArray.get(itemType);
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
			return (ChunkListData) gson.fromJson(item.toString(), mBrickArray.get(listType));
		} catch (Exception e) {
			return null;
		}
	}
	
}
