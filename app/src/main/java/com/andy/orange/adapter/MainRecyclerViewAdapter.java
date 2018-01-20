package com.andy.orange.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.bean.Film;
import com.andy.orange.utils.GlideLoader;
import com.andy.orange.utils.ImageLoaderUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lau on 2017/8/9.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private onItemClickListener mOnItemClickListener;
    private final Context context;
    private final LayoutInflater inflater;
    private List<Film.SubjectsBean> list;
    private static final int TYPE_BANNER = 0;
    private static final int TYPE_MOVIE_DES = 1;
    private static final int TYPE_MOVIE_DETAIL = 2;
    private static final int TYPE_FOOT =3;
    private List<String> mImages = new ArrayList<>();

    public MainRecyclerViewAdapter(Context context, List<Film.SubjectsBean> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_MOVIE_DES;
        } else if(position== list.size()+2){
            return TYPE_FOOT;
        }else {
            return TYPE_MOVIE_DETAIL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new BannerViewHolder(inflater.inflate(R.layout.item_home_banner, parent, false));
            case TYPE_MOVIE_DES:
                return new MovieDesViewHolder(inflater.inflate(R.layout.item_home_movie_title, parent, false));
            case TYPE_MOVIE_DETAIL:
                return new MovieDetailViewHolder(inflater.inflate(R.layout.item_movie, parent, false));
            case TYPE_FOOT:
                return new BottomViewHolder(inflater.inflate(R.layout.item_home_bottom, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            setBannerItemValues((BannerViewHolder) holder, position);
        } else if (holder instanceof MovieDetailViewHolder) {
            setMovieDetailItemValues((MovieDetailViewHolder) holder, position);
        }
    }


    private void setMovieDetailItemValues(final MovieDetailViewHolder holder, final int position) {

        Film.SubjectsBean filmItem=list.get(position-2);

        //获取评分
        Double score=filmItem.getRating().getAverage();
        //获取标题
        String title=filmItem.getTitle();

        //获取海报图片路径
        String  poster=filmItem.getImages().getLarge();


        holder.mTvRating.setText(score+"");
        holder.mTvMovieName.setText(title);

        //glide显示poster
        ImageLoaderUtils.display(context, holder.mIvPhoto, poster);

        holder.mRlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position-2,holder.mIvPhoto);
            }
        });
    }

    /*
    * 设置顶部banner的数据
    * */
    private void setBannerItemValues(final BannerViewHolder holder, int position) {

        //将前4个的海报设置到banner里面
        if (mImages.size() == 0 && list.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                Film.SubjectsBean info = list.get(i);
                mImages.add(info.getImages().getLarge());
            }
        }
        //设置圆点指示器
        holder.mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载框架
        holder.mBanner.setImageLoader(new GlideLoader());
        //设置图片路径List
        holder.mBanner.setImages(mImages);
        //设置banner动画
        holder.mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置自动播放
        holder.mBanner.isAutoPlay(true);
        //设置延迟
        holder.mBanner.setDelayTime(3000);
        //设置指示器位置
        holder.mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //启动
        holder.mBanner.start();
        //设置点击监听
        holder.mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(context,"movie——>"+list.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() + 3;
    }

    /**
     * banner视图映射
     * */
    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private Banner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBanner = (Banner) itemView.findViewById(R.id.banner_home);
        }
    }

    /**
     * 热门电影标题和跟多的视图映射
     * */
    public class MovieDesViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvMore;
        public MovieDesViewHolder(View itemView) {
            super(itemView);
            mTvMore = (TextView) itemView.findViewById(R.id.tv_more);
            mTvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,"movie more!",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /**
     * 单个电影海报的视图映射
     * */
    public class MovieDetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPhoto;
        private RelativeLayout mRlRoot;
        private TextView mTvRating;
        private TextView mTvMovieName;

        public MovieDetailViewHolder(View itemView) {
            super(itemView);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvRating = (TextView) itemView.findViewById(R.id.tv_rating);
            mTvMovieName = (TextView) itemView.findViewById(R.id.tv_name);
            mRlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
        }
    }

    public class BottomViewHolder extends RecyclerView.ViewHolder {

        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position,ImageView imageView);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

}
