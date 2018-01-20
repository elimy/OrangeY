package com.andy.orange.ui.home.model;

import com.andy.orange.bean.Film;

import io.reactivex.Observable;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public interface MainModel {
    Observable<Film> loadHotFilm(int start,int count);
}
