package com.potent.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.potent.R;
import com.potent.common.Constants;
import com.potent.ui.base.BaseActivity;
import com.potent.util.SPUtils;

import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Potent
 * @description: TODO<帮助>
 * @date: 2014/12/28 11:58
 */
public class ActivityHelp extends BaseActivity {
    @BindView(R.id.webview_plyer)
    WebView webview_player;
    private View myView;
    private SharedPreferences m_wsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_webview_activity);
        ButterKnife.bind(this);
        showBack(getString(R.string.title_activity_activity_main), null);
        m_wsp = SPUtils.getInstance(getApplicationContext(), Constants.SPNAME, Context.MODE_PRIVATE);
        String Language = m_wsp.getString(Constants.Language, "English");
        String resPath = "file:///android_asset/help_English.html";
        if (!Language.equals("English")) {
            resPath = "file:///android_asset/help_fayu.html";
        }
        try {
            webview_player.setWebChromeClient(mChromeClient);
            webview_player.setWebViewClient(mWebViewClient);
            webview_player.getSettings().setJavaScriptEnabled(true);
            webview_player.getSettings().setDomStorageEnabled(true);
            webview_player.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webview_player.getSettings().setAllowFileAccess(true);
            webview_player.getSettings().setPluginState(WebSettings.PluginState.OFF);
            webview_player.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webview_player.getSettings().setUserAgentString(
                    webview_player.getSettings().getUserAgentString());
            webview_player.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview_player.getSettings().setBuiltInZoomControls(true);
            webview_player.getSettings().setUseWideViewPort(true);
            webview_player.loadUrl(resPath);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        webview_player.loadUrl(resPath);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//             this.wakeLock.acquire();
        try {
            webview_player.getClass().getMethod("onResume").invoke(webview_player, (Object[]) null);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // mChromeClient.
//             mWebView.onResume();
        webview_player.resumeTimers();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            webview_player.getClass().getMethod("onPause").invoke(webview_player, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        webview_player.stopLoading();
        webview_player.destroy();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        // 处理页面导航
        private WebChromeClient.CustomViewCallback myCallback = null;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }


    };

    private WebChromeClient mChromeClient = new WebChromeClient() {

        private CustomViewCallback myCallback = null;


        /**
         * 加载进度改变时调用
         **/
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
        }

        // 配置权限 （在WebChromeClinet中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        // 扩充数据库的容量（在WebChromeClinet中实现）
        @Override
        public void onExceededDatabaseQuota(String url,
                                            String databaseIdentifier, long currentQuota,
                                            long estimatedSize, long totalUsedQuota,
                                            WebStorage.QuotaUpdater quotaUpdater) {

            quotaUpdater.updateQuota(estimatedSize * 2);
        }


        // Android 使WebView支持HTML5 Video（全屏）播放的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }

            ViewGroup parent = (ViewGroup) webview_player.getParent();
            parent.removeView(webview_player);
            parent.addView(view);
            myView = view;
            myCallback = callback;

        }

        @Override
        public void onHideCustomView() {
            if (myView != null) {
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }

                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                parent.addView(webview_player);
                myView = null;
            }
        }


    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview_player.canGoBack()) {
                webview_player.goBack();
                mChromeClient.onHideCustomView();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
