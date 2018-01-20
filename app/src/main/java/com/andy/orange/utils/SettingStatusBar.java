package com.andy.orange.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.andy.orange.R;
import com.andy.orange.widget.StatusBarView;

/**
 * Created by Andy Lau on 2017/8/9.
 */

public class SettingStatusBar {
    public static final int DEFAULT_STATUS_BAR_ALPHA=112;
    public static final int FAKE_STATUS_BAR_VIEW_ID= R.id.drawer_layout;
    public static final int FAKE_TRANSLUCENT_VIEW_ID=R.id.drawer_layout;
    private static final int COLOR_TRANSLUCENT= Color.BLACK;

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    public static void setColor(Activity activity,@ColorInt int color,int statusBarAlpha){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color,statusBarAlpha));
        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView =decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
            if (statusBarView!=null){
                if (statusBarView.getVisibility()==View.GONE){
                    statusBarView.setVisibility(View.VISIBLE);
                }
                statusBarView.setBackgroundColor(calculateStatusColor(color,statusBarAlpha));
            }else {
                decorView.addView(createStatusBarView(activity,color,statusBarAlpha));
            }
            setRootView(activity);
        }
    }


    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColor(Activity activity, @ColorInt int color){
        setColor(activity,color,DEFAULT_STATUS_BAR_ALPHA);
    }

    /*
    * 创建一个和状态栏一样大小的的半透明矩形条
    * */
    private static View createStatusBarView(Activity activity, int color, int statusBarAlpha) {
        StatusBarView mStatusBar=new StatusBarView(activity);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
        mStatusBar.setLayoutParams(params);
        mStatusBar.setBackgroundColor(calculateStatusColor(color,statusBarAlpha));
        mStatusBar.setId(FAKE_STATUS_BAR_VIEW_ID);
        return mStatusBar;
    }

    /**
     * 创建半透明状态栏 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity,int alpha){

        StatusBarView mStatusBarView=new StatusBarView(activity);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
        mStatusBarView.setLayoutParams(params);
        mStatusBarView.setBackgroundColor(Color.argb(alpha,0,0,0));
        mStatusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return mStatusBarView;
    }

    /*
    * 获取状态栏高度
    * */
    private static int getStatusBarHeight(Context ctx) {
        int id=ctx.getResources().getIdentifier("status_bar_height","dimen", "android");
        return ctx.getResources().getDimensionPixelSize(id);
    }

    /*
    * 设置根布局参数
    * */
    private static void setRootView(Activity activity) {
        ViewGroup parent= (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i=0,count=parent.getChildCount();i<count;i++){
            View childView=parent.getChildAt(i);
            if (childView instanceof ViewGroup){
                childView.setFitsSystemWindows(true);
                ((ViewGroup)childView).setClipToPadding(true);
            }
        }
    }

    /*
    * 计算状态栏的颜色
    * */
    private static int calculateStatusColor(@ColorInt int color,int alpha){
        if (alpha==0){
            return color;
        }
        float a=1-alpha/255f;
        int red=color>>16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;

        red= (int) (red*a+0.5);
        green = (int) (green*a+0.5);
        blue = (int) (blue*a+0.5);

        return 0xff<<24|red<<16|green<<8|blue;
    }

    /*
    * 设置DrawerLayout状态栏颜色
    * */
    public static void setStatusColorForDrawerLayout(Activity activity, DrawerLayout drawerlayout ,
                                                     @ColorInt int color,int alpha){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            return;
        }
        //5.0以上系统
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color,alpha));
        }else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentLayout= (ViewGroup) drawerlayout.getChildAt(0);
            View statusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (statusBarView!=null){
                if(statusBarView.getVisibility()==View.GONE){
                    statusBarView.setVisibility(View.VISIBLE);
                }else {
                    statusBarView.setBackgroundColor(color);
                }
            }else {
                contentLayout.addView(createStatusBarView(activity,color,0),0);
            }

            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1)!=null){
                contentLayout.getChildAt(1)
                        .setPadding(contentLayout.getPaddingLeft(),getStatusBarHeight(activity)+contentLayout.getPaddingTop(),
                                contentLayout.getPaddingRight(),contentLayout.getPaddingBottom());
            }
        }
        setDrawerLayoutProperty(drawerlayout);
    }

    /*
    * 设置DrawerLayout
    * */
    private static void setDrawerLayoutProperty(DrawerLayout drawerlayout) {
        ViewGroup contentLayout= (ViewGroup) drawerlayout.getChildAt(0);
        ViewGroup drawer= (ViewGroup) drawerlayout.getChildAt(1);
        drawerlayout.setFitsSystemWindows(false);
        contentLayout.setFitsSystemWindows(false);
        contentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }
}
