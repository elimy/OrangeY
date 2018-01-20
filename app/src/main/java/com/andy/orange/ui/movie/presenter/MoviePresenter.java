package com.andy.orange.ui.movie.presenter;

/**
 * Created by Administrator on 2017/8/17.
 */

public interface MoviePresenter {
    void getMoviesByType(int pageSize,int startPage,String genres);
}
