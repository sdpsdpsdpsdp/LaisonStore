package com.laisontech.laisondownloader.entitys.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：网络是否连接
 */
public class NetConnectEvent implements Parcelable {
    private boolean mNetConnect;

    public NetConnectEvent() {
    }

    public NetConnectEvent(boolean netConnect) {
        this.mNetConnect = netConnect;
    }

    public boolean isNetConnect() {
        return mNetConnect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mNetConnect ? (byte) 1 : (byte) 0);
    }

    protected NetConnectEvent(Parcel in) {
        this.mNetConnect = in.readByte() != 0;
    }

    public static final Creator<NetConnectEvent> CREATOR = new Creator<NetConnectEvent>() {
        @Override
        public NetConnectEvent createFromParcel(Parcel source) {
            return new NetConnectEvent(source);
        }

        @Override
        public NetConnectEvent[] newArray(int size) {
            return new NetConnectEvent[size];
        }
    };
}
