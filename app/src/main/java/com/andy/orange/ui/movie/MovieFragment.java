package com.andy.orange.ui.movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ImageView;

import com.andy.orange.R;
import com.andy.orange.adapter.MovieAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.bean.GenresMovie;
import com.andy.orange.client.RxDisposeManager;
import com.andy.orange.ui.movie.presenter.impl.MoviePresenterImpl;
import com.andy.orange.ui.movie.view.MovieView;
import com.andy.orange.widget.LoadMoreFooterView;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MovieFragment extends BaseFragment implements OnRefreshListener,OnLoadMoreListener,MovieView {

    @BindView(R.id.irv_movie)
    IRecyclerView iRecyclerView;

    private String mMovieGenres;
    private final static int PAGE_SIZE=16;
    private final static String TAG="Andy";
    private LoadMoreFooterView loadMoreFooterView;
    private Context context;
    private MovieAdapter mMovieAdapter;
    private MoviePresenterImpl moviePresenter;
    private int startPage=0;
    private List<GenresMovie.SubjectsBean> mList=new ArrayList<>();

    /*
    * 获取布局ID，初始化布局
    * */
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_movie;
    }

    /*
    * 初始化内容View
    * */
    @Override
    protected void initView() {
        if (getArguments()!=null){
            mMovieGenres=getArguments().getString("type");
        }

        //获取fragment的上下文对象
        context=getActivity();

        //初始化presenter层
        moviePresenter=new MoviePresenterImpl(this,getActivity());

        mMovieAdapter=new MovieAdapter(context,mList);
        //设置Item点击
        mMovieAdapter.setmOnItemClickListener(new MovieAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, ImageView imageView) {

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

/*                Bundle bundle=new Bundle();
                bundle.putSerializable("mMovieDetail",mMovieDetail);

                intent.putExtra("mMovieDetail", mMovieDetail);*/

                intent.putExtra("movieId",mList.get(pos).getId());


                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

                    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,imageView,"电影海报");
                    context.startActivity(intent);
                }else {
                    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat
                            .makeScaleUpAnimation(imageView,imageView.getWidth()/2,imageView.getHeight()/2,0,0);
                    ActivityCompat.startActivity(context,intent,optionsCompat.toBundle());
                }


            }
        });

        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        manager.setItemPrefetchEnabled(false);
        iRecyclerView.setLayoutManager(manager);
        iRecyclerView.setOnRefreshListener(this);
        iRecyclerView.setOnLoadMoreListener(this);
        iRecyclerView.setItemAnimator(new LandingAnimator());
        iRecyclerView.setIAdapter(new ScaleInAnimationAdapter(mMovieAdapter));
        loadMoreFooterView=(LoadMoreFooterView) iRecyclerView.getLoadMoreFooterView();//new LoadMoreFooterView(context,null);

/*        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }*/
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

        Log.d(TAG,"当前可见状态:"+isVisible+"当前题材:"+mMovieGenres);
        if (isVisible){
            moviePresenter.getMoviesByType(PAGE_SIZE,startPage,mMovieGenres);

        }else {
            RxDisposeManager.getInstance().cancel("MovieByType");
        }
    }

    @Override
    public void onLoadMore() {

        startPage++;
            Log.d(TAG,"start="+PAGE_SIZE*startPage+",end="+(PAGE_SIZE*startPage+PAGE_SIZE));

        moviePresenter.getMoviesByType(PAGE_SIZE,startPage,mMovieGenres);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
    }


    @Override
    public void onRefresh() {
        startPage=0;
        iRecyclerView.setRefreshing(true);
        moviePresenter.getMoviesByType(PAGE_SIZE,startPage,mMovieGenres);
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
    }

    @Override
    public void getGenresMovie(GenresMovie movies) {

        if (!isFirst){
            isFirst=true;

        }
        if (movies.getSubjects().size()!=0){
            if (startPage==0){
                Log.d(TAG,"初次数据大小:"+mList.size());
                iRecyclerView.setRefreshing(false);
                mList.clear();
                mList.addAll(movies.getSubjects());

                mMovieAdapter.notifyDataSetChanged();
            }else {

                Log.d(TAG,"数据集大小:"+mList.size());
                mList.addAll(movies.getSubjects());
                mMovieAdapter.notifyDataSetChanged();
            }
        }else {
            Log.d(TAG,"数据集中数据条目:"+movies.getSubjects().size());

            mList.addAll(movies.getSubjects());
            startPage--;

            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        }

        for (int i=0;i<movies.getSubjects().size();i++){
            Log.d(TAG,"=========================");
            Log.d(TAG,movies.getSubjects().get(i).getTitle());
            Log.d(TAG,movies.getSubjects().get(i).getId());
            Log.d(TAG,movies.getSubjects().get(i).getRating().getStars());
            Log.d(TAG,movies.getSubjects().get(i).getImages().getLarge());
            Log.d(TAG,"=========================");

        }

    }

/*
    @Subscribe
    public void onFabScrollEvent(FabScrollBean fabScrollBean){
        if (fabScrollBean.isTop()){
            Log.d(TAG,"接收到消息");


            //滑动到开始位置
            iRecyclerView.smoothScrollToPosition(0);
        }
    }*/
}
