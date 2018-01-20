package com.andy.orange.ui.music;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicPlayingViewPagerAdapter;
import com.andy.orange.app.AppApplication;
import com.andy.orange.base.BaseActivityWithoutStatus;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongPlayingInfo;
import com.andy.orange.broadcastreceiver.ProgressReceiver;
import com.andy.orange.service.MediaPlayService;
import com.andy.orange.service.MediaServiceConnection;
import com.andy.orange.service.MusicPlayService;
import com.andy.orange.ui.music.fragment.RoundFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.radius;

/**
 * Created by Andy Lau on 2017/9/4.
 */

public class PlayingActivity extends BaseActivityWithoutStatus implements View.OnClickListener {

    private static android.content.Context mContext;
    @BindView(R.id.album_art_bg)
    ImageView playingBgImage;

    @BindView(R.id.music_playing_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.music_playing_frame)
    FrameLayout mPlayingFrame;

    @BindView(R.id.music_playing_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.iv_playing_fav)
    ImageView mLoveImageView;

    @BindView(R.id.iv_playing_down)
    ImageView mDownLoadImageView;

    @BindView(R.id.iv_playing_cmt)
    ImageView mCommentImageView;

    @BindView(R.id.iv_playing_more)
    ImageView mMoreImageView;

    @BindView(R.id.tv_music_duration_played)
    TextView PlayedDuration;

    @BindView(R.id.sb_play_seek)
    SeekBar mSeekBar;

    @BindView(R.id.tv_music_duration)
    TextView totalDuration;

    @BindView(R.id.iv_playing_mode)
    ImageView mModeImageView;

    @BindView(R.id.iv_playing_pre)
    ImageView mPreImageView;

    @BindView(R.id.iv_playing_play)
    ImageView mPlayImageView;

    @BindView(R.id.iv_playing_next)
    ImageView mNextImageView;

    @BindView(R.id.iv_needle)
    ImageView mNeedleImageView;

    @BindView(R.id.iv_playing_playlist)
    ImageView mPlayListImageView;
    private int mDuration;
    private int mCurrentTime;
    private String mTitle;
    private int mPosition;
    private String mArtist;
    private String mPosterUrl;
    private boolean mIsPlaying;
    private ActionBar mActionBar;
    private Intent mIntent;
    private MediaServiceConnection mConnection;
    private ProgressReceiver mReceiver;
    private List<BaseFragment> mFragmentList=new ArrayList<>();
    private boolean isLocal=false;
    private MusicPlayingViewPagerAdapter mPagerAdapter;
    private static int radius = 15;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //返回进度条值(0-100之间)
            if (msg.arg1!=0){
                int pos=msg.arg1;
                SimpleDateFormat format=new SimpleDateFormat("mm:ss");
                String cur=format.format(pos);
                PlayedDuration.setText(cur);

                Log.d("handleMessage Progress",mSeekBar.getProgress()+"");
                mSeekBar.setProgress(pos);
                Log.d("handleMessage Progress",mSeekBar.getProgress()+"");

            }

