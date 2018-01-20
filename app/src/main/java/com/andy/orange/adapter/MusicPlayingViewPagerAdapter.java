package com.andy.orange.adapter;

import android.support.v4.app.FragmentManager;

import com.andy.orange.base.BaseFragment;
import com.andy.orange.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MusicPlayingViewPagerAdapter extends BaseFragmentAdapter {

    public MusicPlayingViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm, fragmentList);
    }
}
