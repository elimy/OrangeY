package com.andy.orange.ui.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.bean.SubjectMovie;
import com.andy.orange.ui.movie.presenter.MovieDetailPresenter;
import com.andy.orange.ui.movie.presenter.impl.MovieDetailPresenterImpl;
import com.andy.orange.ui.movie.view.MovieDetailView;
import com.andy.orange.utils.ImageLoaderUtils;
import com.andy.orange.widget.SuperTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/19.
 */

public class MovieDetailActivity extends BaseActivity implements MovieDetailView, View.OnClickListener {

    private static final String TAG = "MovieDetailActivity";
    @BindView(R.id.film_detail_toolbar)
    Toolbar mtoolbar;
    @BindView(R.id.callapsing_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.film_appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.film_IV_bg_poster)
    ImageView movieBgPoster;
    @BindView(R.id.film_detail_img)
    ImageView moviePoster;

    @BindView(R.id.tv_film_name)
    TextView movieName;
    @BindView(R.id.tv_film_rating)
    TextView movieRating;
    @BindView(R.id.tv_film_genres)
    TextView movieGenres;
    @BindView(R.id.tv_film_release)
    TextView movieTime;

    @BindView(R.id.tv_watch)
    SuperTextView mWatch;
    @BindView(R.id.tv_judge)
    SuperTextView mJudge;

    @BindView(R.id.film_summary)
    TextView mFilmSummary;

    @BindView(R.id.btn_expend)
    ImageButton expendBtn;

    private Context mContext;
    private int radius = 25;
    private String mPosterUrl;
    private String movieId;
    private String mUrl;
    private Handler handler;
    private MovieDetailPresenter detailPresenter;
    private String name;
    private boolean expend=false;

    /*
    * 初始化布局
    * */
    @Override
    public int getChildLayoutId() {
        return R.layout.activity_movie_detail;
    }

    /*
    * 初始化presenter层
    * */
    @Override
    public void initPresenter() {

        mContext = this;

        detailPresenter = new MovieDetailPresenterImpl(this, this);

    }

    /*
    * 初始化view
    * */
    @Override
    public void initView() {

        //StatusBarSetting.setTranslucent(MovieDetailActivity.this);

        setSupportActionBar(mtoolbar);

/*        //收缩时标题颜色
        mToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);*/
        //扩展是标题颜色
        mToolbarLayout.setExpandedTitleColor(-1);

        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取指定电影的细节
        detailPresenter.getMovieDetailById(movieId);

    }

    /*
   * presenter获取返回电影细节
   * */
    @Override
    public void LoadMovieDetailById(SubjectMovie movieDetail) {

        //获取到数据
        setDataOnUI(movieDetail);
    }


    /*
    * 将数据设置到UI界面
    * */
    private void setDataOnUI(SubjectMovie mMovieDetail) {

        //获取到数据
        mPosterUrl = mMovieDetail.getImages().getLarge();
        name = mMovieDetail.getTitle();
        Double score = mMovieDetail.getRating().getAverage();
        String date = mMovieDetail.getYear();
        List<String> genresList = mMovieDetail.getGenres();

        //拼接电影类型文本
        String genres = "";
        for (int i = 0; i < genresList.size(); i++) {
            if (i == genresList.size() - 1) {
                genres += genresList.get(i);
            } else {
                genres += genresList.get(i) + "/";
            }
        }

        movieName.setText(name);
        movieRating.setText(score + "");
        movieGenres.setText(genres);
        movieTime.setText("上映时间:"+date);
        mFilmSummary.setText(mMovieDetail.getSummary());

        //设置显示数据
        ImageLoaderUtils.displayBigPhoto(mContext, moviePoster, mPosterUrl);

        //设置标题
        mToolbarLayout.setTitle(mMovieDetail.getTitle());

        //设置观看和评价的事件监听
        mJudge.setOnClickListener(this);
        mWatch.setOnClickListener(this);
        expendBtn.setOnClickListener(this);

        //设置模糊背景
        applyBlur();
    }

    /*
    * 按钮的事件监听响应
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_watch:

                Intent intent = new Intent(MovieDetailActivity.this, MoviePlayActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);

                break;
            case R.id.tv_judge:
                Toast.makeText(this, "评价", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_expend:
                if (!expend){

                    mFilmSummary.setMinLines(0);
                    mFilmSummary.setMaxLines(Integer.MAX_VALUE);
                    expend=true;
                    expendBtn.setRotation(180f);
                }else {
                    mFilmSummary.setLines(3);
                    expendBtn.setRotation(0);
                    expend=false;
                }
                break;
        }
    }

    /*设置海报的模糊背景*/
    private void applyBlur(){
        Object localObject = ((BitmapDrawable)getResources().getDrawable(R.mipmap.head_image)).getBitmap();
        RenderScript localRenderScript = RenderScript.create(this.mContext);
        Allocation localAllocation1 = Allocation.createFromBitmap(localRenderScript, (Bitmap)localObject);
        Allocation localAllocation2 = Allocation.createTyped(localRenderScript, localAllocation1.getType());
        if (Build.VERSION.SDK_INT >= 17)
        {
            ScriptIntrinsicBlur localScriptIntrinsicBlur = ScriptIntrinsicBlur.create(localRenderScript, Element.U8_4(localRenderScript));
            localScriptIntrinsicBlur.setInput(localAllocation1);
            localScriptIntrinsicBlur.setRadius(this.radius);
            localScriptIntrinsicBlur.forEach(localAllocation2);
            localAllocation2.copyTo((Bitmap)localObject);
            localRenderScript.destroy();
        }
        localObject = new BitmapDrawable(getResources(), (Bitmap)localObject);
        if (Build.VERSION.SDK_INT >= 16) {
            this.movieBgPoster.setBackground((Drawable)localObject);
        }
    }



}
