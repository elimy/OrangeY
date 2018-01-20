package com.andy.orange.ui.movie.model.impl;

import android.util.Log;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.Film;
import com.andy.orange.bean.GenresMovie;
import com.andy.orange.ui.movie.model.MovieModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MovieModelImpl implements MovieModel {
    private static final String TAG="Andy";

    @Override
    public Observable<GenresMovie> getMoviesByGenres(String tag, int start, int count) {

        Retrofit retrofit =
                new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(ApiService.MOVIE_BASE_URL_HOT).build();

        ApiService apiService = retrofit.create(ApiService.class);

        Observable<GenresMovie> genresMovies = apiService.getMoviesByGenres(tag,start,count);

        Log.d(TAG,"MovieModelImpl class->getMoviesByGenres()");
        Log.d(TAG,"MovieModelImpl: movieObservable"+genresMovies.toString());

        return genresMovies;
    }
}
