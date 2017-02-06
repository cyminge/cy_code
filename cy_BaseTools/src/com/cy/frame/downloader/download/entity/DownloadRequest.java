package com.cy.frame.downloader.download.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.cy.constant.Constant;

public class DownloadRequest extends DownloadArgs implements Parcelable {
    public String mFilePath = Constant.EMPTY;
    private static final String BLANK_TAG = " ";
    public boolean mAllowByMobileNet = false;
    public String mReserveJson = Constant.EMPTY;

    public DownloadRequest() {
    }

    public DownloadRequest(DownloadArgs args) {
        size = args.size;
        downUrl = args.downUrl.replaceAll(BLANK_TAG, Constant.EMPTY);
        gameId = args.gameId;
        gameid = args.gameid;
        name = args.name;
        packageName = args.packageName;
        mSource = args.mSource;
        mVersionCode = args.mVersionCode;
        mIsSilentDownload = args.mIsSilentDownload;
        mWifiAutoDownload = args.mWifiAutoDownload;
    }

    public static final Parcelable.Creator<DownloadRequest> CREATOR = new Parcelable.Creator<DownloadRequest>() {

        @Override
        public DownloadRequest createFromParcel(Parcel in) {
            return new DownloadRequest(in);
        }

        @Override
        public DownloadRequest[] newArray(int size) {
            return new DownloadRequest[size];
        }
    };

    public DownloadRequest(Parcel in) {
        mFilePath = in.readString();
        size = in.readString();
        downUrl = in.readString();
        gameId = in.readString();
        gameid = in.readLong();
        name = in.readString();
        packageName = in.readString();
        mSource = in.readString();
        mAllowByMobileNet = (Boolean) in.readValue(null);
        mReserveJson = in.readString();
        mIsSilentDownload = (Boolean) in.readValue(null);
        mVersionCode = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFilePath);
        dest.writeString(size);
        dest.writeString(downUrl);
        dest.writeString(gameId);
        dest.writeLong(gameid);
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeString(mSource);
        dest.writeValue(mAllowByMobileNet);
        dest.writeString(mReserveJson);
        dest.writeValue(mIsSilentDownload);
        dest.writeInt(mVersionCode);
    }
}
