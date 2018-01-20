package com.andy.orange.ui.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.adapter.MusicSongListDetailAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.app.AppApplication;
import com.andy.orange.base.BaseActivityWithoutStatus;
import com.andy.orange.bean.SongDetail;
import com.andy.orange.bean.SongListDetail;
import com.andy.orange.bean.SongListInfo;
import com.andy.orange.service.MediaPlayService;
import com.andy.orange.service.MediaServiceConnection;
import com.andy.orange.service.MusicPlayService;
import com.andy.orange.ui.music.presenter.impl.MusicSongListDetailPresenterImpl;
import com.andy.orange.ui.music.view.MusicSongListDetailView;
import com.andy.orange.ui.music.view.MusicSongListView;
import com.andy.orange.utils.ImageLoaderUtils;
import com.aspsine.irecyclerview.IRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;

import static android.R.attr.radius;

/**
 * Created by Andy Lau on 2017/8/28.
 */

public class MusicSongListDetailActivity extends BaseActivityWithoutStatus implements
        MusicSongListDetailView,MusicSongListDetailAdapter.onItemClickListener,MusicSongListDetailAdapter.onPlayAllClickListener{

    public static final  String MUSIC_URL_FROM = "webapp_music";
    public static final String MUSIC_URL_FORMAT = "json";
    public static final String MUSIC_URL_METHOD_SONGLIST_DETAIL ="baidu.ting.diy.gedanInfo";
    public static final String MUSIC_URL_FROM_2 = "android";
    public static final String MUSIC_URL_VERSION = "5.6.5.6";
    public static final String MUSIC_URL_METHOD_SONG_DETAIL ="baidu.ting.song.play";


    @BindView(R.id.overlay)
    View mOverlay;

    @BindView(R.id.song_list_toobar)
    Toolbar mToolbar;

    @BindView(R.id.song_list_detail_img)
    ImageView mSongListImage;

    @BindView(R.id.album_art)
    ImageView mListDetailBgImage;

    @BindView(R.id.header_detail)
    RelativeLayout mHeaderDetail;

    @BindView(R.id.tv_songlist_count)
    TextView mSongCount;

    @BindView(R.id.tv_songlist_name)
    TextView mSongListName;

    @BindView(R.id.tv_songlist_detail)
    TextView mSongListDesc;

    @BindView(R.id.recycler_song_detail)
    IRecyclerView mRecyclerView;

    private MusicSongListDetailPresenterImpl detailPresenter;
    private MediaServiceConnection mConnection;

    private String songListId;
    private Boolean isLocal;
    private String photoUrl;
    private String listName;
    private String detail;
    private String count;
    private static Context mContext;
    private static int radius = 25;
    private Intent mIntent;
    private List<SongListDetail.ContentBean> mList=new ArrayList<>();
    private MusicSongListDetailAdapter mDetailAdapter;
    private MusicPlayService mService;
    private static MusicPlayService.MusicBinder mMediaBinder;
    private HashMap<String,Integer> idMap=new HashMap<>();
    private SongDetail[] detailInfos;
    private AtomicInteger index=new AtomicInteger(0);

    @Override
    public int getLayoutId() {
        return R.layout.activity_songlist_detail;
    }

    @Override
    public void initPresenter() {

        detailPresenter=new MusicSongListDetailPresenterImpl(this,this);
    }

    @Override
    public void initView() {
        mContext=this;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if (bundle !=null){
            songListId=(String) bundle.get("songListId");
            isLocal=(Boolean)bundle.get("islocal");
            photoUrl=(String)bundle.get("songListPhoto");
            listName=(String)bundle.get("songListname");
            detail=(String)bundle.get("songListTag");
            count=(String)bundle.get("songListCount");
        }

        //设置Toolbar返回
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUI();
        setRecyclerView();
        //设置点击监听
        mDetailAdapter.setOnItemClickListener(this);
        mDetailAdapter.setOnPlayAllClickListener(this);
    }

    private void setRecyclerView() {

        //获取歌单详细信息
        detailPresenter.requestSongListDetail(MUSIC_URL_FORMAT, MUSIC_URL_FROM, MUSIC_URL_METHOD_SONGLIST_DETAIL,songListId);

        mDetailAdapter=new MusicSongListDetailAdapter(mContext,mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.setIAdapter(new ScaleInAnimationAdapter(mDetailAdapter));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));

        //LoadingDialog.showDialogForLoading(this).show();
        
    }

    private void setUI() {

        ImageLoaderUtils.display(this,mSongListImage,photoUrl);
        mSongCount.setText(count);
        mSongListName.setText(listName);

        String[] split=detail.split(",");
        StringBuffer sb=new StringBuffer();
        sb.append("标签:");

        for (int i=0;i<split.length;i++){
            sb.append(split[i]+" ");
        }
        mSongListDesc.setText(sb);

        //设置模糊背景
        new PathAsyncTask(mListDetailBgImage,mContext).execute(photoUrl);

        //开启播放服务
        mConnection=new MediaServiceConnection();
        if (mIntent==null){
            mIntent=new Intent(this, MusicPlayService.class);
            startService(mIntent);
            bindService(mIntent,mConnection,BIND_AUTO_CREATE);
            Log.d("setUI","startService");
        }else {
            Log.d("setUI","mIntent is not null");
        }
    }

    /*
    * 获取整个音乐List的细节
    * */
    @Override
    public void returnMusicListDetail(SongListDetail details) {

        mList.addAll(details.getContent());
        detailInfos = new SongDetail[mList.size()];
        mDetailAdapter.notifyDataSetChanged();

        for (int i=0;i<mList.size();i++){
            SongListDetail.ContentBean songDetail=mList.get(i);
            String songId=songDetail.getSong_id();
            idMap.put(songId,i);

            detailPresenter.requestSongDetail(MUSIC_URL_FROM_2,MUSIC_URL_VERSION,MUSIC_URL_FORMAT,
                    MUSIC_URL_METHOD_SONG_DETAIL,songId);
        }
    }

    /*
    * 获取某个单曲的detail
    * */
    @Override
    public void returnMusicDetail(SongDetail detail) {

        //获取到音乐播放服务的实例
        if(null !=mMediaBinder){
            if (mService==null){
                mService=mMediaBinder.getMusicPlayService();
            }

            if (detail.getSonginfo()==null){
                Toast.makeText(this,"该单曲不支持播放",Toast.LENGTH_SHORT).show();
            }else {
                String song_id=detail.getSonginfo().getSong_id();
                Integer pos=idMap.get(song_id);
                detailInfos[pos]=detail;
            }

            int currentNumber=index.addAndGet(1);

            //将歌曲全部添加到service播放列表中
            if (currentNumber==detailInfos.length){
                for (int i=0;i<detailInfos.length;i++){
                    if (i==0){
                        mService.clearPlayList();
                    }

                    mService.setOneInPlayList(detailInfos[i]);
                }
                //LoadingDialog.cancelDialogForLoading();
            }
        }else {
            Log.d("returnMusicDetail","mMediaBinder is null");
        }

    }


    private static class MediaServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.d("MediaServiceConnection","onServiceConnected");
            mMediaBinder= (MusicPlayService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d("MediaServiceConnection","onServiceDisconnected");
        }
    }

    /*
    * 单曲播放
    * */
    @Override
    public void onItemClick(int position) {

        Toast.makeText(this,"单曲播放",Toast.LENGTH_SHORT).show();

        if (mService!=null){
            mService.playOne(position,isLocal);
        }else {
            Log.d("onItemClick","mService is null");
        }
    }

    /*
    * 全部播放
    * */

    @Override
    public void onHeaderItemClick(List<SongListDetail.ContentBean> songDetails) {

        Toast.makeText(this,"全部播放",Toast.LENGTH_SHORT).show();

        if (mService!=null){
            mService.playAll(isLocal,mContext);
        }
    }

    /*
    *下载缓存并设置歌单模糊背景
    * */
    private static class PathAsyncTask extends AsyncTask<String,Void,String>{

        private ImageView mImageView;
        private String mPath;
        private FileInputStream mIs;
        private Context mContext;


        public PathAsyncTask(ImageView image,Context mContext){

            this.mImageView=image;
            this.mContext=mContext;
        }

        @Override
        protected String doInBackground(String... params) {

            //通过glide下载缓存歌单海报
            FutureTarget<File> future = Glide.with(mContext)
                    .load(params[0])
                    .downloadOnly(100,100);

            try {

                //获取到缓存路径
                File cacheFile=future.get();
                mPath=cacheFile.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return mPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Bitmap bitmap=null;
            try {

                //判断传回的线程返回的资源路径是否为空，为空设置默认的bitmap
                if (null==s) {
                    Drawable drawable=mContext.getResources().getDrawable(R.mipmap.head_image);
                    BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
                    bitmap=bitmapDrawable.getBitmap();
                }else {
                    mIs=new FileInputStream(s);
                    bitmap= BitmapFactory.decodeStream(mIs);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            applyBlur(bitmap,mImageView);
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
            mImageView.setBackground(bitmapDrawable);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
