package com.andy.orange.ui.home.model.impl;


import android.util.Log;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.Film;
import com.andy.orange.ui.home.model.MainModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy Lau on 2017/4/24.
 */

public class MainModelImpl implements MainModel {

    protected final static String TAG="Andy";

    @Override
    public Observable<Film> loadHotFilm(int start,int count) {

        Retrofit retrofit =
                new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(ApiService.MOVIE_BASE_URL_HOT).build();

        ApiService apiService = retrofit.create(ApiService.class);

        Observable<Film> hotFilm = apiService.getHotFilm(start,count);

        Log.d(TAG,"MainModelImpl class->loadHotFilm()");
        Log.d(TAG,"MainModelImpl: hotFilm"+hotFilm.toString());
        return hotFilm;
    }
}
