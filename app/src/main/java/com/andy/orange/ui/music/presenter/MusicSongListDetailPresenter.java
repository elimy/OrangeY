package com.andy.orange.ui.music.presenter;

/**
 * Created by Administrator on 2017/8/28.
 */

public interface MusicSongListDetailPresenter {
    void requestSongListDetail(String format,String from,String method, String listid);

    void requestSongDetail(String musicUrlFrom2, String musicUrlVersion, String musicUrlFormat,
                           String musicUrlMethodSongDetail, String songId);
}
