package com.andy.orange.adapter;

import android.support.v4.app.FragmentManager;

import com.andy.orange.base.BaseFragment;
import com.andy.orange.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicViewPagerAdapter extends BaseFragmentAdapter {
    public MusicViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> mTitles) {
        super(fm, fragmentList, mTitles);
    }

    public MusicViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }
}
