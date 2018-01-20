package com.andy.orange.ui.movie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.andy.orange.R;
import com.andy.orange.adapter.MovieViewPagerAdapter;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MovieDisplayActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.select_tabs)
    TabLayout mTabLayout;

/*    @BindView(R.id.fab_btn)
    FloatingActionButton mFab;*/

    @BindView(R.id.film_content)
    ViewPager mViewPager;

    //tabs中英文分类
    private String[] mChTypes={"喜剧","动作","科幻","犯罪","战争","动画","爱情","恐怖","记录片"};
    private String[] mEnTypes={"comedy","action","fantasy","crime","war","animation","romance","horror","documentary"};

    private  MovieViewPagerAdapter mFragmentAdapter;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_movie;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mToolbar.setTitle("电影");
        setSupportActionBar(mToolbar);
        loadMovieChannel();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabScrollBean fabScrollBean =new FabScrollBean();
                fabScrollBean.setTop(true);
                EventBus.getDefault().post(fabScrollBean);
            }
        });*/
    }

    private void loadMovieChannel(){

        List<BaseFragment> mFragments=new ArrayList<>();
        for (int i=0;i<mChTypes.length;i++){
            mFragments.add(createListFragments(i));
            //mFragmentAdapter=new MovieViewPagerAdapter(getSupportFragmentManager(),mFragments, Arrays.asList(mChTypes));
        }

        if (mFragmentAdapter==null){
            mFragmentAdapter=new MovieViewPagerAdapter(getSupportFragmentManager(),mFragments, Arrays.asList(mChTypes));

        }else {
            mFragmentAdapter.setFragments(getSupportFragmentManager(),mFragments, Arrays.asList(mChTypes));
        }

        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


        //设置tabs的正常色和选中色
        mTabLayout.setTabTextColors(getResources().getColor(R.color.white),getResources().getColor(R.color.black));
    }

    /*
    * 根据电影类型创建fragment
    * */
    private BaseFragment createListFragments(int i) {

        MovieFragment fragment=new MovieFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",mChTypes[i]);
        fragment.setArguments(bundle);
        return fragment;

    }

}
