package com.laisontech.laisondownloader.entitys.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by SDP on 2017/10/19.
 * 从服务端获取
 */

public class LaisonAppBean implements Serializable {
    private static final long serialVersionUID = 2072893447591548402L;
    //App的状态
    public static final int TYPE_SERVER = 0x110;
    public static final int TYPE_LOCAL = 0x111;


    @IntDef({TYPE_SERVER, TYPE_LOCAL})
    public @interface AppType {

    }

    //app的kid
    private int apkid = -1;
    //app的名称
    private String appname;
    //公司名称
    private String companyname;
    //app的包名
    private String projectname;
    //app版本号
    private int versioncode;
    //详细版本名
    private String versionname;
    //APK文件大小，单位M
    private double apkfilesize;
    //app的图标下载链接
    private String applogolink;
    //app的下载地址
    private String apkdownloadlink;
    //app的描述
    private String appintroduction;
    //最近更新日期
    private String updatedate;
    //APP最近的更新说明
    private String updatedescription;
    //app的等级
    private int priority;
    //app的icon drawable;
    private transient Drawable localDrawable;
    //增加tag用于判断是本地安装的还是从服务端下载的，默认将更新的也当做服务端下载的处理,除非删除任务(暂不做)
    private @AppType
    int appType = TYPE_SERVER;

    private boolean isNeedUpdate = false;

    private boolean isDataChange = false;

    public LaisonAppBean() {
        setAppPri();
    }

    public LaisonAppBean(String appDownloadUrl, String versionName, String introduce, String updateIntroduce, long appLength, String appCompany, String appPackageName, String appName, int appNewVersion, String appUrl) {
        this.apkdownloadlink = appDownloadUrl;
        this.apkfilesize = appLength;
        this.companyname = appCompany;
        this.projectname = appPackageName;
        this.appname = appName;
        this.versioncode = appNewVersion;
        this.applogolink = appUrl;
        versionname = versionName;
        appintroduction = introduce;
        updatedescription = updateIntroduce;
        setAppPri();
    }


    public LaisonAppBean(int apkid, String appname, String companyname, String projectname, int versioncode, String versionname, double apkfilesize, String applogolink, String apkdownloadlink, String appintroduction, String updatedate, String updatedescription, int appType) {
        this.apkid = apkid;
        this.appname = appname;
        this.companyname = companyname;
        this.projectname = projectname;
        this.versioncode = versioncode;
        this.versionname = versionname;
        this.apkfilesize = apkfilesize;
        this.applogolink = applogolink;
        this.apkdownloadlink = apkdownloadlink;
        this.appintroduction = appintroduction;
        this.updatedate = updatedate;
        this.updatedescription = updatedescription;
        this.appType = appType;
        setAppPri();
    }

    public LaisonAppBean(String appname, String projectname, int versioncode, String versionname, double apkfilesize, Drawable localDrawable, int appType) {
        this.appname = appname;
        this.projectname = projectname;
        this.versioncode = versioncode;
        this.versionname = versionname;
        this.apkfilesize = apkfilesize;
        this.localDrawable = localDrawable;
        this.appType = appType;
        setAppPri();
    }

    private void setAppPri() {
        Random random = new Random();
        priority = random.nextInt(100);
    }
    /*
    //app的描述
    private String appintroduction;
    //最近更新日期
    private String updatedate;
     */

    /**
     * 设置更新的App信息
     */
    public void setNewVersionInfo(LaisonAppBean appNet) {
        if (appNet == null) return;
        setApkId(appNet.getApkId());
        setAppName(appNet.getAppName());
        setCompanyName(appNet.getCompanyName());
        setProjectName(appNet.getProjectName());
        setVersionCode(appNet.getVersioncode());
        setVersionName(appNet.getVersionName());
        setApkFileSize(appNet.getApkFileSize());
        setAppLogoLink(appNet.getAppLogoLink());
        setApkDownloadLink(appNet.getApkDownloadLink());
        setAppIntroduction(appNet.getAppIntroduction());
        setUpdateDate(appNet.getUpdateDate());
    }

    public int getApkId() {
        return apkid;
    }

    public void setApkId(int apkid) {
        this.apkid = apkid;
    }

    public String getAppName() {
        return appname;
    }

    public void setAppName(String appName) {
        this.appname = appName;
    }

    public String getCompanyName() {
        return companyname;
    }

    public void setCompanyName(String companyname) {
        this.companyname = companyname;
    }

    public String getProjectName() {
        return projectname;
    }

    public void setProjectName(String appPackageName) {
        this.projectname = appPackageName;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersionCode(int appNewVersion) {
        this.versioncode = appNewVersion;
    }

    public String getVersionName() {
        return versionname;
    }

    public void setVersionName(String versionname) {
        this.versionname = versionname;
    }

    public double getApkFileSize() {
        return apkfilesize;
    }

    public void setApkFileSize(double appLength) {
        this.apkfilesize = appLength;
    }


    public String getAppLogoLink() {
        return applogolink;
    }

    public void setAppLogoLink(String appIconUrl) {
        this.applogolink = appIconUrl;
    }

    public String getApkDownloadLink() {
        return apkdownloadlink;
    }

    public void setApkDownloadLink(String appDownloadUrl) {
        this.apkdownloadlink = appDownloadUrl;
    }

    public String getAppIntroduction() {
        return appintroduction;
    }

    public void setAppIntroduction(String appDes) {
        this.appintroduction = appDes;
    }

    public String getUpdateDate() {
        return updatedate;
    }

    public void setUpdateDate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getUpdateDescription() {
        return updatedescription;
    }

    public void setUpdateDescription(String updatedescription) {
        this.updatedescription = updatedescription;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public Drawable getLocalDrawable() {
        return localDrawable;
    }

    public void setLocalDrawable(Drawable appDrawable) {
        this.localDrawable = appDrawable;
    }

    public @AppType
    int getAppType() {
        return appType;
    }

    public void setAppType(@AppType int appType) {
        this.appType = appType;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public boolean isDataChange() {
        return isDataChange;
    }

    public void setDataChange(boolean dataChange) {
        isDataChange = dataChange;
    }

    @Override
    public String toString() {
        return "LaisonAppBean{" +
                "apkid=" + apkid +
                ", appname='" + appname + '\'' +
                ", companyname='" + companyname + '\'' +
                ", projectname='" + projectname + '\'' +
                ", versioncode=" + versioncode +
                ", versionname='" + versionname + '\'' +
                ", apkfilesize=" + apkfilesize +
                ", applogolink='" + applogolink + '\'' +
                ", apkdownloadlink='" + apkdownloadlink + '\'' +
                ", appintroduction='" + appintroduction + '\'' +
                ", updatedate='" + updatedate + '\'' +
                ", updatedescription='" + updatedescription + '\'' +
                ", priority=" + priority +
                ", localDrawable=" + localDrawable +
                ", appType=" + appType +
                ", isNeedUpdate=" + isNeedUpdate +
                ", isDataChange=" + isDataChange +
                '}';
    }
}
