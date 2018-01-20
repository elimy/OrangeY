package com.andy.orange.ui.coder;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/26.
 */

public class CoderActivity extends BaseActivity {

    @BindView(R.id.coder_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.coder_collapsing_layout)
    CollapsingToolbarLayout mCollapsingLayout;

    @BindView(R.id.coder_toolbar)
    Toolbar mToolbar;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_coder;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mToolbar.setTitle("关于作者");
        mCollapsingLayout.setTitleEnabled(false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
