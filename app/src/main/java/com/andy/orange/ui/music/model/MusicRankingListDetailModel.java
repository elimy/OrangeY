package com.andy.orange.ui.music.model;

import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongDetail;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface MusicRankingListDetailModel {
    Observable<RankingListDetail> requestRankListDetail(String format, String from, String method, int type, int offset, int
            size, String fields);

    Observable<SongDetail> requestRankSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat, String musicUrlMethodSongDetail, String song_id);
}
