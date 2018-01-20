package com.andy.orange.ui.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicMainPagerAdapter;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.ui.music.fragment.MeFragment;
import com.andy.orange.ui.music.fragment.MusicDisplayFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Lau on 2017/8/24.
 */

public class MusicActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bar_back_imageView)
    ImageView mBackImageView;

    @BindView(R.id.bar_me_imageView)
    ImageView mMeImageView;

    @BindView(R.id.bar_music_imageView)
    ImageView mMusicImageView;

    @BindView(R.id.music_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.music_viewPager)
    ViewPager mViewPager;

    private List<ImageView> tabs;
    private MusicDisplayFragment mMusicFragment;
    private List<BaseFragment> mFragments=new ArrayList<>();
    private MusicMainPagerAdapter mPagerAdapter;
    private MeFragment mMeFragment;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_music;
    }



    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mBackImageView.setOnClickListener(this);
        mMusicImageView.setOnClickListener(this);
        mMeImageView.setOnClickListener(this);

        tabs=new ArrayList<>();
        tabs.add(mMusicImageView);
        tabs.add(mMeImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_back_imageView:
                finish();
                break;
            case R.id.bar_music_imageView:
                mMusicImageView.setSelected(true);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.bar_me_imageView:
                mMeImageView.setSelected(true);
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化Fragment
        initFragment(savedInstanceState);
    }

    /*
    * 初始化fragment
    * */
    private void initFragment(Bundle state){

        if (null!=state){
           List<Fragment> fragments=getSupportFragmentManager().getFragments();
            for (Fragment fragment:fragments){
                if (fragment instanceof MeFragment){
                    mMeFragment= (MeFragment) fragment;
                }

                if (fragment instanceof MusicDisplayFragment){

                    mMusicFragment=(MusicDisplayFragment) fragment;

                }
            }
        }else {

            mMusicFragment=new MusicDisplayFragment();
            mMeFragment=new MeFragment();
        }

        setViewPager();
    }

    /*
    * 设置ViewPager
    * */
    private void setViewPager(){

        mFragments.add(mMusicFragment);
        mFragments.add(mMeFragment);
        mPagerAdapter=new MusicMainPagerAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        mMusicImageView.setSelected(true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabs (position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*
    * viewPager滑动的同时切换tabs
    * */
    private void switchTabs(int pos){
        for (int i=0;i<tabs.size();i++){
            if (pos==i){
                tabs.get(i).setSelected(true);
            }else {
                tabs.get(i).setSelected(false);
            }
        }
    }
}
