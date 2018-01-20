package com.andy.orange.ui.music.view;

import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongDetail;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface MusicRankingListDetailView {

    void returnRankingListDetails(RankingListDetail rankingListDetail);

    void returnRankingSongDetail(SongDetail songDetail);
}
