package com.andy.orange.ui.movie.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.GenresMovie;
import com.andy.orange.ui.movie.MovieFragment;
import com.andy.orange.ui.movie.model.MovieModel;
import com.andy.orange.ui.movie.model.impl.MovieModelImpl;
import com.andy.orange.ui.movie.presenter.MoviePresenter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MoviePresenterImpl implements MoviePresenter {

    private final MovieFragment mMovieView;
    private MovieModel movieModel;
    private final Activity mContext;
    private final static String TAG="Andy";

    public MoviePresenterImpl(MovieFragment view, Activity context) {
        this.mMovieView = view;
        this.mContext = context;
        this.movieModel = new MovieModelImpl();
    }


    @Override
    public void getMoviesByType(int pageSize, int startPage,String genres) {

        Log.d(TAG,"MoviePresenterImpl class->getMoviesByType()");

        Observable<GenresMovie> observable=movieModel.getMoviesByGenres(genres,startPage*pageSize,pageSize);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GenresMovie>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GenresMovie genresMovies) {

                        mMovieView.getGenresMovie(genresMovies);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG,"MoviePresenterImpl class->onComplete()");


                    }
                });
    }
}
