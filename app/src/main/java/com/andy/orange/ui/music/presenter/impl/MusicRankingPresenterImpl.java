package com.andy.orange.ui.music.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.RankingListItem;
import com.andy.orange.ui.music.model.MusicModel;
import com.andy.orange.ui.music.model.impl.MusicModelImpl;
import com.andy.orange.ui.music.presenter.MusicRankingPresenter;
import com.andy.orange.ui.music.view.MusicRankingListView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy lau on 2017/8/25.
 */

public class MusicRankingPresenterImpl implements MusicRankingPresenter {

    private Context mContext;
    private MusicRankingListView mRankingView;
    private MusicModel musicModel;

    public MusicRankingPresenterImpl(Context context, MusicRankingListView rankingView) {

        this.mContext=context;
        this.mRankingView=rankingView;
        musicModel=new MusicModelImpl();
    }

    @Override
    public void loadRankingList(String format, String from, String method, int kFlag) {

        Log.d("RankingPresenterImpl",format+format+method+kFlag);

        Observable<RankingListItem> observable=musicModel.loadRankingList(format,from,method,kFlag);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankingListItem>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RankingListItem contentBeanXes) {

                Log.d("onNext",contentBeanXes.getContent().get(1).getName()+"");

                mRankingView.returnRankingListInfos(contentBeanXes);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

                Log.d("onComplete","onComplete");
            }
        });

    }
}
