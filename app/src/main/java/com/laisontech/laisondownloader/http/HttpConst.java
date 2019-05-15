package com.laisontech.laisondownloader.http;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class HttpConst {
    //服务器地址
    public static final String DEFAULT_SERVER_ADDRESS = "http://47.91.105.10:10002/";
    //sp
    public static final String KEY_LAISON_SP_NAME = "LAISON_SP_NAME";
    //Email相关常量
    //邮箱账号和密码
    public static final String EMAIL_ACCOUNT = "ella.huo@laisontech.com";
    public static final String EMAIL_PWD = "laisonellaHHJ521";
    //使用的QQ邮箱服务器名称(主机名)
    public static final String EMAIL_HOST = "smtp.exmail.qq.com";
    //端口号
    public static final int EMAIL_PORT = 465;
    //超时时间
    public static final int EMAIL_TAIMEOUT = 6 * 1000;
    //字符编码
    public static final String EMAIL_CHAR = "UTF-8";

    public static final java.lang.String EMAIL_NAME = "Laison Users";
    public static final String EMAIL_SUBJECT = "Suggestion";

    public static final String ToEmail = "289112583@qq.com";

    public static final String COMPANY_LINK_ADDRESS = "https://www.linkedin.com/company/6410230";
    public static final String COMPANY_FACE_BOOK_ADDRESS = "https://www.facebook.com/laisontech";
    //服务器地址的key
    public static final String KEY_SERVER_URL = "ServerUrl";
    //网络请求
    public static final String FUNCTION = "function";//功能
    //请求成功了
    public static final int CODE_SUCCESS = 0;
    /*
    获取轮播
     */
    public static final String FUN_BANNER = "getadimage";
    public static final Object TAG_BANNER = "tagBanner";
    /*
    获取内容
     */
    public static final int PAGE = 6;
    public static final String FUN_LIST_CONTENT = "queryappoverviewinfo";
    public static final String START_INDEX = "startindex";
    public static final String APP_COUNT = "appcount";
    public static final Object TAG_CONTENT = "tagContent";
    /**
     * 根据ID获取App详情
     */
    public static final String FUN_APP_DETAIL_BY_ID = "queryappdetailsbyapkid";
    public static final String APK_ID = "apkid";
    public static final Object TAG_APP_DETAIL_BY_ID = "tagDetailsByApkId";
    /*
    根据包名获取App详情
     */
    public static final String FUN_APP_DETAIL_BY_PACKAGE = "queryappdetailsbyprojectname";
    public static final String APK_PACKAGE_NAME = "projectname";
    public static final Object TAG_APP_DETAIL_BY_PACKAGE = "tagDetailsByApkPackage";
    /**
     * 搜索App的信息
     */
    public static final String FUN_SEARCH_APPS = "fuzzysearchappoverviewinfo";
    public static final String QUERY_CODE = "querycode";
    public static final Object TAG_SEARCH = "tagSearch";

    /*
    App商店请求
     */
    public static final String FUN_APP_STORE = "getlatestversionofappstore";
    public static final Object TAG_APP_STORE = "tagAppStore";
}
