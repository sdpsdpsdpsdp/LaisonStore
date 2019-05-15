package com.laisontech.laisondownloader.customeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.laisontech.laisondownloader.R;


/**
 * Created by 80926 on 2016/12/15.
 * 自定义带进度条的webView
 */

public class ProgressWebView extends LinearLayout {

    WebView mWebView;

    ProgressBar mProgressBar;

    private Context mContext;

    private String url;


    public ProgressWebView(Context context) {
        this(context, null);
    }


    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_web_progress, this);
        mWebView = (WebView) view.findViewById(R.id.web_view_progress);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void loadUrl(String url) {
        if (url.isEmpty() || url.equals("")) {
            Toast.makeText(mContext, "网址为空，不能打开连接", Toast.LENGTH_SHORT).show();
        }
        initWebview(url);
    }


    @SuppressLint("JavascriptInterface")
    private void initWebview(String url) {
        mWebView.addJavascriptInterface(this, "android");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
// 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
//自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.loadUrl(url);

        // 设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                Log.e("testUrl", url);
                if (url.contains("tel:")) {
                    String[] tels = url.split(":");
                    if (tels.length == 2) {
                        call(tels[1]);
                    }
                } else {
                    view.loadUrl(url);
                }
                // 相应完成返回true
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

        });

        // 设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            // 设置网页加载的进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 设置程序的Title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //增加了可以设置title的选项
            }
        });
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) { // 表示按返回键
                        if (mWebView.canGoBack()) {
                            mWebView.goBack(); // 后退
                            return true; // 已处理
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url.endsWith(".apk")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        mContext.startActivity(intent);
    }

    public boolean canBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public void goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void goForward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    public void refreshWeb() {
        mWebView.reload();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
