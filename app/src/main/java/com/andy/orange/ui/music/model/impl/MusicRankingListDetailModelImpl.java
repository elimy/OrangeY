package com.andy.orange.ui.music.model.impl;

import android.util.Log;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.ui.music.model.MusicRankingListDetailModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy Lau on 2017/9/7.
 */

public class MusicRankingListDetailModelImpl implements MusicRankingListDetailModel {


    /*
    * 获取榜单信息
    * */
    @Override
    public Observable<RankingListDetail> requestRankListDetail(String format, String from, String
            method, int type, int offset, int size, String fields) {

        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL)
                .build();

        ApiService service=retrofit.create(ApiService.class);

        Observable<RankingListDetail> detailObservable=service.getRankingListDetail(format,from,method,type,offset,size,fields);

        return detailObservable;
    }

    /*
    * 获取榜单单首歌的信息
    * */
    @Override
    public Observable<SongDetail> requestRankSongDetail(String musicUrlFrom2, String musicUrlVersion,
                                                        String musicUrlFormat, String musicUrlMethodSongDetail, String song_id) {

        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL)
                .build();

        ApiService service=retrofit.create(ApiService.class);
        Observable<SongDetail> details=service.getSongDetail(musicUrlFrom2,musicUrlVersion,
                musicUrlFormat,musicUrlMethodSongDetail,song_id);

        return details;
    }
}
