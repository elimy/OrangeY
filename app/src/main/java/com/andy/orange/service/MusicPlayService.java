package com.andy.orange.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.duration;

/**
 * Created by Administrator on 2017/9/23.
 */

public class MusicPlayService extends Service {

    //是否在播放的标识
    public boolean isPlaying=true;

    //是否为列表全部播放的标识
    private boolean isPlayAll=false;

    //是否需要启动播放页
    private boolean START_FLAG=true;

    //播放模式标识，列表循环0，单曲1，随机2
    private int playMode=0;

    //播放的音乐列表
    private List<SongDetail> mPlayList=new ArrayList<>();

    //媒体播放器和音频管理器
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    //播放进度
    private int currentTime;
    //播放总进度
    private int duration;
    //当前歌曲在歌曲中搞得位置
    private int songIndex;

    private ExecutorService es= Executors.newSingleThreadExecutor();

    private Runnable progressRunnable=new Runnable() {
        @Override
        public void run() {
            int currentTime=mediaPlayer.getCurrentPosition();

            //发送广播，通知playActivity界面更新UI
            Intent intent=new Intent();
            intent.putExtra("progress",currentTime);
            intent.putExtra("max",duration);
            intent.setAction("com.andy.progress");

            sendBroadcast(intent);

            Log.d("progressRunnable","progress="+currentTime+",duration="+mediaPlayer.getDuration());

            SystemClock.sleep(1000);

            if (isPlaying&&currentTime<duration){
                progressRunnable.run();
            }else {
                Log.d("progressRunnable","暂停播放");
            }
        }
    };


    /*
    * 在service被创建时初始化媒体播放器和音频管理器
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicPlayService","onCreate");

        mediaPlayer=getMediaPlayer(AppApplication.getAppContext());
        audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    /*
    * 根据不同API版本实例化媒体播放器
    * */
    private MediaPlayer getMediaPlayer(Context appContext) {
        MediaPlayer mediaPlayer=new MediaPlayer();
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            return mediaPlayer;
        }

