package com.andy.orange.ui.movie.model;

import com.andy.orange.bean.GenresMovie;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/8/17.
 */

public interface MovieModel {
    Observable<GenresMovie> getMoviesByGenres(String tag,int start,int count);
}
