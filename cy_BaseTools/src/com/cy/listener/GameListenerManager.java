package com.cy.listener;

import android.util.SparseArray;

import java.util.ArrayList;

public class GameListenerManager {

    public static final int DOWNLOADARGS_DONE = 0x1;
    public static final int COMMENT_SUCCESS = DOWNLOADARGS_DONE + 1;
    public static final int UPGRADE_CHECK_START = COMMENT_SUCCESS + 1;
    public static final int UPGRADE_CHECK_FINISH = UPGRADE_CHECK_START + 1;
    public static final int UPGRADE_TYPE_CHANGE = UPGRADE_CHECK_FINISH + 1;
    public static final int GRAB_GIFT_END = UPGRADE_TYPE_CHANGE + 1;
    public static final int CALL_DETAIL_TABVIEW = GRAB_GIFT_END + 1;
    public static final int GAME_OFFLINE = CALL_DETAIL_TABVIEW + 1;
    public static final int MONEY_INFO_RECEIVED = GAME_OFFLINE + 1;
    public static final int LOGIN_SUCCESS = MONEY_INFO_RECEIVED + 1;
    public static final int ACCOUNT_OFFLINE = LOGIN_SUCCESS + 1;
    public static final int USER_INFO_EDIT = ACCOUNT_OFFLINE + 1;
    public static final int UNREAD_MSG_CHANGE = USER_INFO_EDIT + 1;
    public static final int WELFARE_RECEIVED = UNREAD_MSG_CHANGE + 1;
    public static final int WELFARE_CHANGE = WELFARE_RECEIVED + 1;
    public static final int ACCOUNT_ICON_DOWNLOADED = WELFARE_CHANGE + 1;
    public static final int GAMES_UPDATE_CHANGE = ACCOUNT_ICON_DOWNLOADED + 1;
    public static final int SELF_UPDATE_CHANGE = GAMES_UPDATE_CHANGE + 1;
    public static final int FEEDBACK_CHANGED = SELF_UPDATE_CHANGE + 1;
    public static final int MENU_BG_CHANGE = FEEDBACK_CHANGED + 1;
    public static final int FAVOR_GAME_CHANGED = MENU_BG_CHANGE + 1;
    public static final int DAILY_TASK_CHANGE = FAVOR_GAME_CHANGED + 1;
    public static final int POINT_INFO_CHANGE = DAILY_TASK_CHANGE + 1;
    public static final int SUBMIT_RECEIVE_INFO = POINT_INFO_CHANGE + 1;
    public static final int PACKAGE_CHANGE = SUBMIT_RECEIVE_INFO + 1;
    public static final int DOWNLOAD_COUNT_CHANGE = PACKAGE_CHANGE + 1;
    public static final int IMAGE_PICKER_BY_GALLERY = DOWNLOAD_COUNT_CHANGE + 1;
    public static final int SEARCH_KEY_WORD_FETCHED = IMAGE_PICKER_BY_GALLERY + 1;
    public static final int SEARCH_HOT_WORD_FETCHED = SEARCH_KEY_WORD_FETCHED + 1;
    public static final int GAME_LOCAL_GIFT_TIME_CHANGED = SEARCH_HOT_WORD_FETCHED + 1;
    public static final int ACTIVITY_GET_FOCUS = GAME_LOCAL_GIFT_TIME_CHANGED + 1;
    public static final int GAME_DETAIL_DOWNLOAD_SHOWED = ACTIVITY_GET_FOCUS + 1;
    public static final int USER_FAVOR_CHANGE = GAME_DETAIL_DOWNLOAD_SHOWED + 1;
    public static final int GET_DOWNLOAD_ARGS_FOR_GIFT = USER_FAVOR_CHANGE + 1;
    public static final int MONEY_INFO_EXPIRED = GET_DOWNLOAD_ARGS_FOR_GIFT + 1;
    public static final int ON_PARSE_GAME_INTRO_DATA = MONEY_INFO_EXPIRED + 1;
    public static final int ON_CHECK_GAME_DATA = ON_PARSE_GAME_INTRO_DATA + 1;
    public static final int REFRESH_LIST = ON_CHECK_GAME_DATA + 1;
    
    private static SparseArray<ArrayList<GameListener>> sListeners = new SparseArray<ArrayList<GameListener>>();
    private static SparseArray<ArrayList<GameListener>> sStaticListeners = new SparseArray<ArrayList<GameListener>>();

    public static synchronized void addListener(GameListener listener, int... keys) {
        for (int key : keys) {
            addListener(listener, key);
        }
    }

    public static synchronized void addListener(GameListener listener, int key) {
        add(listener, key, sListeners);
    }

    public static synchronized void addStaticListener(GameListener listener, int... keys) {
        for (int key : keys) {
            addStaticListener(listener, key);
        }
    }

    public static synchronized void addStaticListener(GameListener listener, int key) {
        add(listener, key, sStaticListeners);
    }

    public static synchronized void removeListener(GameListener listener) {
        remove(listener, sListeners);
        remove(listener, sStaticListeners);
    }

    private static void add(GameListener listener, int key, SparseArray<ArrayList<GameListener>> listeners) {
        if (listener == null) { // notify
            return;
        }
        ArrayList<GameListener> list = listeners.get(key);
        if (list != null) {
            if (!list.contains(listener)) {
                list.add(listener);
            }
        } else {
            list = new ArrayList<GameListener>();
            list.add(listener);
            listeners.put(key, list);
        }
    }

    private static void remove(GameListener listener, SparseArray<ArrayList<GameListener>> listeners) {
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            ArrayList<GameListener> list = listeners.valueAt(i);
            list.remove(listener);
        }
    }

    public static synchronized void onEvent(int key) {
        onEvent(key, new Object());
    }

    public static synchronized void onEvent(int key, Object... params) {
        onEvent(key, sListeners.get(key), params);
        onEvent(key, sStaticListeners.get(key), params);
    }

    private static void onEvent(int key, ArrayList<GameListener> target, Object... params) {

        if (target == null) {
            return;
        }
        for (GameListener listener : target) {
            listener.onEvent(key, params);
        }
    }

    public static synchronized void exit() {
        recycle(sListeners);
    }

    private static void recycle(SparseArray<ArrayList<GameListener>> listeners) {
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            ArrayList<GameListener> list = listeners.valueAt(i);
            list.clear();
        }
        listeners.clear();
    }
}
