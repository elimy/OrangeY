package com.andy.orange.ui.music.fragment;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicRankingAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.bean.RankingListItem;
import com.andy.orange.ui.music.presenter.MusicRankingPresenter;
import com.andy.orange.ui.music.presenter.impl.MusicRankingPresenterImpl;
import com.andy.orange.ui.music.view.MusicRankingListView;
import com.aspsine.irecyclerview.IRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/24.
 */

public class RankingFragment extends BaseFragment implements MusicRankingListView {

    @BindView(R.id.music_ranking_ire)
    IRecyclerView mRecyclerView;

    public static final  String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_FORMAT = "json";
    public static final String MUSIC_URL_METHOD_RANKINGLIST ="baidu.ting.billboard.billCategory";

    private Context mContext;
    private MusicRankingAdapter rankingAdapter;
    private MusicRankingPresenter rankingPresenter;
    private List<RankingListItem.ContentBeanX> mList=new ArrayList<>();


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_music_rank;
    }

    @Override
    protected void initView() {

        Log.d("initView","initView");

        mContext=getActivity();
        rankingPresenter=new MusicRankingPresenterImpl(getActivity(),this);
        rankingAdapter=new MusicRankingAdapter(mContext,mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setIAdapter(new ScaleInAnimationAdapter(rankingAdapter));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));

        rankingPresenter.loadRankingList(MUSIC_URL_FORMAT,MUSIC_URL_FROM,MUSIC_URL_METHOD_RANKINGLIST,1);
    }

    @Override
    public void returnRankingListInfos(RankingListItem rangkingListInfo) {


        Log.d("returnRankingListInfos",rangkingListInfo.getContent().get(1).getName());
        Log.d("returnRankingListInfos",rangkingListInfo.getContent().get(1).getType()+"");

        //mList.addAll(rangkingListInfo);
        mList.addAll(rangkingListInfo.getContent());

        rankingAdapter.notifyDataSetChanged();
    }
}
