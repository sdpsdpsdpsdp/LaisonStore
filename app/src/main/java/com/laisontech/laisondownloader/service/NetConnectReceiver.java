package com.laisontech.laisondownloader.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import com.laisontech.laisondownloader.callback.OnNetConnectStatusListener;

/**
 * Created by SDP on 2017/8/18.
 */

public class NetConnectReceiver extends BroadcastReceiver {
    private static final String TAG = NetConnectReceiver.class.getSimpleName();
    private OnNetConnectStatusListener mConnectListener;

    public NetConnectReceiver() {
    }

    public NetConnectReceiver(OnNetConnectStatusListener listener) {
        this.mConnectListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取ConnectivityManager对象对应的NetworkInfo对象
        //获取WIFI连接的信息
        assert connMgr != null;
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Log.e(TAG, "WIFI已连接,移动数据已连接");
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(true);
                }
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Log.e(TAG, "WIFI已连接,移动数据已断开");
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(true);
                }
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Log.e(TAG, "WIFI已断开,移动数据已连接");
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(true);
                }
            } else {
                Log.e(TAG, "WIFI已断开,移动数据已断开");
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(false);
                }
            }
        } else {
            Network[] networks = connMgr.getAllNetworks();
            if (networks.length < 1) {
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(false);
                }
            } else {
                if (mConnectListener != null) {
                    mConnectListener.onConnectStatus(true);
                }
            }
        }
    }
}
