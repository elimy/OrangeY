package com.andy.orange.ui.movie.model;

import com.andy.orange.bean.SubjectMovie;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/8/22.
 */

public interface MovieDetailModel {
    Observable<SubjectMovie> getMovieDetailById(String id);
}