        try {
            Class<?> cMediaTimeProvider=Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );
            Constructor<?> constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(appContext, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            f.set(subtitleInstance,new Handler());
            Method setSubtitleAnchor = mediaPlayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
            setSubtitleAnchor.invoke(mediaPlayer, subtitleInstance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d("MusicPlayService","onBind");
        return new MusicBinder();
    }

    public void setStartFlag(boolean b) {

        START_FLAG=b;
    }

    public boolean getFlag(){
        return START_FLAG;
    }


    /*
    * 在服务被绑定的时候初始化musicBinder类的实例，提供给UI界面获得MusicPlayService
    * 并调用改类的方法
    * */
    public class MusicBinder extends Binder {

        public MusicPlayService getMusicPlayService(){
            return MusicPlayService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }

    /*
    *listActivity需要的功能
    *   1.全部播放
    *   2.单曲播放
    *
    * playingActivity需要的功能：
    *   1.实现音乐的播放暂停
    *   2.实现音乐的上一曲，下一曲
    *   3.实现进度条拖动更新播放进度
    *   4.实现修改播放模式，列表播放(0)，单曲播放(1)，随机播放(2)
    *   5.实现动态通知UI更新进度
    * */

    /*
    * 歌曲的播放控制 暂停
    *
    * */
    public void pause(){
        if (null==mediaPlayer)return;
        if (isPlaying){
            currentTime=mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            isPlaying=false;
        }
    }

    /*
    *歌曲播放控制 继续播放
    * */
    public void resume(){
        if (null==mediaPlayer)return;
        mediaPlayer.start();
        if (currentTime>0){
            mediaPlayer.seekTo(currentTime);
            isPlaying=true;
        }

        //progressRunnable.run();
        es.execute(progressRunnable);
    }

    /*
    * 歌曲播放控制 上一首
    *
    * */
    public void pre(boolean isLocal){
        mediaPlayer.stop();
        currentTime=0;

        //判断是否以及是第一首了，是则没有前一首
        if (songIndex==0){
            songIndex=0;
        }else {
            if (mPlayList.size()>0) {
                songIndex--;
            }else {
                songIndex=0;
            }
        }

        //播放
        START_FLAG=false;
        playOne(songIndex,isLocal);
        //更新UI（海报）
        updateUI(mPlayList.get(songIndex));
    }

    /*
    *
    * 拖动进度条改变播放进度
    * */
    public void seekTo(int currentTime){
        if (null==mediaPlayer)return;
        mediaPlayer.seekTo(currentTime);
    }

    /*
    * 歌曲播放控制 下一首
    * */
    public void next(boolean isLocal){
        mediaPlayer.stop();
        currentTime=0;

        //如果播放列表大于0则加1
        if (mPlayList.size()>0){
            songIndex++;
        }
        //如果下一首的index超出了长度，则设置播放第一首
        if (songIndex>=mPlayList.size()){
            songIndex=0;
        }

        START_FLAG=false;
        playOne(songIndex,isLocal);
        updateUI(mPlayList.get(songIndex));
    }

    /*
    * 停止媒体播放
    * */
    public void stop(){
        if (null==mediaPlayer)return;
        mediaPlayer.stop();
    }

    /*
    * 设置单曲或者列表播放
    * */
    public void setPlayAll(boolean isPlayAll){
        this.isPlayAll=isPlayAll;
    }

    /*
    * 在修改播放歌曲的时候更新UI
    *
    * */
    private void updateUI(SongDetail songDetail) {

        Log.d("MusicPlayService","updateUI 更新歌曲的索引为:"+songIndex);

        SongPlayingInfo updateInfo=new SongPlayingInfo();
        updateInfo.setIndex(songIndex);
        updateInfo.setTitle(songDetail.getSonginfo().getTitle());
        updateInfo.setArtist(songDetail.getSonginfo().getAuthor());
        updateInfo.setPosterUrl(songDetail.getSonginfo().getPic_premium());

        //通过EventBus与UI线程通讯
        EventBus.getDefault().post(updateInfo);
    }

    /*
    * 实现修改播放模式，列表播放(0)，单曲播放(1)，随机播放(2)
    * */
    public void setPlayMode(int playMode){
        this.playMode=playMode;
    }

    /*
    * 获取到播放模式
    * */
    public int getPlayMode(){
        return playMode;
    }

    /*
    * 清除所有的歌曲列表
    * */
    public void clearPlayList(){
        mPlayList.clear();
    }

    //1.歌曲播放，需要将播放列表设置进来
    public void setPlayList(List<SongDetail> mPlayList) {
        this.mPlayList = mPlayList;
    }

    public List<SongDetail> getPlayList() {
        return mPlayList;
    }

    //1.歌曲播放，需要将播放列表设置进来
    public void setOneInPlayList(SongDetail songDetail) {
        this.mPlayList.add(songDetail);
    }

    /*
    * 1.设置播放列表和播放方式,外部通过setOneInPlayList()设置
    * 2.判断网络
    * 3.获取第一首，重置播放器参数，设置播放链接
    * 4.播放器准备好后启动播放页
    * 5.等待播放完毕，根据单曲或者全部播放，以及播放模式决定是否停止或者切换歌曲
    *
    * */
    public void playAll(Boolean isLocal,Context mContext){

        isPlayAll=true;
        isPlaying=true;
        START_FLAG=true;

        if (!isLocal&&!NetworkUtil.isNetworkAvailable(mContext)){
            Toast.makeText(AppApplication.getAppContext(),"没有网络了哟，请检查网络设置",Toast.LENGTH_SHORT).show();
        }

        if (null==mediaPlayer)return;
        if (requestFocus()){

            final SongDetail song=mPlayList.get(0);
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //重置播放进度
            currentTime=0;
            //song.setOnClick(true);

            try {
                mediaPlayer.setDataSource(song.getBitrate().getFile_link());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        Log.d("MusicPlayService","mediaPlayer onPrepared");

                        mp.start();
                        currentTime=mp.getCurrentPosition();
                        duration=mp.getDuration();

                        //UI更新进度
                        es.execute(progressRunnable);

                        //启动播放页
                        if (START_FLAG) {
                            startPlayingActivity(song);
                        }

                    }
                });

                mediaPlayer.setOnCompletionListener(completionListener);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    * 1.根据位置获取歌曲
    * 2.判断网络
    * 3.设置播放源
    * 4.等待准备好
    * 5.跳转页面
    *
    * */
    public void playOne(int position,boolean isLocal){

        final SongDetail songDetail=mPlayList.get(position);
        isPlayAll=false;
        isPlaying=true;
        //songIndex=position;

        if (!isLocal&&!NetworkUtil.isNetworkAvailable(AppApplication.getAppContext())){
            Toast.makeText(AppApplication.getAppContext(),"没有网络了哟，请检查网络设置",Toast.LENGTH_SHORT).show();
        }

        if (null==mediaPlayer)return;
        if (requestFocus()){

            currentTime=0;
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(songDetail.getBitrate().getFile_link());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(completionListener);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        currentTime=mp.getCurrentPosition();
                        duration=mp.getDuration();

                        //UI更新进度
                        //progressRunnable.run();
                        es.execute(progressRunnable);
                        if (START_FLAG) {
                            Log.d("playOne", "启动activity");
                            startPlayingActivity(songDetail);
                        }else {
                            Log.d("playOne", "updateUI");
                            updateUI(songDetail);
                        }
                    }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    /*
    * 判断mediaPlayer是否获得焦点
    * */
    private boolean requestFocus() {
        int result=audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        return result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                //获得焦点
                case AudioManager.AUDIOFOCUS_GAIN:
                    resume();
                    break;
                //失去音频焦点、持续时间长
                case AudioManager.AUDIOFOCUS_LOSS:
                    stop();
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

    /*
    * 启动播放页
    * */
    private void startPlayingActivity(SongDetail song) {

        String name=song.getSonginfo().getTitle();
        String artist=song.getSonginfo().getAuthor();
        String posterUrl=song.getSonginfo().getPic_premium();

        Intent intent=new Intent(MusicPlayService.this, PlayingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title",name);
        intent.putExtra("artist",artist);
        intent.putExtra("posterUrl",posterUrl);
        intent.putExtra("position",songIndex);
        intent.putExtra("isPlaying",true);
        intent.putExtra("duration",duration);
        intent.putExtra("currentTime",currentTime);
        startActivity(intent);
    }


    //播放完成监听
    MediaPlayer.OnCompletionListener completionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            Log.d("MusicPlayService","mediaPlayer onCompletion");
            //播放结束 0列表循环 1单曲循环 2.随机播放
            if (playMode==0) {
                START_FLAG = false;
                // EventBus.getDefault().post(null);

/*                if (songIndex == (mPlayList.size()-1)){
                    songIndex=0;
                }else{
                    songIndex=songIndex+1;
                }*/
                Log.d("onCompletion","列表循环,songIndex="+songIndex);

                playOne(songIndex,false);


            }else if (playMode==1){
                START_FLAG=false;
                playOne(songIndex,false);

                Log.d("onCompletion","单曲循环,songIndex="+songIndex);
            }else {
                START_FLAG=false;
                int temp=new Random().nextInt(mPlayList.size());
                songIndex=temp;
                playOne(songIndex,false);

                Log.d("onCompletion","随机播放,songIndex="+songIndex);
            }

        }
    };
}
