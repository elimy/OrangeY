package com.andy.orange.ui.music.fragment;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicViewPagerAdapter;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.utils.DynamicTabUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicDisplayFragment extends BaseFragment {

    @BindView(R.id.main_content)
    LinearLayout mLinearLayout;

    @BindView(R.id.music_tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.music_web_viewPager)
    ViewPager mViewPager;

    private String[] titles={"歌单","排行榜"};
    private Context cxt;
    private MusicViewPagerAdapter mFragmentAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_music_songs;
    }

    @Override
    protected void initView() {
        this.cxt=getActivity();
        loadMusicChannel();
    }

    private void loadMusicChannel() {

        List<BaseFragment> mMusicFragments=createListFragments();

        if (mFragmentAdapter==null){
            mFragmentAdapter=new MusicViewPagerAdapter(getActivity().getSupportFragmentManager(),mMusicFragments, Arrays.asList(titles));
        }else {
            mFragmentAdapter.setFragments(getActivity().getSupportFragmentManager(),mMusicFragments, Arrays.asList(titles));

        }

        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //动态布局tabLayout
        DynamicTabUtil.dynamicSetTabLayoutMode(mTabLayout,getActivity());

    }

    private List<BaseFragment> createListFragments() {
        List<BaseFragment> list=new ArrayList<>();
        list.add(new SongListFragment());
        list.add(new RankingFragment());

        return list;
    }
}
