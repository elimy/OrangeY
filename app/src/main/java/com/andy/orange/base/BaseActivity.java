package com.andy.orange.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.andy.orange.R;
import com.andy.orange.utils.SettingStatusBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Andy Lau on 2017/8/9.
 * 定义一个基础的Activity抽象类,做一些通用的操作
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getChildLayoutId());

        //设置默认的状态栏颜色
        setStatusBarColor();
        mContext=this;
        mUnbinder= ButterKnife.bind(this);
        this.initPresenter();
        this.initView();
    }

    /*
    * 设置默认的状态栏颜色(4.4系统以上有效)
    * */
    protected void setStatusBarColor(){
        SettingStatusBar.setColor(this,getResources().getColor(R.color.colorPrimary));
    };

    /*
    * 用户自定义状态栏颜色(4.4系统以上有效)
    * */
    protected void setStatusBarColor(int color){
        SettingStatusBar.setColor(this,color);
    };

    /*
    * 在设置布局之前的配置
    * */
    private void beforeSetContentView(){

        //设置不显示标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /*
    *定义一个获取子类layoutId的抽象方法，在子类中实现
     */
    public abstract int getChildLayoutId();

    /*
    * 初始化Presenter
    * */
    public abstract void initPresenter();

    /*
    * 初始化view
    * */
    public abstract void initView();

    /*
    * 通过class无返回值跳转页面,并传递Bundle
    *
    */
    public void startActivity(Class<?> cls,Bundle bundle){
        Intent intent=new Intent();
        intent.setClass(this,cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /*
   * 通过class不带返回值跳转页面,无Bundle参数
   *
   */
    public void startActivity(Class cls){
        startActivity(cls,null);
    }

    /*
   * 通过class带返回值跳转页面,有Bundle参数
   *
   */
    public void  startActivityForResult(Class cls,Bundle bundle,int requestCode){

        Intent intent=new Intent(this,cls);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    /*
   * 通过class带返回值跳转页面,无Bundle参数
   *
   */
    public void  startActivityForResult(Class cls,int requestCode){

        startActivityForResult(cls,null,requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //用来重置绑定的view
        mUnbinder.unbind();

    }

}
