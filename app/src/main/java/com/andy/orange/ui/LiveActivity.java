package com.andy.orange.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.widget.NestedScrollWebView;
import com.andy.orange.widget.ShareDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/27.
 */

public class LiveActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.live_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.live_webView)
    NestedScrollWebView mWebView;

    @BindView(R.id.share_icon)
    ImageView shareBtn;

    private ProgressDialog mProgressDlg;
    private SweetAlertDialog sweetAlertDialog;


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mToolbar.setTitle("直播");
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

        shareBtn.setOnClickListener(this);
    }

    private void setWebView() {

        final String url="http://live.bianxianmao.com/redirect.htm?appKey=70e6374729e743118af31c3d6bb1828d&appType=app&appEntrance=1";

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        WebSettings webSettings=mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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

    /*
    * 分享按钮点击事件监听
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_icon:

                final ShareDialog dialog=new ShareDialog(mContext,R.style.Theme_share_dialog);
                dialog.show();
                dialog.setShareItemClickListener(new ShareDialog.OnShareItemClickListener() {
                    @Override
                    public void onItemClick(View view) {

                        switch (view.getId()){
                            case R.id.btn_share_cancel:
                                dialog.dismiss();
                                break;
                            default:
                                Toast.makeText(mContext,"暂时不支持分享！",Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


/*    *//*
    * 创建选项菜单
    * *//*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.share_menu,menu);
        //menu.add(0,0,0,"分享");
        return true;
    }

    *//*
    * 菜单单机响应
    * *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                //intent.setType("image*//*");
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT,"share");
                intent.putExtra(Intent.EXTRA_TEXT,"一个来自橘子娱乐的分享\nhttp://live.bianxianmao.com/redirect.htm?appKey=70e6374729e743118af31c3d6bb1828d&appType=app&appEntrance=1");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent,getTitle()));

            return true;
        }
        return false;
    }

    *//*
    * 通过反射设置显示图标
    * *//*
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {

        if (menu!=null){
            if (menu.getClass()== MenuBuilder.class){
                try {
                Method m=menu.getClass().getDeclaredMethod("setOptionalIconsVisible");
                m.setAccessible(true);

                    m.invoke(menu,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
    */
}
