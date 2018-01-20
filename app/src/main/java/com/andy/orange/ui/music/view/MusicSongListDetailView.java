package com.andy.orange.ui.music.view;

import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;
import com.andy.orange.bean.SongListInfo;

/**
 * Created by Andy lau on 2017/8/28.
 */

public interface MusicSongListDetailView {

    void returnMusicListDetail(SongListDetail details);

    void returnMusicDetail(SongDetail detail);
}
