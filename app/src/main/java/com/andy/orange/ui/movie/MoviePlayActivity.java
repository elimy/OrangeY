package com.andy.orange.ui.movie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.widget.NestedScrollWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/8/21.
 */

public class MoviePlayActivity extends BaseActivity {

    @BindView(R.id.play_toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_webView)
    NestedScrollWebView mWebView;
    private String movieId="";
    private String mUrl="";
    private ProgressDialog mProgressDlg;
    private String movieUrl="";
    private  Handler handler;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_movie_play;
    }

    @Override
    public void initPresenter() {
        Intent intent=getIntent();
        movieId=intent.getStringExtra("movieId");
        mUrl="https://movie.douban.com/subject/"+movieId;
        //https://movie.douban.com/subject/3011091/
    }



    @Override
    public void initView() {

        toolbar.setTitle("电影播放");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        grabMoviePlayUrl();

        mProgressDlg = new ProgressDialog(mContext);

        sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在努力加载中...");
        sweetAlertDialog.setCancelable(true);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        movieUrl= (String) msg.obj;

                        Log.d("movieUrl",movieUrl);
                        setWebView(movieUrl);
                        break;
                    default:

                        Log.d("default",movieUrl);
                        break;
                }

            }
        };
    }

    /*
    * 抓取电影的播放链接
    * */
    private void grabMoviePlayUrl() {

        new Thread( new Runnable() {
            @Override
            public void run() {
                Document d;
                String playUrl="";
                String subPlayUrl="";
                try {

                    //获取到播放地址
                    d = Jsoup.connect(mUrl).get();

                    Elements element=d.select("a.playbtn");
                    for (int i=0;i<element.size();i++){

                        Log.d("element",element.get(i).attr("data-cn")+":"+element.get(i).attr("href"));
                    }

                    playUrl = d.select("a.playbtn").attr("href");
                    Log.d("playUrl", playUrl);

                    //匹配获取电影链接
                    subPlayUrl=matchUrl(playUrl);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message message=new Message();
                message.what=0;
                message.obj =subPlayUrl;

                handler.sendMessage(message);
            }
        }).start();
    }

    /*
    * 设置webView
    * */
    public void setWebView(final String url) {

        WebSettings websettings = mWebView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);

        websettings.setDomStorageEnabled(true);
        websettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.2; C1905 Build/15.1.C.2.8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");
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

                Toast.makeText(MoviePlayActivity.this,"发生未知错误!",Toast.LENGTH_SHORT).show();

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

    /*
    * 从豆瓣电影链接中获取并替换得到电影播放链接
    * */
    private String matchUrl(String url) {


        String resultUrl = "";

        //https://www.douban.com/link2/?url=http%3A%2F%2Fv.qq.com%2Fx%2Fcover%2Fuiq0rxuywu508qr.html%3Fptag%3Ddouban.movie&subtype=1&type=online-video

        Pattern p = Pattern.compile("http%3(.*)html");
        Matcher m = p.matcher(url);
        while (m.find()) {
            System.out.println(m.group(1));
            resultUrl = m.group(0);
            Log.d("matchUrl", m.group(0));
        }

        resultUrl = resultUrl.replace("%3A", ":");
        resultUrl = resultUrl.replace("%2F", "/");

        Log.d("resultUrl", resultUrl);

        return resultUrl;
    }


    /*
    * 拦截back键
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode==event.KEYCODE_BACK)&&mWebView.canGoBack()){

            mWebView.goBack();

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
