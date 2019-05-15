package com.laisontech.laisondownloader.http;

import android.text.TextUtils;

import com.laisontech.laisondownloader.db.OperateDBUtils;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.db.OperateDBUtils;
import com.laisontech.mvp.laisonjsonparse.ResultParseUtils;
import com.laisontech.mvp.net.OnConnectResultListener;
import com.laisontech.mvp.net.okconnect.OkHttpConnect;

import java.util.List;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class HttpClient {
    /**
     * 根据TAG关闭网络请求
     */
    public static void cancelTag(Object tag) {
        OkHttpConnect.getInstance().cancelTag(tag);
    }

    /**
     * 请求广告头
     */
    public static void queryBanner(final OnAssignmentResultListener<List<LaisonAppAdData>> listener) {
        String bannerUrl = buildGetUrl(HttpConst.FUN_BANNER);
        OkHttpConnect.getInstance().buildGetString(bannerUrl, HttpConst.TAG_BANNER, new OnConnectResultListener() {
            @Override
            public void onResponse(String json, Object tag) {
                ResultParseUtils.ParseResultBean result = ResultParseUtils.getResult(json, LaisonAppAdData.class, ResultParseUtils.ResultType.LIST);
                listener.onSuccess(result.getErrorCode(), (List<LaisonAppAdData>) result.getParseResult());
            }

            @Override
            public void onError(String s, Object tag) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 请求列表
     */
    public static void queryContentData(int index, final OnAssignmentResultListener<List<LaisonAppBean>> listener) {
        String connectUrl = buildGetUrl(HttpConst.FUN_LIST_CONTENT,
                HttpConst.START_INDEX, index * HttpConst.PAGE,
                HttpConst.APP_COUNT, HttpConst.PAGE);
        OkHttpConnect.getInstance().buildGetString(connectUrl, HttpConst.TAG_CONTENT, new OnConnectResultListener() {
            @Override
            public void onResponse(String json, Object tag) {
                ResultParseUtils.ParseResultBean result = ResultParseUtils.getResult(json, LaisonAppBean.class, ResultParseUtils.ResultType.LIST);
                listener.onSuccess(result.getErrorCode(), (List<LaisonAppBean>) result.getParseResult());
            }

            @Override
            public void onError(String s, Object tag) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 获取App详情信息根据包名
     */
    public static void queryAppDetailByPackage(String appPackageName, final OnAssignmentResultListener<LaisonAppBean> listener) {
        String queryDetailUrl = buildGetUrl(HttpConst.FUN_APP_DETAIL_BY_PACKAGE,
                HttpConst.APK_PACKAGE_NAME, appPackageName);
        OkHttpConnect.getInstance().buildGetString(queryDetailUrl, HttpConst.TAG_APP_DETAIL_BY_PACKAGE, new OnConnectResultListener() {
            @Override
            public void onResponse(String json, Object tag) {
                parseEntity(json, LaisonAppBean.class, listener, ResultParseUtils.ResultType.OBJECT);
            }

            @Override
            public void onError(String s, Object tag) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 获取App详情信息根据AppID
     */
    public static void queryAppDetailByID(int apkId, final OnAssignmentResultListener<LaisonAppBean> listener) {
        String queryDetailUrl = buildGetUrl(HttpConst.FUN_APP_DETAIL_BY_ID,
                HttpConst.APK_ID, apkId);
        OkHttpConnect.getInstance().buildGetString(queryDetailUrl, HttpConst.TAG_APP_DETAIL_BY_ID, new OnConnectResultListener() {
            @Override
            public void onResponse(String json, Object tag) {
                parseEntity(json, LaisonAppBean.class, listener, ResultParseUtils.ResultType.OBJECT);
            }

            @Override
            public void onError(String s, Object tag) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 搜索App信息
     */
    public static void searchAppList(String searchRecord, int index, final OnAssignmentResultListener<List<LaisonAppBean>> listener) {
        String url = buildGetUrl(HttpConst.FUN_SEARCH_APPS,
                HttpConst.QUERY_CODE, searchRecord,
                HttpConst.START_INDEX, index * HttpConst.PAGE,
                HttpConst.APP_COUNT, HttpConst.PAGE);
        OkHttpConnect.getInstance().buildGetString(url, HttpConst.TAG_SEARCH, new OnConnectResultListener() {
            @Override
            public void onResponse(String json, Object o) {
                ResultParseUtils.ParseResultBean result = ResultParseUtils.getResult(json, LaisonAppBean.class, ResultParseUtils.ResultType.LIST);
                listener.onSuccess(result.getErrorCode(), (List<LaisonAppBean>) result.getParseResult());
            }

            @Override
            public void onError(String s, Object o) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 查询App更新信息
     */
    public static void queryAppStoreInfo(final OnAssignmentResultListener<LaisonAppBean> listener) {
        String url = buildGetUrl(HttpConst.FUN_APP_STORE);
        OkHttpConnect.getInstance().buildGetString(url, HttpConst.TAG_APP_STORE, new OnConnectResultListener() {
            @Override
            public void onResponse(String s, Object o) {
                parseEntity(s, LaisonAppBean.class, listener, ResultParseUtils.ResultType.OBJECT);
            }

            @Override
            public void onError(String s, Object o) {
                listener.onFailed(s);
            }
        });
    }

    /**
     * 构建get请求的数据
     *
     * @param funName fun名称
     * @param params  参数
     * @return get请求URL
     */
    public static String buildGetUrl(String funName, Object... params) {
        String baseUrl = baseGetUrl();
        if (TextUtils.isEmpty(funName)) return baseUrl;
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append(HttpConst.FUNCTION).append("=").append(funName);
        if (params != null && params.length > 0 && params.length % 2 == 0) {
            sb.append("&");
            for (int i = 0; i < params.length; i += 2) {
                Object key = params[i];
                Object value = params[i + 1];
                sb.append(key).append("=").append(value);
                if (i + 1 < params.length - 1) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    /**
     * @param json     网路请求获取到的json
     * @param clzT     要转换的实体了
     * @param listener 接口传递消息
     */
    private static <T> void parseEntity(String json, Class<T> clzT, OnAssignmentResultListener<T> listener, ResultParseUtils.ResultType type) {
        if (listener == null) return;
        if (clzT == null) {
            listener.onFailed("Failure to pass in the correct entity class!");
            return;
        }
        ResultParseUtils.ParseResultBean result = ResultParseUtils.getResult(json, clzT, type);
        Object parseResult = result.getParseResult();
        listener.onSuccess(result.getErrorCode(), parseResult == null ? null : (T) parseResult);
    }


    private static String baseGetUrl() {
        return serverAddress() + "?";
    }

    private static String serverAddress() {
        return OperateDBUtils.getServerAddress();
    }


}
