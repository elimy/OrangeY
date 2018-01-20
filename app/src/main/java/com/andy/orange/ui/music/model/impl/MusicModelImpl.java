package com.andy.orange.ui.music.model.impl;

import com.andy.orange.api.ApiService;
import com.andy.orange.bean.RankingListItem;
import com.andy.orange.bean.SongListInfo;
import com.andy.orange.ui.music.model.MusicModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MusicModelImpl implements MusicModel {


    /*
    * 获取歌单
    * */
    @Override
    public Observable<SongListInfo> loadSongListAll(String format, String from, String method, int page_size, int page_no) {

        Retrofit retrofit= new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL)
                .build();

        ApiService service=retrofit.create(ApiService.class);

        Observable<SongListInfo> infos=service.getSongListAll(format,from,method,page_size,page_no);

        return infos;
    }


    /*
    * 获取排行榜
    * */
    @Override
    public Observable<RankingListItem> loadRankingList(String format, String from, String method, int kFlag) {

        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiService.MUSIC_BASE_URL).build();

        ApiService service=retrofit.create(ApiService.class);

        Observable<RankingListItem> listObservable=service.getRankingListAll(format,from,method,kFlag);
        return listObservable;
    }
}
