package com.cy.uiframetest.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.SparseArray;

import com.cy.uiframe.main.parse.Parser;
import com.cy.uiframetest.bean.BrickDissertationData;
import com.cy.uiframetest.bean.BrickListData;
import com.cy.uiframetest.bean.BrickSimpleBanner;
import com.google.gson.Gson;

public class UIFrameParser extends Parser<BrickListData> {
	
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
		mBrickArray.put(LIST_TYPE.simpleBanner.toInt(), BrickSimpleBanner.class);
		mBrickArray.put(LIST_TYPE.Dissertation.toInt(), BrickDissertationData.class);
		
		mListItemTypeArray = new HashMap<String, Integer>();
		mListItemTypeArray.put(ITEM_TYPE_BANNER, LIST_TYPE.simpleBanner.toInt());
		mListItemTypeArray.put(ITEM_TYPE_HOME_DISSERTATION_GAMES, LIST_TYPE.Dissertation.toInt());
	}

	public UIFrameParser(ParserCallBack<BrickListData> callback) {
		super(callback);
	}

	@Override
	protected ArrayList<BrickListData> createDataList(JSONArray list) throws JSONException {
		ArrayList<BrickListData> dataList = new ArrayList<BrickListData>();
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String itemType = item.optString("listItemType");
                addListItem(dataList, item, itemType);
        }
        return dataList;
	}
	
	private void addListItem(ArrayList<BrickListData> dataList, JSONObject item, String itemType) {
		BrickListData data = createItemData(mListItemTypeArray.get(itemType), item);
		if(null == data) {
			return;
		}
		data.mListType = mListItemTypeArray.get(itemType);
		dataList.add(data);
	}

	public BrickListData createItemData(int listType, JSONObject item) {
		try {
			Gson gson = new Gson();
			return (BrickListData) gson.fromJson(item.toString(), mBrickArray.get(listType));
		} catch (Exception e) {
			return null;
		}
	}
	
}
