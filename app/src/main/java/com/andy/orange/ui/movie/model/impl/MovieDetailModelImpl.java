package com.andy.orange.ui.movie.model.impl;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.SubjectMovie;
import com.andy.orange.ui.movie.model.MovieDetailModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/22.
 */

public class MovieDetailModelImpl implements MovieDetailModel {
    @Override
    public Observable<SubjectMovie> getMovieDetailById(String id) {

        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiService.MOVIE_BASE_URL_HOT).build();

        ApiService service=retrofit.create(ApiService.class);

        Observable<SubjectMovie> subMovie=service.getMovieDetail(id);

        return subMovie;
    }
}
