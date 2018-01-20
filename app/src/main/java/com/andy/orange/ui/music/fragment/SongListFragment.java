package com.andy.orange.ui.music.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicSongListAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.bean.SongListInfo;
import com.andy.orange.ui.music.presenter.impl.MusicPresenterImpl;
import com.andy.orange.ui.music.view.MusicSongListView;
import com.andy.orange.widget.LoadMoreFooterView;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/24.
 */

public class SongListFragment extends BaseFragment implements MusicSongListView,OnLoadMoreListener{

    public static final  String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_FORMAT = "json";
    public static final String MUSIC_URL_METHOD_GEDAN ="baidu.ting.diy.gedan";

    @BindView(R.id.linear_selector)
    LinearLayout mLinearLayout;

    @BindView(R.id.ire_music_list)
    IRecyclerView mRecyclerView;

    private Context mCxt;
    private MusicPresenterImpl musicPresenter;
    private MusicSongListAdapter musicSongListAdapter;
    private List<SongListInfo.ContentBean> mList=new ArrayList<>();
    private LoadMoreFooterView moreFooterView;
    private static int pageSize=16;
    private static int startPage=1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_song_list;
    }

    @Override
    public void loadSongListInfos(SongListInfo songLists) {

        if (null!=songLists) {
            if (songLists.getContent().size() != 0) {
                if (startPage == 1) {

                    mList.clear();
                    mList.addAll(songLists.getContent());
                    Log.d("初次加载数据大小:", mList.size() + "");
                    musicSongListAdapter.notifyDataSetChanged();
                } else {
                    mList.addAll(songLists.getContent());

                    Log.d("加载更多数据:", mList.size() + "");
                    musicSongListAdapter.notifyDataSetChanged();
                }
            } else {
                Log.d("数据集中数据条目：", songLists.getContent().size() + "");
                startPage--;
                moreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
            }
        }
    }

    @Override
    protected void initView() {

        Log.d("SongListFragment","initView");

        mCxt=getActivity();
        musicPresenter=new MusicPresenterImpl(getActivity(),this);

        //获取歌单
        musicPresenter.requestSongListAll(MUSIC_URL_FORMAT,MUSIC_URL_FROM,MUSIC_URL_METHOD_GEDAN,pageSize,startPage);


        musicSongListAdapter=new MusicSongListAdapter(mCxt,mList);

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setItemPrefetchEnabled(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setIAdapter(new ScaleInAnimationAdapter(musicSongListAdapter));
        moreFooterView= (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
    }

    @Override
    public void onLoadMore() {
        ++startPage;
        Log.d("当前加载页面",startPage+"");

        musicPresenter.requestSongListAll(MUSIC_URL_FORMAT,MUSIC_URL_FROM,MUSIC_URL_METHOD_GEDAN,pageSize,startPage);
        moreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
    }
}
