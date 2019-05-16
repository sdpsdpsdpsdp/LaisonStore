package com.laisontech.laisondownloader.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.global.GlobalData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SDP on 2017/10/19.
 * 对系统包进行检测以及查看
 */

public class PackageUtils {
    /*
    获取包的管理者
     */
    public static PackageManager getPM() {
        return GlobalData.getInstance().getLocalContext().getPackageManager();
    }

    /*
    获取当前App的包信息
     */
    private static PackageInfo getPackageInfo() {
        try {
            String packageName = GlobalData.getInstance().getLocalContext().getPackageName();
            return getPM().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
       获取包信息
        */
    public static PackageInfo getPackageInfo(String packageName) {
        try {
            return getPM().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检测一个app是否已经安装
     */
    public static boolean isAppInstalled(String packageName) {
        return getPackageInfo(packageName) != null;
    }

    /**
     * 卸载一个app
     */
    public static void uninstall(Context context, String packageName) {
        //通过程序的包名创建URI
        Uri packageURI = Uri.parse("package:" + packageName);
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
        //执行卸载程序
        context.startActivity(intent);
    }

    /**
     * 根据文件路径获取包名
     */
    public static String getPackageName(Context context, String filePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;  //得到安装包名称
        }
        return null;
    }

    /**
     * 根据文件路径获取包名
     */
    public static String getPackageName() {
        return GlobalData.getInstance().getLocalContext().getPackageName();
    }

    /**
     * 安装一个apk文件
     */
    public static boolean install(String filePath) {
        Context context = GlobalData.getInstance().getLocalContext();
        if (context == null) return false;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {
            boolean canRequestPackageInstalls = getPM().canRequestPackageInstalls();
            if (!canRequestPackageInstalls) {
                Toast.makeText(context, R.string.NeedInstallPerssion, Toast.LENGTH_SHORT).show();
                //该权限不是运行时权限，智能用户自己操作，所以需要用户自己调节
                //注意这个是8.0新API
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                context.startActivity(intent);
                return false;
            }
            return installApp(context, filePath);
        } else {
            return installApp(context, filePath);
        }
    }

    private static boolean installApp(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) return false;
        try {
            File apkFile = new File(filePath);
            if (!apkFile.isFile()) return false;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String authority = getPackageName() + ".fileprovider";
                Uri contentUri = FileProvider.getUriForFile(context, authority, apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                context.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 启动一个app
     */
    public static void launcherApp1(String packagename) {
        Context context = GlobalData.getInstance().getLocalContext();
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = getPackageInfo(packagename);
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /*
    启动一个App
     */
    public static void launcherApp(String packagename) {
        Context context = GlobalData.getInstance().getLocalContext();
        if (context == null || TextUtils.isEmpty(packagename)) return;
        context.startActivity(getPM().getLaunchIntentForPackage(packagename));
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName() {
        return getAppName(getPackageName());
    }

    /*
     * 获取程序的名字
     */
    public static String getAppName(String packname) {
        PackageInfo packageInfo = getPackageInfo(packname);
        if (packageInfo == null) return "";
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        return applicationInfo.loadLabel(getPM()).toString();
    }

    /*
     *获取程序的版本号
     */
    public static int getAppVersionCode(String packname) {
        PackageInfo packinfo = getPackageInfo(packname);
        return packinfo == null ? -1 : packinfo.versionCode;
    }

    /*
     *获取程序的版本号
     */
    public static int getAppVersionCode() {
        PackageInfo packinfo = getPackageInfo();
        return packinfo == null ? -1 : packinfo.versionCode;
    }

    /*
     *获取版本号名
     */
    public static String getAppVersionName(String packname) {
        PackageInfo packinfo = getPackageInfo(packname);
        return packinfo == null ? "" : packinfo.versionName;
    }

    public static String getAppVersionName() {
        PackageInfo packinfo = getPackageInfo();
        return packinfo == null ? "" : packinfo.versionName;
    }

    /**
     * 获取程序 图标
     *
     * @param packname 应用包名
     * @return
     */
    public static Drawable getAppIcon(String packname) {
        //获取到应用信息
        try {
            PackageManager pm = getPM();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前展示 的Activity名称
     */
    private static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity;
        runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    //通过反射获取app的大小 M
    public static double getPkgSize(final ApplicationInfo appInfo) {
        String dir = appInfo.publicSourceDir;
        int size = Integer.valueOf((int) new File(dir).length());
        BigDecimal apkSize = parseApkSize(size);
        return apkSize.doubleValue();
    }

    //单位kb
    // 1m = 1024kb  1kb = 1024b
    private static BigDecimal parseApkSize(int size) {
        BigDecimal bd = new BigDecimal((double) size / (1024 * 1024));
        BigDecimal setScale = bd.setScale(2, BigDecimal.ROUND_DOWN);
        return setScale;
    }


    /**
     * 获取公司的文件目录
     */
    public static String getLaisonProjectPath(Context context, String fileType) {
        if (context == null) return "Error";
        if (TextUtils.isEmpty(fileType)) return "default";
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new IllegalStateException("No SdCard!");
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "com.laisontech.filefolder"
                + File.separator
                + fileType
                + File.separator
                + getAppName()
                + File.separator;
    }

    public static byte[] getDrawableTypes(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            return baos.toByteArray();
        }
        return null;
    }
}