            //获取最大进度值
            if (msg.arg2 != 0) {

                int max = msg.arg2;
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                String total = format.format(max);
                totalDuration.setText(total);

                Log.d("handleMessage max",max+"");
                mSeekBar.setMax(max);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_music_play;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        //StatusBarSetting.transparentStatusBar(this);
        mContext=this;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (null!=bundle){
            mIsPlaying = (boolean)bundle.get("isPlaying");
            mDuration=(int)bundle.get("duration");
            mCurrentTime = (int) bundle.get("currentTime");
            mPosition = (int) bundle.get("position");
            mTitle = (String) bundle.get("title");
            mArtist = (String) bundle.get("artist");
            mPosterUrl = (String) bundle.get("posterUrl");
        }
        
        setToolbar();
        setUI();
        setViewPager();

        mReceiver=new ProgressReceiver(mHandler);
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.andy.progress");
        registerReceiver(mReceiver,filter);

        //注册监听更新UI的事件
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 更新UI事件的接收
     *
     * @param info 歌曲信息
     */
    @Subscribe
    public void onUpdateUIEvent(SongPlayingInfo info) {
        System.out.println("接收到更新UI的消息");
        String title = info.getTitle();
        String author = info.getArtist();
        String url = info.getPosterUrl();

        mActionBar.setTitle(title + "---" + author);

        if (!TextUtils.isEmpty(url)) {
            new PathAsyncTask(playingBgImage,this).execute(url);
        }
        System.out.println("歌曲索引：" + info.getIndex());
        updateData();

       // mViewPager.setCurrentItem(info.getIndex());


    }

    @Subscribe
    public void onUpdateViewPagerEvent() {

        Log.d("onUpdateViewPagerEvent","onUpdateViewPagerEvent");

        int item = mViewPager.getCurrentItem();
        mPosition=0;
        updateData();
/*        if (item == mFragmentList.size()) {
            return;
        }
        mViewPager.setCurrentItem(item + 1);*/
    }
/*    public void onUpdateViewPagerEvent(UpdateViewPagerBean bean) {
        int item = mViewPager.getCurrentItem();
        updateData();
        if (item == mFragmentList.size()) {
            return;
        }
        mViewPager.setCurrentItem(item + 1);
    }*/


    private void setViewPager() {
        RoundFragment roundFragment = new RoundFragment();
        Bundle bundle=new Bundle();
        bundle.putString("posterUrl",mPosterUrl);
        roundFragment.setArguments(bundle);
        mFragmentList.add(roundFragment);

        mPagerAdapter=new MusicPlayingViewPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateData();
            }

            @Override
            public void onPageSelected(int position) {
                if (position-mPosition==-1){
                    mConnection.musicPlayService.pre(isLocal);
                }
                if (position-mPosition==1){
                    mConnection.musicPlayService.next(isLocal);
                }

                mPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*
    * 更新歌曲fragment UI信息
    * */
    private void updateData() {

        if (mFragmentList.size()==1){
            if (mConnection.musicPlayService==null){
                return;
            }

            //获取到播放列表
            List<SongDetail> list=mConnection.musicPlayService.getPlayList();
            //清楚之前生成List Fragment
            mFragmentList.clear();

            //初始化行的FragmentList
            for (int i=0;i<list.size();i++){
                SongDetail info=list.get(i);
                String url=info.getSonginfo().getPic_premium();
                RoundFragment roundFragment=new RoundFragment();

                Bundle bundle=new Bundle();
                bundle.putString("posterUrl",url);
                roundFragment.setArguments(bundle);
                mFragmentList.add(roundFragment);
            }

            mPagerAdapter.notifyDataSetChanged();

            //设置切换到指定的fragment
            mViewPager.setCurrentItem(mPosition);
        }
    }

    /*
    * 设置UI布局
    * */
    private void setUI() {
        if (null==mIntent){
            mIntent=new Intent(this, MusicPlayService.class);
            mConnection=new MediaServiceConnection(mPlayImageView);
            bindService(mIntent,mConnection,BIND_AUTO_CREATE);
        }
        mActionBar.setTitle(mTitle+"---"+mArtist);
        if (!TextUtils.isEmpty(mPosterUrl)){
            new PathAsyncTask(playingBgImage,this).execute(mPosterUrl);
        }
        float rotation=mNeedleImageView.getRotation();
        if (mIsPlaying||rotation==-35){
            mNeedleImageView.setRotation(5);
        }else{
            mNeedleImageView.setRotation(-35);
        }

        mPlayImageView.setOnClickListener(this);
        mNextImageView.setOnClickListener(this);
        mPreImageView.setOnClickListener(this);
        mModeImageView.setOnClickListener(this);

        /*
        * 进度条拖动修改播放进度
        * */
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                mSeekBar.setProgress(progress);

                Log.d("mSeekBar","onProgressChanged progress="+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Log.d("mSeekBar","onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Log.d("mSeekBar","onStopTrackingTouch");

                int progress=seekBar.getProgress();
                mConnection.musicPlayService.seekTo(progress);
            }
        });
    }

    /*
    * 设置toolbar
    * */
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();

                Log.d("finish",""+mConnection.musicPlayService.getFlag());
                mConnection.musicPlayService.setStartFlag(true);
                Log.d("finish",""+mConnection.musicPlayService.getFlag());

                //在退出的时候停止播放
                //mConnection.musicPlayService.stop();
                mConnection.musicPlayService.isPlaying=false;
                finish();

            }
        });
    }

    /*
    * 点击事件监听
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_playing_mode:

                switch (mConnection.musicPlayService.getPlayMode()){
                    case 0:
                        mModeImageView.setImageResource(R.drawable.play_icn_one);
                        mConnection.musicPlayService.setPlayMode(1);
                        break;
                    case 1:
                        mModeImageView.setImageResource(R.drawable.play_icn_shuffle);
                        mConnection.musicPlayService.setPlayMode(2);

                        break;
                    case 2:
                        mModeImageView.setImageResource(R.drawable.play_icn_loop);
                        mConnection.musicPlayService.setPlayMode(0);
                        break;
                    default:
                        break;
                }

                break;
            case R.id.iv_playing_pre:
                int item1=mViewPager.getCurrentItem();
                if (item1==0){
                    return;
                }
                mViewPager.setCurrentItem(item1-1);
                break;

            case R.id.iv_playing_play:

                if(mConnection.musicPlayService.isPlaying){
                    mPlayImageView.setImageResource(R.drawable.play_rdi_btn_play);
                    mNeedleImageView.setRotation(-35);

                    mConnection.musicPlayService.pause();
                }else {
                    mPlayImageView.setImageResource(R.drawable.play_rdi_btn_pause);
                    mNeedleImageView.setRotation(5);

                    mConnection.musicPlayService.resume();
                }

                break;
            case R.id.iv_playing_next:
                int item=mViewPager.getCurrentItem();
                updateData();
                if (item==mFragmentList.size()){
                    return;
                }
                mViewPager.setCurrentItem(item+1);
                break;
            case R.id.iv_playing_playlist:

                break;
            default:
                break;
        }
    }

    /*
    * 缓存并设置模糊背景
    * */
    private static class PathAsyncTask extends AsyncTask<String,Void,String> {

        private ImageView mImage;
        private Context cxt;
        private String mPath;
        private FileInputStream mFis;

        public PathAsyncTask(ImageView playingBgImage, Context context) {

            mImage=playingBgImage;
            cxt=context;
        }

        @Override
        protected String doInBackground(String... params) {

            FutureTarget<File> future= Glide.with(cxt)
                    .load(params[0])
                    .downloadOnly(100,100);
            try {
                File cacheFile=future.get();
                mPath=cacheFile.getAbsolutePath();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return mPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (null!=s){
                    mFis=new FileInputStream(s);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(mFis);
            if (null==bitmap){
                bitmap=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.head_image);
            }
            applyBlur(bitmap, mImage);

        }
    }

    /*
    * 图片模糊化
    * */
    private static void applyBlur(Bitmap bgBitmap, ImageView mImageView) {

        //处理得到模糊效果的图
        RenderScript renderScript = RenderScript.create(mContext);
        // Allocate memory for Renderscript to work with
        final Allocation input = Allocation.createFromBitmap(renderScript, bgBitmap);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());

        if (Build.VERSION.SDK_INT>=17) {
            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            scriptIntrinsicBlur.setInput(input);
            // Set the blur radius
            scriptIntrinsicBlur.setRadius(radius);
            // Start the ScriptIntrinisicBlur
            scriptIntrinsicBlur.forEach(output);
            // Copy the output to the blurred bitmap
            output.copyTo(bgBitmap);
            renderScript.destroy();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bgBitmap);

            if (null==mImageView){

            }else {

                mImageView.setBackground(bitmapDrawable);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        unregisterReceiver(mReceiver);
    }
}
