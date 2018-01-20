package com.andy.orange.ui.music.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;
import com.andy.orange.ui.music.MusicSongListDetailActivity;
import com.andy.orange.ui.music.model.MusicSongListDetailModel;
import com.andy.orange.ui.music.model.impl.MusicSongListDetailModelImpl;
import com.andy.orange.ui.music.presenter.MusicSongListDetailPresenter;
import com.andy.orange.ui.music.view.MusicSongListDetailView;
import com.andy.orange.ui.music.view.MusicSongListView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public class MusicSongListDetailPresenterImpl implements MusicSongListDetailPresenter {

    private Context mContext;
    private MusicSongListDetailView mSongDetailListView;
    private MusicSongListDetailModel detailModel;

    public MusicSongListDetailPresenterImpl(Context mContext, MusicSongListDetailView songListDetailView) {

        this.mContext=mContext;
        this.mSongDetailListView=songListDetailView;
        this.detailModel=new MusicSongListDetailModelImpl();
    }

    /*
    * 获取整个List detail
    * */
    @Override
    public void requestSongListDetail(String format, String from, String method, String listId) {

        Log.d("DetailPresenterImpl","requestSongListDetail");

        Observable<SongListDetail> observable=detailModel.requestSongListDetail(format,from,method,listId);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SongListDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongListDetail detail) {

                        mSongDetailListView.returnMusicListDetail(detail);
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
    * 获取单个song detail
    * */
    @Override
    public void requestSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat,
                                  String musicUrlMethodSongDetail, String songId) {

        Observable<SongDetail> observable=detailModel.requestSongDetail(musicUrlFrom2,musicUrlVersion,
                musicUrlFormat,musicUrlMethodSongDetail,songId);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SongDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SongDetail songDetail) {
                        mSongDetailListView.returnMusicDetail(songDetail);
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
