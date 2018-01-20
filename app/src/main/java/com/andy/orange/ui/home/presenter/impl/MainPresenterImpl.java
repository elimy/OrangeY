package com.andy.orange.ui.home.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.Film;
import com.andy.orange.ui.home.model.MainModel;
import com.andy.orange.ui.home.model.impl.MainModelImpl;
import com.andy.orange.ui.home.presenter.MainPresenter;
import com.andy.orange.ui.home.view.MainView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private MainModel mainModel;
    private Context cxt;
    private static final String TAG ="Andy";
    private Film mFilms;

    /*
    * 带参构造方法
    * */
    public MainPresenterImpl(Context cxt,MainView mainView){
        this.mainView=mainView;
        this.cxt=cxt;
        Log.d(TAG,"MainPresenterImpl class->MainPresenterImpl()");
        mainModel=new MainModelImpl();

    }

    /*
    * 请求获取热门电影
    * */
    @Override
    public void requestHotFilm(int start,int count) {

        Log.d(TAG,"MainPresenterImpl class->requestHotFilm()");

        Observable<Film> observable=mainModel.loadHotFilm(start,count);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Film>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Film films) {
                        mFilms=films;
                        //mainView.getFilmInfos(mFilms);
                        Log.d(TAG,"MainPresenterImpl class->onNext()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"MainPresenterImpl class->onComplete()");

                        mainView.getFilmInfos(mFilms);
                    }
                });

    }


}
