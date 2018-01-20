package com.andy.orange.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/27.
 */

public class DuanziActivity extends BaseActivity {
    @BindView(R.id.duan_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.duan_webView)
    WebView mWebView;
    private ProgressDialog mProgressDlg;
    private SweetAlertDialog sweetAlertDialog;


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_duanzi;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mToolbar.setTitle("搞笑段子");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mContext=this;
        mProgressDlg = new ProgressDialog(mContext);

        sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在努力加载中...");
        sweetAlertDialog.setCancelable(true);
        setWebView();
    }

    private void setWebView() {

        final String url="http://joke.bianxianmao.com?appKey=70e6374729e743118af31c3d6bb1828d";

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        int i = Build.VERSION.SDK_INT;
        try {
            webSettings.setAllowFileAccess(true);
            if (i>= 5) {
                webSettings.setDatabaseEnabled(true);
                webSettings.setGeolocationEnabled(true);
            }

            if (i>= 7) {
                webSettings.setAppCacheEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
            }
            if (i>= 8) {
                webSettings.setPluginState(WebSettings.PluginState.ON);
            }
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSupportZoom(false);
            webSettings.setAppCachePath(mContext.getCacheDir().getAbsolutePath());
        } catch (Exception e) {
        }

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(url);
                return true;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (!sweetAlertDialog.isShowing()){
                    sweetAlertDialog.show();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sweetAlertDialog.isShowing()){
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (sweetAlertDialog.isShowing()){
                    sweetAlertDialog.dismiss();
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress<=90){
                    //sweetAlertDialog.setProgress(newProgress);
                }else {
                    sweetAlertDialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode==event.KEYCODE_BACK)&&mWebView.canGoBack()){

            mWebView.goBack();

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
