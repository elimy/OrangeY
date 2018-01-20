package com.andy.orange.ui.music.presenter;

import com.andy.orange.bean.RankingListItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/8/25.
 */

public interface MusicRankingPresenter {

    void loadRankingList(String format,String from,String method,int kFlag);
}
