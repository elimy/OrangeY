package com.andy.orange.ui.movie.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.andy.orange.bean.SubjectMovie;
import com.andy.orange.ui.movie.model.MovieDetailModel;
import com.andy.orange.ui.movie.model.impl.MovieDetailModelImpl;
import com.andy.orange.ui.movie.presenter.MovieDetailPresenter;
import com.andy.orange.ui.movie.view.MovieDetailView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/22.
 */

public class MovieDetailPresenterImpl implements MovieDetailPresenter {

    private Context mCxt;
    private MovieDetailView movieDetailView;
    private MovieDetailModel detailModel;


    public MovieDetailPresenterImpl(Context context, MovieDetailView view){
        this.mCxt=context;
        this.movieDetailView=view;
        detailModel=new MovieDetailModelImpl();
    }

    @Override
    public void getMovieDetailById(String id) {

        Observable<SubjectMovie> subMovie=detailModel.getMovieDetailById(id);
        subMovie.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubjectMovie>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SubjectMovie subjectMovie) {

                        movieDetailView.LoadMovieDetailById(subjectMovie);
                        Log.d("onNext",subjectMovie.getTitle());
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
