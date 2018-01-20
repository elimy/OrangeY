package com.andy.orange.adapter;

import android.support.v4.app.FragmentManager;

import com.andy.orange.base.BaseFragment;
import com.andy.orange.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicMainPagerAdapter extends BaseFragmentAdapter {

    public MusicMainPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }

    public MusicMainPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> mTitles) {
        super(fm, fragmentList, mTitles);
    }
}
