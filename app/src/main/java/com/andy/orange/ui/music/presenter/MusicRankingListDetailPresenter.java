package com.andy.orange.ui.music.presenter;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface MusicRankingListDetailPresenter {

    void requestRankListDetail(String format, String from, String method, int type, int offset, int
            size, String fields);

    void requestRankSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat,
                               String musicUrlMethodSongDetail, String song_id);
}
