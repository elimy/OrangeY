package com.andy.orange.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.andy.orange.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList=new ArrayList<>();
    List<String> mTitles=new ArrayList<>();

    public BaseFragmentAdapter(FragmentManager fm,List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    public BaseFragmentAdapter(FragmentManager fm,List<BaseFragment> fragmentList,List<String> mTitles) {
        super(fm);
        this.fragmentList=fragmentList;
        this.mTitles=mTitles;
    }

    /*
    * 获取fragment对应的标题
    * */
    @Override
    public CharSequence getPageTitle(int position) {
        return !CollectionUtils.isNullOrEmpty(mTitles)?mTitles.get(position):"";

    }

    /*
        * 获取指定位置的fragment
        * */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /*
    * 获取fragment数量
    * */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /*
    * 刷新fragment
    * */
    public void setFragments(FragmentManager fm,List<BaseFragment> fragments,List<String> mTitles){
        this.mTitles=mTitles;
        if (this.fragmentList!=null){
            FragmentTransaction ft=fm.beginTransaction();
            for (Fragment f:fragmentList){
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            ft=null;
            fm.executePendingTransactions();
        }
        fragmentList=fragments;
        notifyDataSetChanged();
    }
}
