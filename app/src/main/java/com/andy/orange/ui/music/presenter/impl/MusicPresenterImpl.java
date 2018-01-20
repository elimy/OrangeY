package com.andy.orange.ui.music.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.SongListInfo;
import com.andy.orange.ui.music.model.MusicModel;
import com.andy.orange.ui.music.model.impl.MusicModelImpl;
import com.andy.orange.ui.music.presenter.MusicPresenter;
import com.andy.orange.ui.music.view.MusicSongListView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicPresenterImpl implements MusicPresenter {

    private Context mContext;
    private MusicSongListView musicSongListView;
    private MusicModel musicModel;
    private SongListInfo infos;

    public MusicPresenterImpl(Context context, MusicSongListView songListView) {

        this.mContext=context;
        this.musicSongListView=songListView;
        this.musicModel=new MusicModelImpl();

    }

    @Override
    public void requestSongListAll(String format, String from, String method, int pageSize, int startPage) {

        Log.d("MusicPresenterImpl","requestSongListAll");


        Observable<SongListInfo> observable=musicModel.loadSongListAll(format,from,method,pageSize,startPage);

        if (null ==observable){
            Log.d("requestSongListAll","requestSongListAll observable is null");
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongListInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SongListInfo contentBeen) {

                Log.d("MusicPresenterImpl","onNext");
                infos=contentBeen;
                musicSongListView.loadSongListInfos(contentBeen);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

               // musicSongListView.loadSongListInfos(infos);

                Log.d("MusicPresenterImpl","onComplete");
            }
        });
    }
}
