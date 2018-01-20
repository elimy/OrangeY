package com.andy.orange.adapter;

import android.support.v4.app.FragmentManager;

import com.andy.orange.base.BaseFragment;
import com.andy.orange.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by Andy Lau on 2017/8/17.
 */

public class MovieViewPagerAdapter extends BaseFragmentAdapter {

    public MovieViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }

    public MovieViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> mTitles) {
        super(fm, fragmentList, mTitles);
    }
}
