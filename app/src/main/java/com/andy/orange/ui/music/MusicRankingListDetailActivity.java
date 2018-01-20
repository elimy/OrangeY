package com.andy.orange.ui.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicRankingListDetailAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.base.BaseActivityWithoutStatus;
import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.service.MediaPlayService;
import com.andy.orange.service.MusicPlayService;
import com.andy.orange.ui.music.presenter.impl.MusicRankingListDetailPresenterImpl;
import com.andy.orange.ui.music.view.MusicRankingListDetailView;
import com.andy.orange.utils.ImageLoaderUtils;
import com.aspsine.irecyclerview.IRecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;

/**
 * Created by Andy Lau on 2017/9/7.
 */

public class MusicRankingListDetailActivity extends BaseActivityWithoutStatus implements
        MusicRankingListDetailView, MusicRankingListDetailAdapter.onItemClickListener, MusicRankingListDetailAdapter.onPlayAllClickListener {

    @BindView(R.id.ranking_detail_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.ranking_detail_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.ranking_detail_poster)
    ImageView rankingPoster;

    @BindView(R.id.ranking_detail_name)
    TextView rankingName;

    @BindView(R.id.ranking_detail_toobar)
    Toolbar rankingToolbar;

    @BindView(R.id.ranking_detail_toobar_rel)
    RelativeLayout rankingRelativeLayout;

    @BindView(R.id.ranking_detail_recycler)
    IRecyclerView rankingRecyclerView;

    @BindView(R.id.ranking_detail_bottom_container)
    FrameLayout rankingBottomContainer;
    private MusicRankingListDetailPresenterImpl mPresenter;
    private int mType;
    private String mFields;
    private MediaServiceConnection mConnection;
    private static MusicPlayService.MusicBinder mMediaBinder;
    private Intent mIntent;
    private List<RankingListDetail.SongListBean> mList = new ArrayList<>();
    private MusicRankingListDetailAdapter mRankingDetailAdapter;
    public static final String MUSIC_URL_FORMAT = "json";
    public static final String MUSIC_URL_FROM_2 = "android";
    public static final String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_VERSION = "5.6.5.6";
    public static final String MUSIC_URL_METHOD_RANKING_DETAIL = "baidu.ting.billboard.billList";
    public static final String MUSIC_URL_METHOD_SONG_DETAIL = "baidu.ting.song.play";

    private MusicPlayService mService;
    private boolean isLocal=false;
    private HashMap<String, Integer> PosMap = new HashMap<>();
    private SongDetail[] mDetails;
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public int getLayoutId() {
        return R.layout.activity_rankinglist_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MusicRankingListDetailPresenterImpl(this, this);
    }

    @Override
    public void initView() {
        //StatusBarSetting.setTranslucent(this);

        mContext = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mType = (int) bundle.get("type");
        }

        //设置actionbar
        setSupportActionBar(rankingToolbar);

        //设置返回按钮点击监听
        rankingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String encodeStr = "song_id,title,author,album_title,pic_big,pic_small,havehigh,all_rate,charge," +
                "has_mv_mobile,learn,song_source,korean_bb_song";
        try {
            encodeStr = URLEncoder.encode("song_id,title,author,album_title,pic_big,pic_small,havehigh," +
                    "all_rate,charge,has_mv_mobile,learn,song_source,korean_bb_song", "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mFields = encodeStr;

        setRecyclerView();

        //绑定播放服务
        mConnection = new MusicRankingListDetailActivity.MediaServiceConnection();
        if (mIntent == null) {
            mIntent = new Intent(this, MusicPlayService.class);
            startService(mIntent);
            bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        }
    }

    /*
    * 设置IRecyclerView
    * */
    private void setRecyclerView() {
        mRankingDetailAdapter = new MusicRankingListDetailAdapter(mContext, mList);
        mRankingDetailAdapter.setOnItemClickListener(this);
        mRankingDetailAdapter.setOnPlayAllClickListener(this);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        rankingRecyclerView.setItemAnimator(new LandingAnimator());
        rankingRecyclerView.setIAdapter(new ScaleInAnimationAdapter(mRankingDetailAdapter));
        rankingRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //LoadingDialog.showDialogForLoading(this).show();
        mPresenter.requestRankListDetail(MUSIC_URL_FORMAT, MUSIC_URL_FROM, MUSIC_URL_METHOD_RANKING_DETAIL, mType, 0, 100, mFields);
    }

    /*
    * presenter层返回获取的榜单详情
    * */
    @Override
    public void returnRankingListDetails(RankingListDetail rankingListDetail) {
        List<RankingListDetail.SongListBean> list = rankingListDetail.getSong_list();
        setUI(rankingListDetail.getBillboard());
        mList.addAll(list);

        //初始化数据集合
        mRankingDetailAdapter.notifyDataSetChanged();

        //初始化mDetails的大小为list的长度
        mDetails = new SongDetail[mList.size()];
        //初始化歌曲的list
        initSongList();
    }

    /*
    * 获取保存榜单中所有歌曲的详细信息并将歌曲添加到播放服务的列表中
    * */
    private void initSongList() {

        for (int i = 0; i < mList.size(); i++) {
            RankingListDetail.SongListBean songDetail = mList.get(i);
            String song_id = songDetail.getSong_id();
            PosMap.put(song_id, i);

            mPresenter.requestRankSongDetail(MUSIC_URL_FROM_2, MUSIC_URL_VERSION, MUSIC_URL_FORMAT,
                    MUSIC_URL_METHOD_SONG_DETAIL, song_id);
        }
    }

    /*
    * presenter层返回单首歌曲的信息
    * */
    @Override
    public void returnRankingSongDetail(SongDetail songDetail) {

        if (mMediaBinder != null) {
            if (mService == null) {
                mService = mMediaBinder.getMusicPlayService();
            }

            if (null == songDetail.getSonginfo()) {

                Log.d("songDetail", "song info is null");
            } else {
                //将歌曲细节放入SongDetail数组中
                String song_id = songDetail.getSonginfo().getSong_id();
                Integer pos = PosMap.get(song_id);
                mDetails[pos] = songDetail;
            }

            //将获取到的歌曲信息放到播放列表中
            int currentNumber = index.addAndGet(1);
            if (currentNumber == mDetails.length) {
                for (int i = 0; i < mDetails.length; i++) {
                    if (i == 0) {
                        mService.clearPlayList();
                    }
                    mService.setOneInPlayList(mDetails[i]);
                }

                // LoadingDialog.cancelDialogForLoading();
            }
        }
    }


    /*
    * 设置榜单UI
    * */
    private void setUI(RankingListDetail.BillboardBean billboard) {

        rankingName.setText(billboard.getName());
        ImageLoaderUtils.display(this, rankingPoster, billboard.getPic_s260());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    /*
    * 单曲点击事件
    * */
    @Override
    public void onItemClick(int position) {

        if (mService != null) {
            mService.playOne(position, isLocal);
        }

    }

    /*
    * 全部播放点击事件
    * */
    @Override
    public void onHeaderItemClick(List<RankingListDetail.SongListBean> details) {

        if (mService != null) {
            mService.playAll(isLocal, mContext);
        }
    }


    /*
    * 在Activity中，通过ServiceConnection接口来取得建立连接与连接意外丢失的回调
    * 作用在于在activity中获取到binder的实例，再通过binder实例的getService()方法获取获取
    * service的实例从而对service进行操作
    * */
    private static class MediaServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMediaBinder = (MusicPlayService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
