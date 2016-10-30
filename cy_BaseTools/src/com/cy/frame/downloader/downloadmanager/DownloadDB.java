package com.cy.frame.downloader.downloadmanager;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.cy.constant.Constant;
import com.cy.frame.downloader.core.DownloadStatusMgr;
import com.cy.frame.downloader.download.entity.DownloadInfo;
import com.cy.global.BaseApplication;
import com.cy.threadpool.NomalThreadPool;
import com.cy.utils.Utils;

/**
 * 下载文件数据库管理
 * @author JLB6088
 *
 */
public class DownloadDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "download_mgr.db";
    private static final int DATABASE_VER = 4;
    private static final int VER_ADD_START_TIME_FREE_TAG = 2;
    private static final int VER_ADD_SILENT_DOWNLOAD = 3;
    private static final int VER_ADD_INIT_TIME = 4;
    private static final String TABLE_NAME = "download_info";
    public static final int NO_DOWN_ID = -1;

    // column name
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FILE_PATH = "file_path";
    private static final String COLUMN_GAME_SIZE = "game_size";
    private static final String COLUMN_DOWNLOAD_URL = "download_url";
    private static final String COLUMN_GAME_ID = "game_id";
    private static final String COLUMN_ICON_URL = "icon_url";
    private static final String COLUMN_GAME_NAME = "game_name";
    private static final String COLUMN_PACKAGE_NAME = "package_name";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_ALLOW_BY_MOBILE_NET = "allow_by_mobile_net";
    private static final String COLUMN_RESERVE_JSON = "reserve_json";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REASON = "reason";
    private static final String COLUMN_COMPLETE_TIME = "complete_time";
    private static final String COLUMN_PROGRESS = "progress";
    private static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_RAW_DOWNLOAD_URL = "raw_download_url";
    private static final String COLUMN_FREE_TAG = "iccid";
    private static final String COLUMN_IS_SILENT_DOWNLOAD = "is_silent_download";
    private static final String COLUMN_VERSION_CODE = "version_code";
    private static final String COLUMN_INIT_TIME = "init_time";

    private static DownloadDB sInstance = new DownloadDB();
    private Handler mLoopHandler;
    private UpdateAllRunnable mLastUpdateAllRunnable;

    private DownloadDB() {
        super(BaseApplication.getAppContext(), DATABASE_NAME, null, DATABASE_VER);
        HandlerThread handlerThread = new HandlerThread("DownloadDBThread");
        handlerThread.start();
        mLoopHandler = new Handler(handlerThread.getLooper());
    }

    public static DownloadDB getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = getCreateTableSQL();
        db.execSQL(sb.toString());
    }

    private StringBuilder getCreateTableSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(TABLE_NAME);
        sb.append("(").append(COLUMN_ID).append(" integer primary key,");
        sb.append(COLUMN_FILE_PATH).append(" varchar,");
        sb.append(COLUMN_GAME_SIZE).append(" varchar,");
        sb.append(COLUMN_DOWNLOAD_URL).append(" varchar,");
        sb.append(COLUMN_GAME_ID).append(" integer,");
        sb.append(COLUMN_ICON_URL).append(" varchar,");
        sb.append(COLUMN_GAME_NAME).append(" varchar,");
        sb.append(COLUMN_PACKAGE_NAME).append(" varchar,");
        sb.append(COLUMN_SOURCE).append(" varchar,");
        sb.append(COLUMN_ALLOW_BY_MOBILE_NET).append(" integer,");
        sb.append(COLUMN_RESERVE_JSON).append(" TEXT,");
        sb.append(COLUMN_STATUS).append(" integer,");
        sb.append(COLUMN_REASON).append(" integer,");
        sb.append(COLUMN_COMPLETE_TIME).append(" integer,");
        sb.append(COLUMN_PROGRESS).append(" integer,");
        sb.append(COLUMN_TOTAL_SIZE_BYTES).append(" integer,");
        sb.append(COLUMN_START_TIME).append(" integer,");
        sb.append(COLUMN_RAW_DOWNLOAD_URL).append(" varchar,");
        sb.append(COLUMN_FREE_TAG).append(" varchar,");
        sb.append(COLUMN_IS_SILENT_DOWNLOAD).append(" integer,");
        sb.append(COLUMN_VERSION_CODE).append(" integer,");
        sb.append(COLUMN_INIT_TIME).append(" integer)");
        return sb;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < VER_ADD_START_TIME_FREE_TAG) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_START_TIME
                    + " INTEGER NOT NULL DEFAULT 0;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_RAW_DOWNLOAD_URL + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FREE_TAG + " TEXT;");
        }

        if (oldVersion < VER_ADD_SILENT_DOWNLOAD) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_IS_SILENT_DOWNLOAD
                    + " INTEGER NOT NULL DEFAULT 0;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_VERSION_CODE
                    + " INTEGER NOT NULL DEFAULT 0;");
        }

        if (oldVersion < VER_ADD_INIT_TIME) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_INIT_TIME
                    + " INTEGER NOT NULL DEFAULT 0;");
        }
    }

    public void tryCloseDB() {
        if (needClose()) {
            close();
        }
    }

    private boolean needClose() {
        return DownloadService.isTaskEmpty();
    }

    public void delete(final String pkgName) {
    	NomalThreadPool.getInstance().post(new Runnable() {
            @Override
            public void run() {
                synchronized (sInstance) {
                    SQLiteDatabase database;
                    try {
                        database = getWritableDatabase();
                        database.delete(TABLE_NAME, COLUMN_PACKAGE_NAME + " = ? ", new String[] {pkgName});
                    } catch (SQLiteException e) {
                        Log.e("TAG", e.getLocalizedMessage(), e);
                    }
                }
            }
        });
    }

    public void update(final DownloadInfo info) {
        if (info.mIsXunlei) {
            return;
        }

        NomalThreadPool.getInstance().post(new Runnable() {
            @Override
            public void run() {
                synchronized (sInstance) {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_STATUS, info.mStatus);
                    values.put(COLUMN_REASON, info.mReason);
                    values.put(COLUMN_TOTAL_SIZE_BYTES, info.mTotalSize);
                    values.put(COLUMN_PROGRESS, info.mProgress);
                    values.put(COLUMN_DOWNLOAD_URL, info.mDownloadUrl);
                    values.put(COLUMN_RAW_DOWNLOAD_URL, info.mRawDownloadUrl);
                    if (info.isCompleted()) {
                        values.put(COLUMN_COMPLETE_TIME, info.getCompleteTime());
                    }
                    values.put(COLUMN_ALLOW_BY_MOBILE_NET, info.mAllowByMobileNet ? 1 : 0);
                    values.put(COLUMN_RESERVE_JSON, info.mReserveJson);
                    values.put(COLUMN_START_TIME, info.mStartTime);
                    SQLiteDatabase database;
                    try {
                        database = getWritableDatabase();
                        database.update(DownloadDB.TABLE_NAME, values, DownloadDB.COLUMN_ID + " = ?",
                                new String[] {String.valueOf(info.mDownId)});
                    } catch (SQLiteException e) {
                        Log.e("TAG", e.getLocalizedMessage(), e);
                    }
                }
            }
        });
    }

    public long insert(DownloadInfo info) {
        synchronized (sInstance) {
            long retDownId = NO_DOWN_ID;
            ContentValues values = new ContentValues();
            values.put(COLUMN_FILE_PATH, info.mFilePath);
            values.put(COLUMN_GAME_SIZE, info.mGameSize);
            values.put(COLUMN_DOWNLOAD_URL, info.mDownloadUrl);
            values.put(COLUMN_GAME_ID, info.mGameId);
            values.put(COLUMN_ICON_URL, info.mIconUrl);
            values.put(COLUMN_GAME_NAME, info.mGameName);
            values.put(COLUMN_PACKAGE_NAME, info.mPackageName);
            values.put(COLUMN_SOURCE, info.mSource);
            values.put(COLUMN_ALLOW_BY_MOBILE_NET, info.mAllowByMobileNet ? 1 : 0);
            values.put(COLUMN_RESERVE_JSON, info.mReserveJson);
            values.put(COLUMN_STATUS, info.mStatus);
            values.put(COLUMN_REASON, info.mReason);
            values.put(COLUMN_PROGRESS, info.mProgress);
            values.put(COLUMN_TOTAL_SIZE_BYTES, info.mTotalSize);
            values.put(COLUMN_START_TIME, info.mStartTime);
            values.put(COLUMN_RAW_DOWNLOAD_URL, info.mDownloadUrl);
            values.put(COLUMN_IS_SILENT_DOWNLOAD, info.mIsSilentDownload ? 1 : 0);
            values.put(COLUMN_VERSION_CODE, info.mVersionCode);
            values.put(COLUMN_INIT_TIME, info.mInitTime);

            SQLiteDatabase database;
            try {
                database = getWritableDatabase();
                retDownId = database.insert(DownloadDB.TABLE_NAME, null, values);
            } catch (SQLiteException e) {
                Log.e("TAG", e.getLocalizedMessage(), e);
            }

            return retDownId;
        }
    }

    public ArrayList<DownloadInfo> queryAll(boolean isSilentDownload) {
        synchronized (sInstance) {
            SQLiteDatabase database;
            Cursor cursor = null;
            ArrayList<DownloadInfo> list = new ArrayList<DownloadInfo>();
            try {
                database = getWritableDatabase();
                String isSilentStr = isSilentDownload ? "1" : "0";
                cursor = database.query(TABLE_NAME, null, COLUMN_IS_SILENT_DOWNLOAD + "=?",
                        new String[] {isSilentStr}, null, null, null);
                int count = cursor.getCount();
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    DownloadInfo info = createDownloadInfo(cursor);
                    if (!info.isCompleted() || Utils.isFileExisting(info.mPackageName + Constant.APK)) {
                        list.add(info);
                    }
                }
            } catch (SQLiteException e) {
                Log.e("TAG", e.getLocalizedMessage(), e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return list;
        }
    }

    private DownloadInfo createDownloadInfo(Cursor cursor) {
        DownloadInfo info = new DownloadInfo();
        info.mDownId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        info.mFilePath = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_PATH));
        info.mGameSize = cursor.getString(cursor.getColumnIndex(COLUMN_GAME_SIZE));
        info.mDownloadUrl = cursor.getString(cursor.getColumnIndex(COLUMN_DOWNLOAD_URL));
        info.mGameId = cursor.getLong(cursor.getColumnIndex(COLUMN_GAME_ID));
        info.mIconUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ICON_URL));
        info.mGameName = cursor.getString(cursor.getColumnIndex(COLUMN_GAME_NAME));
        info.mPackageName = cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME));
        info.mSource = cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE));
        info.mAllowByMobileNet = cursor.getInt(cursor.getColumnIndex(COLUMN_ALLOW_BY_MOBILE_NET)) == 1;
        info.mReserveJson = cursor.getString(cursor.getColumnIndex(COLUMN_RESERVE_JSON));
        info.mStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
        info.mReason = cursor.getInt(cursor.getColumnIndex(COLUMN_REASON));
        info.setCompleteTime(cursor.getLong(cursor.getColumnIndex(COLUMN_COMPLETE_TIME)));
        info.mProgress = cursor.getLong(cursor.getColumnIndex(COLUMN_PROGRESS));
        info.mTotalSize = cursor.getLong(cursor.getColumnIndex(COLUMN_TOTAL_SIZE_BYTES));
        info.mStartTime = cursor.getLong(cursor.getColumnIndex(COLUMN_START_TIME));
        info.mIsSilentDownload = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SILENT_DOWNLOAD)) == 1;
        info.mVersionCode = cursor.getInt(cursor.getColumnIndex(COLUMN_VERSION_CODE));
        info.mRawDownloadUrl = cursor.getString(cursor.getColumnIndex(COLUMN_RAW_DOWNLOAD_URL));
        info.mIsXunlei = false;
        info.mInitTime = cursor.getLong(cursor.getColumnIndex(COLUMN_INIT_TIME));
        if (info.mInitTime == 0) {
            info.mInitTime = info.mStartTime;
        }

        return info;
    }

    public void updateAll(Collection<DownloadInfo> infos) {
        mLoopHandler.removeCallbacks(mLastUpdateAllRunnable);

        final ArrayList<DownloadInfo> infoList = new ArrayList<DownloadInfo>(infos);
        UpdateAllRunnable updateAllRunnable = new UpdateAllRunnable(infoList);
        mLoopHandler.post(updateAllRunnable);
        mLastUpdateAllRunnable = updateAllRunnable;
    }

    private class UpdateAllRunnable implements Runnable {
        private Collection<DownloadInfo> mInfos;

        public UpdateAllRunnable(Collection<DownloadInfo> infos) {
            mInfos = infos;
        }

        public void run() {
            synchronized (sInstance) {
                SQLiteDatabase database;
                try {
                    database = getWritableDatabase();
                    for (DownloadInfo info : mInfos) {
                        if (info.mIsXunlei) {
                            continue;
                        }

                        if (info.mStatus == DownloadStatusMgr.TASK_STATUS_DOWNLOADING || info.mNeedUpdateDB) {
                            ContentValues values = new ContentValues();
                            values.put(COLUMN_STATUS, info.mStatus);
                            values.put(COLUMN_REASON, info.mReason);
                            values.put(COLUMN_TOTAL_SIZE_BYTES, info.mTotalSize);
                            values.put(COLUMN_PROGRESS, info.mProgress);
                            values.put(COLUMN_ALLOW_BY_MOBILE_NET, info.mAllowByMobileNet ? 1 : 0);
                            values.put(COLUMN_RESERVE_JSON, info.mReserveJson);
                            database.update(DownloadDB.TABLE_NAME, values, DownloadDB.COLUMN_ID + " = ?",
                                    new String[] {String.valueOf(info.mDownId)});
                            info.mNeedUpdateDB = false;
                        }
                    }
                } catch (SQLiteException e) {
                    Log.e("TAG", e.getLocalizedMessage(), e);
                } finally {
                    mInfos.clear();
                }
            }
        }
    }

    public DownloadInfo getDownloadInfo(String pkgName) {
        synchronized (sInstance) {
            String[] selectionArgs = new String[] {pkgName};
            SQLiteDatabase database;
            Cursor cursor = null;
            DownloadInfo info = null;
            try {
                database = getWritableDatabase();
                cursor = database.query(TABLE_NAME, null, COLUMN_PACKAGE_NAME + "=?", selectionArgs, null,
                        null, null);
                if (isCursorValid(cursor)) {
                    info = createDownloadInfo(cursor);
                }
            } catch (SQLiteException e) {
                Log.e("TAG", e.getLocalizedMessage(), e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return info;
        }
    }

    private boolean isCursorValid(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0 && cursor.moveToFirst();
    }

}
