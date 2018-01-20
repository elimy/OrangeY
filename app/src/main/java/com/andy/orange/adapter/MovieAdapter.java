package com.andy.orange.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andy.orange.R;
import com.andy.orange.bean.GenresMovie;
import com.andy.orange.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GenresMovie.SubjectsBean> mList;
    private LayoutInflater inflater;
    private onItemClickListener mOnItemClickListener;

    /*
    * 带参构造，传入上下文和电影list
    * */
    public MovieAdapter(Context cxt,List<GenresMovie.SubjectsBean> mList){
        this.context=cxt;
        this.mList=mList;
        inflater=LayoutInflater.from(cxt);
    }

    /*
    * 绑定item布局
    * */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(inflater.inflate(R.layout.item_movie,parent,false));
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MovieViewHolder){
            setMovieItemValues((MovieViewHolder) holder, position);
        }

    }

    /*
    * 绑定值和view
    * */
    private void setMovieItemValues(final MovieViewHolder holder, final int position) {
        GenresMovie.SubjectsBean info=mList.get(position);
        double score=info.getRating().getAverage();
        String name=info.getTitle();
        String url=info.getImages().getLarge();

        holder.mTvMovieName.setText(name);
        holder.mTvRating.setText(score+"");
        ImageLoaderUtils.display(context,holder.mIvPhoto,url);

        holder.mRlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position,holder.mIvPhoto);
            }
        });

/*        int width = DisplayUtil.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = holder.mRlRoot.getLayoutParams();
        params.width = width/2-40;
        holder.mRlRoot.setLayoutParams(params);      */
    }

    /*
    * 返回list的长度
    * */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    /*
    * 电影item的viewHolder
    * */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPhoto;
        private RelativeLayout mRlRoot;
        private ImageView mIvHot;
        private TextView mTvRating;
        private TextView mTvMovieName;

        public MovieViewHolder(View itemView) {
            super(itemView);
           // mIvHot = (ImageView) itemView.findViewById(R.id.iv_hot);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            mTvRating = (TextView) itemView.findViewById(R.id.tv_rating);
            mTvMovieName = (TextView) itemView.findViewById(R.id.tv_name);
            mRlRoot = (RelativeLayout) itemView.findViewById(R.id.rl_root);
        }

    }

    /*
    * 点击事件接口
    * */
    public interface onItemClickListener{
        void onItemClick(int pos, ImageView imageView);
    }

    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
}
