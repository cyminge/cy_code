package com.cy.downloadtest;

/**
 * 封装了一个ListData
 * @author JLB6088
 *
 */
public class GameListDataCategory {

    private int mListType; // 这是个什么东东
    private Object mData;

    public GameListDataCategory(int listType, Object data) {
        this.mListType = listType;
        this.mData = data;
    }

    public int getListType() {
        return mListType;
    }

    public Object getData() {
        return mData;
    }
}
