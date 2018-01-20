package com.andy.orange.ui.music.model;

import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface MusicSongListDetailModel {

    Observable<SongListDetail> requestSongListDetail(String format, String from, String method, String listId);

    Observable<SongDetail> requestSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat,
                                             String musicUrlMethodSongDetail, String songId);
}
