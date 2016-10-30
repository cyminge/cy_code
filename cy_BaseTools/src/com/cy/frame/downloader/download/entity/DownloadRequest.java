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
        mGameSize = args.mGameSize;
        mDownloadUrl = args.mDownloadUrl.replaceAll(BLANK_TAG, Constant.EMPTY);
        mGameId = args.mGameId;
        mIconUrl = args.mIconUrl;
        mGameName = args.mGameName;
        mPackageName = args.mPackageName;
        mSource = args.mSource;
        mVersionCode = args.mVersionCode;
        mIsSilentDownload = args.mIsSilentDownload;
        mAutoDownload = args.mAutoDownload;
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
        mGameSize = in.readString();
        mDownloadUrl = in.readString();
        mGameId = in.readLong();
        mIconUrl = in.readString();
        mGameName = in.readString();
        mPackageName = in.readString();
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
        dest.writeString(mGameSize);
        dest.writeString(mDownloadUrl);
        dest.writeLong(mGameId);
        dest.writeString(mIconUrl);
        dest.writeString(mGameName);
        dest.writeString(mPackageName);
        dest.writeString(mSource);
        dest.writeValue(mAllowByMobileNet);
        dest.writeString(mReserveJson);
        dest.writeValue(mIsSilentDownload);
        dest.writeInt(mVersionCode);
    }
}
