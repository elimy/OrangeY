package com.andy.orange.ui.music.fragment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicMeAdapter;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.bean.MusicMeItem;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MeFragment extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.IRecycler_music_me)
    IRecyclerView mRecyclerView;

    @BindView(R.id.broadcast_imageView)
    ImageView imageView;

    private Context mCxt;
    private MusicMeAdapter musicMeAdapter;
    private List<MusicMeItem> mList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_music_me;
    }

    @Override
    protected void initView() {
        mCxt=getActivity();
        initData();
        setRecyclerView();
    }

    private void initData() {
         mList=new ArrayList<>();
        MusicMeItem item=new MusicMeItem();
        item.setTitle("本地音乐");
        item.setCount(0);
        item.setImageRes(getResources().getDrawable(R.drawable.lcoal_music));
        mList.add(item);

        MusicMeItem item1=new MusicMeItem();
        item1.setTitle("最近播放");
        item1.setCount(0);
        item1.setImageRes(getResources().getDrawable(R.drawable.play_record));
        mList.add(item1);

        MusicMeItem item2=new MusicMeItem();
        item2.setTitle("下载管理");
        item2.setCount(0);
        item2.setImageRes(getResources().getDrawable(R.drawable.download_manage));
        mList.add(item2);

        MusicMeItem item3=new MusicMeItem();
        item3.setTitle("我的歌手");
        item3.setCount(0);
        item3.setImageRes(getResources().getDrawable(R.drawable.my_singer));
        mList.add(item3);

        MusicMeItem item4=new MusicMeItem();
        item4.setTitle("我的喜欢");
        item4.setCount(0);
        item4.setImageRes(getResources().getDrawable(R.drawable.my_like));
        mList.add(item4);
    }

    private void setRecyclerView(){

        musicMeAdapter=new MusicMeAdapter(mCxt,mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mCxt));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setIAdapter(musicMeAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mCxt,DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onRefresh() {

    }
}
