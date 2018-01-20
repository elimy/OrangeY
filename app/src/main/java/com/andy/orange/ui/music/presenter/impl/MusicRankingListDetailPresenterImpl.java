package com.andy.orange.ui.music.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.ui.music.model.MusicRankingListDetailModel;
import com.andy.orange.ui.music.model.impl.MusicRankingListDetailModelImpl;
import com.andy.orange.ui.music.presenter.MusicRankingListDetailPresenter;
import com.andy.orange.ui.music.view.MusicRankingListDetailView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MusicRankingListDetailPresenterImpl implements MusicRankingListDetailPresenter {

    private MusicRankingListDetailView rankingListDetailView;
    private MusicRankingListDetailModel rankingListDetailModel;
    private Context context;
    private SongDetail detail;

    public MusicRankingListDetailPresenterImpl(Context context, MusicRankingListDetailView detailView) {

        this.context = context;
        rankingListDetailView = detailView;
        rankingListDetailModel = new MusicRankingListDetailModelImpl();
    }

    /*
    * 获取排行榜详情
    * */
    @Override
    public void requestRankListDetail(String format, String from, String method, int type, int offset, int size, String fields) {

        Observable<RankingListDetail> detailObservable = rankingListDetailModel.requestRankListDetail(
                format, from, method, type, offset, size, fields);

        detailObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankingListDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RankingListDetail rankingListDetail) {

                        rankingListDetailView.returnRankingListDetails(rankingListDetail);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /*
    * 获取排行榜详情中单首歌曲的详情
    * */
    @Override
    public void requestRankSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat, String musicUrlMethodSongDetail, String song_id) {

        Observable<SongDetail> observable=rankingListDetailModel.requestRankSongDetail(musicUrlFrom2,musicUrlVersion,musicUrlFormat,musicUrlMethodSongDetail,song_id);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SongDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongDetail songDetail) {

                        Log.d("RankSongDetail onNext",songDetail.getSonginfo().getTitle());
                        detail=songDetail;
                        rankingListDetailView.returnRankingSongDetail(detail);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
