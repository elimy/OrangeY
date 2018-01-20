package com.andy.orange.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.andy.orange.base.BaseApplication;

/**
 * Created by Administrator on 2017/8/24.
 */

public class DynamicTabUtil {


    public static void dynamicSetTabLayoutMode(TabLayout tabLayout, Context context){
        int tabWidth=calculateTabWidth(tabLayout);
        int screenWidth=getScreenWidth(context);
    }

    private static int calculateTabWidth(TabLayout tabLayout) {
        int tabWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0); // 通知父view测量，以便于能够保证获取到宽高
            tabWidth += view.getMeasuredWidth();
        }
        return tabWidth;

    }

    public static int getScreenWidth(Context context) {

        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }
}
