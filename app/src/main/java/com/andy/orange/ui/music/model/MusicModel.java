package com.andy.orange.ui.music.model;

import com.andy.orange.bean.RankingListItem;
import com.andy.orange.bean.SongListInfo;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface MusicModel {

    Observable<SongListInfo> loadSongListAll(String format,String from,String method,int page_size,int page_no);

    Observable<RankingListItem> loadRankingList(String format, String from, String method, int kFlag);

}
