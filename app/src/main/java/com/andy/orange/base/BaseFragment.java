package com.andy.orange.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    private Unbinder mUnbinder;
    //标识当前Fragment是否处于可见状态
    private boolean isFragmentVisible;
    //是否第一次开启网络加载
    public boolean isFirst;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null){
           rootView=inflater.inflate(getLayoutResource(),container,false);
           mUnbinder= ButterKnife.bind(this,rootView);
            initView();
            if (isFragmentVisible&&!isFirst){
                onFragmentVisibleChange(true);
            }
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            isFragmentVisible=true;
        }

        if (rootView==null){
            return;
        }

        //可见并且没有加载过
        if (!isFirst&&isFragmentVisible){
            onFragmentVisibleChange(true);
            return;
        }

        //已经加载过，从可见到不可见
        if (isFragmentVisible){
            onFragmentVisibleChange(false);
            isFragmentVisible=false;
        }

    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作.
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    protected abstract int getLayoutResource();

    protected abstract  void initView();


    /*
    * 不含bundle的页面跳转
    * */
    public void startActivity(Class<?> cls){
        startActivity(cls,null);
    }

    /*
    * 包含bundle的class页面跳转
    * */
    public void startActivity(Class<?> cls,Bundle bundle){

        Intent intent=new Intent();
        intent.setClass(getActivity(),cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }

        startActivity(intent);
    }

    /*
    * 含有bundle有返回值的class页面跳转
    * */
    public void startActivityForResult(Class<?> cls,Bundle bundle,int requestCode){

        Intent intent=new Intent(getActivity(),cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }

        startActivityForResult(intent,requestCode);
    }

    /*
    * 带返回值无bundle数据的class跳转
    * */
    public void startActivityForResult(Class<?>cls,int requestCode){
        startActivityForResult(cls,null,requestCode);
    }

    /*
    * fragment销毁时解绑butterknife
    * */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //解绑ButterKnife
        //mUnbinder.unbind();
    }
}
