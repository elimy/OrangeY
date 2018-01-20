package com.andy.orange.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.ImageView;

import com.andy.orange.R;

/**
 * Created by Administrator on 2017/8/29.
 */

public class MediaServiceConnection implements ServiceConnection {

    private MusicPlayService.MusicBinder mediaBinder;
    public MusicPlayService musicPlayService;
    private ImageView mImageView;

    /*
    * 无参构造方法
    * */
    public MediaServiceConnection() {
    }

    /*
    * 带参构造方法
    * */
    public MediaServiceConnection(ImageView imageView) {
        this.mImageView = imageView;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        //通过ibinder去绑定媒体播放service
        mediaBinder = (MusicPlayService.MusicBinder) service;
        musicPlayService = mediaBinder.getMusicPlayService();

        //根据播放情况设置显示特定的icon
        if (musicPlayService.isPlaying) {
            //正在播放
            mImageView.setImageResource(R.drawable.play_rdi_btn_pause);

        } else {
            //正在暂停
            mImageView.setImageResource(R.drawable.play_rdi_btn_play);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
