package com.andy.orange.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.andy.orange.app.AppApplication;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongPlayingInfo;
import com.andy.orange.client.NetworkUtil;
import com.andy.orange.ui.music.PlayingActivity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * Created by Andy Lau on 2017/8/29.
 */

public class MediaPlayService extends Service {

    private List<SongDetail> mList = new ArrayList<>();

    private SongDetail mSongInfo;
    //MediaPlayer
    private MediaPlayer mediaPlayer;
    //播放时间
    private int CurrentTime = 0;
    //播放歌曲在列表中的索引
    private int position = 0;
    //当前歌曲的时长
    private int duration = 0;
    private AudioManager audioManager;
    private boolean isPlayAll = false;
    private boolean flag = true;

    //创建单线程池
    private ExecutorService es = Executors.newSingleThreadExecutor();
    //播放状态，默认是列表循环
    private int play_mode = 0;
    private int currentTime = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = getMediaPlayer(AppApplication.getAppContext());

        //音频播放管理器
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    /*
    * 获取媒体播放器
    * */
    public MediaPlayer getMediaPlayer(Context context){

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Constructor<?> constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                f.setAccessible(false);
            }
            Method setSubtitleAnchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
            setSubtitleAnchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {

        }

        return mediaplayer;
    }

    /*
    * 在服务被绑定的时候实例化一个MediaBinder，方便后面activity回调是通过getMediaPlayService()获取service
    * 实例
    * */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MediaBinder();
    }

    public class MediaBinder extends Binder {

        public MediaPlayService getMediaPlayService() {
            return MediaPlayService.this;
        }
    }

    //创建线程 动态更新歌曲播放进度
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {

            //获取到播放的进度
            int position = mediaPlayer.getCurrentPosition();

            //发送广播 通知PlayActivity界面更新UI
            Intent intent = new Intent();
            intent.putExtra("max", duration);
            intent.putExtra("progress", position);
            intent.setAction("com.andy.progress");

            //发送一个更新进度的广播
            sendBroadcast(intent);

            SystemClock.sleep(1000);

            if (position<duration&&isPlaying()) {
                progressRunnable.run();
            }else {
                Log.d("progressRunnable","停止播放");
            }
        }
    };

    /*
    * 全部播放
    * */
    public void playAll(Boolean isLocal, Context mContext) {
        setPlayAll(true);
        SongDetail detail = mList.get(0);
        if (mSongInfo == null || !detail.getSonginfo().getSong_id().equals(mSongInfo.getSonginfo().getSong_id())) {

            //歌曲不匹配
            mSongInfo = detail;
        }

        if (isPlayAll) {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mSongInfo.setOnClick(true);
            playForAll(mSongInfo.getBitrate().getFile_link(), isLocal, mContext);
        }
    }

    /*
    * 根据音乐链接播放全部
    * */
    private void playForAll(String musicUrl, Boolean isLocal, Context mContext) {
        //给予无网络提示
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            if (!isLocal) {
                Toast.makeText(AppApplication.getAppContext(), "没有网络了哟，请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        }
        if (null == mediaPlayer) return;
        if (requestFocus()) {
            try {
                currentTime = 0;
                mediaPlayer.setDataSource(musicUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(completionListener);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        currentTime = mp.getCurrentPosition();
                        duration = mp.getDuration();

                        //发送广播 通知PlayActivity界面更新UI
                        progressRunnable.run();
                        //es.execute();

                        Log.d("playForAll", "playForAll");
                        startPlayingActivity(mSongInfo);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * 启动播放页
    * */
    private void startPlayingActivity(SongDetail mSongInfo) {

        //歌曲名称
        String title = mSongInfo.getSonginfo().getTitle();
        //艺术家
        String artist = mSongInfo.getSonginfo().getAuthor();
        //封面
        String posterUrl = mSongInfo.getSonginfo().getPic_premium();
        boolean playing = isPlaying();

        Intent intent = new Intent(MediaPlayService.this, PlayingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isPlaying", playing);
        intent.putExtra("duration", duration);
        intent.putExtra("position", position);
        intent.putExtra("curPostion", currentTime);
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("posterUrl", posterUrl);
        startActivity(intent);

    }

    /*
    * 给PlayingActivity传递歌曲播放基本信息
    * */
    public List<SongPlayingInfo> getPlayingList() {
        List<SongPlayingInfo> list = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            SongDetail detail = mList.get(i);
            SongPlayingInfo info = new SongPlayingInfo();
            info.setTitle(detail.getSonginfo().getTitle());
            info.setArtist(detail.getSonginfo().getAuthor());
            info.setPosterUrl(detail.getSonginfo().getPic_premium());
            list.add(info);
        }

        return list;
    }

    private boolean requestFocus() {

        int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    /*
    * Audio焦点改变监听
    * */
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                //获得音频焦点
                case AudioManager.AUDIOFOCUS_GAIN:
                    resume();
                    break;
                //失去音频焦点、持续时间长
                case AudioManager.AUDIOFOCUS_LOSS:
                    stop();
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                    break;
                //失去音频焦点、持续时间短
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    pause();
                    break;
                //暂时失去焦点
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {

            Log.d("media", "音乐播放完毕");
            if (play_mode == 0) {
                //列表循环
                mSongInfo.setOnClick(false);
                //EventBus.getDefault().post(new UpdateViewPagerBean());
            } else if (play_mode == 2) {
                //单曲循环
                mSongInfo.setOnClick(false);
                playSong(position, false);
            } else {
                //随机播放
                mSongInfo.setOnClick(false);
                int temp = new Random().nextInt(mList.size());
                position = temp;
                playSong(position, false);
            }
        }
    };

    /*
    * 单曲播放
    * */
    public void playSong(int position, boolean isLocal) {
        this.position = position;

        SongDetail detail = mList.get(position);
        if (mSongInfo == null || !detail.getSonginfo().getSong_id().equals(mSongInfo.getSonginfo().getSong_id())) {
            //不是同一首歌
            if (mSongInfo != null) {
                mSongInfo.setOnClick(false);
            }
            mSongInfo = detail;
        }

        if (!mSongInfo.isOnClick()) {
            mSongInfo.setOnClick(true);
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            play(detail.getBitrate().getFile_link(), isLocal);
        } else {
            startPlayingActivity(mSongInfo);
        }
    }

    /*
    * 音乐播放
    * */
    private void play(String file_link, boolean isLocal) {

        if (!NetworkUtil.isNetworkAvailable(AppApplication.getAppContext())) {
            if (!isLocal) {
                Toast.makeText(AppApplication.getAppContext(), "没有网络", Toast.LENGTH_SHORT).show();
            }
        }

        if (null == mediaPlayer) return;
        if (requestFocus()) {
            currentTime = 0;
            try {
                mediaPlayer.setDataSource(file_link);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(completionListener);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        currentTime = mp.getCurrentPosition();
                        duration = mp.getDuration();

                        //发送广播 playingActivity更新UI
                        es.execute(progressRunnable);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * 设置播放模式
    * */
    public void setPlayMode(int playMode) {
        this.play_mode = playMode;
    }

    /*
    * 获取当前的播放模式
    * */
    public int getPlayMode() {
        return play_mode;
    }

    /*
    * 向播放列表添加待播放的歌曲
    * */
    public void addSongToPlayList(SongDetail songDetail) {
        mList.add(songDetail);
    }

    /*
    * 清空播放列表
    * */
    public void clearSongList() {
        mList.clear();
    }

    /*
    * 设置是否全部播放
    * */
    public void setPlayAll(boolean isPlayAll) {
        this.isPlayAll = isPlayAll;
    }

    /*
    * 获取是否全部播放
    * */
    public boolean isPlayAll() {
        return isPlayAll;
    }


    /*
    * 音乐是否正在播放
    * */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /*
    * 播放暂停
    * */
    public void pause() {
        if (null == mediaPlayer) return;
        if (mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    /*
    * 继续播放
    * */
    public void resume() {
        if (null == mediaPlayer) return;
        mediaPlayer.start();
        if (currentTime > 0) {
            mediaPlayer.seekTo(currentTime);
        }
    }

    /*
    * 拖动进度条
    * */
    public void seekTo(int pos) {
        if (null == mediaPlayer) return;
        mediaPlayer.seekTo(pos);
    }

    public void stop() {
    }

    /*
    * 播放下一首
    * */
    public void playNext(boolean isLocal) {

        //isPlaying()=false;
        mediaPlayer.stop();

        currentTime = 0;
        if (position < 0) {
            position = 0;
        }

        //如果播放列表大于0,位置向后移一位
        if (mList.size() > 0) {
            position++;

            //如果位置超出了list大小，设置从头播放
            if (position < mList.size()) {

            } else {
                position = 0;
            }
            playSong(position, isLocal);
            updatePlayingUI(mSongInfo);
        }
    }

    /*
    * 播放上一首
    * */
    public void pre(boolean isLocal) {

        mediaPlayer.stop();

        currentTime = 0;
        if (position < 0) {
            position = 0;
        }
        if (mList.size() > 0) {
            position--;
            if (position >= 0) {//大于等于0的情况
                playSong(position, isLocal);
            } else {
                position = 0;
                //mSongInfo.setOnClick(false);
                playSong(position, isLocal);
            }
        }
        updatePlayingUI(mSongInfo);
    }

    /*
    * 更新正在播放的歌曲UI
    * */
    private void updatePlayingUI(SongDetail mSongInfo) {

        flag = true;
        final SongPlayingInfo updateInfo = new SongPlayingInfo();
        updateInfo.setArtist(mSongInfo.getSonginfo().getAuthor());
        updateInfo.setTitle(mSongInfo.getSonginfo().getTitle());
        updateInfo.setPosterUrl(mSongInfo.getSonginfo().getPic_premium());
        updateInfo.setIndex(position);
        System.out.println("发送更新UI的消息，索引为：" + position);
        EventBus.getDefault().post(updateInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        //关闭线程池
        es.shutdownNow();

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
