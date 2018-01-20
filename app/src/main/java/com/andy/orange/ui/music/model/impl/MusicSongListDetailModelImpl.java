package com.andy.orange.ui.music.model.impl;

import android.util.Log;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;
import com.andy.orange.ui.music.model.MusicSongListDetailModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MusicSongListDetailModelImpl implements MusicSongListDetailModel {


    /*
    * 获取歌单List的detail
    * */
    @Override
    public Observable<SongListDetail> requestSongListDetail(String format, String from, String method, String listId) {



        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL)
                .build();

        ApiService service=retrofit.create(ApiService.class);

        Observable<SongListDetail> details=service.getSongListDetail(format,from,method,listId);

        Log.d("DetailModelImpl","requestSongListDetail");
        Log.d("details",details.toString());


        return details;
    }

    /*
    * 获取单曲的detail
    * */
    @Override
    public Observable<SongDetail> requestSongDetail(String musicUrlFrom2, String musicUrlVersion,
                                                    String musicUrlFormat, String musicUrlMethodSongDetail, String songId) {
        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL)
                .build();

        ApiService service=retrofit.create(ApiService.class);
        Observable<SongDetail> detailObs=service.getSongDetail(musicUrlFrom2,musicUrlVersion,musicUrlFormat,musicUrlMethodSongDetail,songId);

        return detailObs;
    }
}
