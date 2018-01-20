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
import com.andy.orange.bean.RankingListDetail;
import com.andy.orange.bean.SongListDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MusicRankingListDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;

    private Context cxt;
    private List<RankingListDetail.SongListBean> mList;
    private LayoutInflater inflater;
    private onItemClickListener mOnItemClickListener;
    private onPlayAllClickListener mOnPlayAllClickListener;

    public MusicRankingListDetailAdapter(Context cxt, List<RankingListDetail.SongListBean> mList) {
        this.cxt = cxt;
        this.mList = mList;
        inflater=LayoutInflater.from(cxt);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==TYPE_HEAD){
            return new HeaderViewHolder(inflater.inflate(R.layout.item_song_list_detail_header,parent,false));
        }else {
            return new ListViewHolder(inflater.inflate(R.layout.item_song_list_detail_list,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MusicRankingListDetailAdapter.HeaderViewHolder){
            ((HeaderViewHolder) holder).listNumber.setText("共"+mList.size()+"首");

            //全部播放点击事件
            ((HeaderViewHolder) holder).playAllLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPlayAllClickListener.onHeaderItemClick(mList);
                }
            });

        }else {
            ((ListViewHolder) holder).mItemNumber.setText(position+"");

            ((ListViewHolder) holder).mItemName.setText(mList.get(position-1).getTitle());
            ((ListViewHolder) holder).mItemAuthor.setText(mList.get(position-1).getAuthor());
            ((ListViewHolder) holder).mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(position-1);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout playAllLayout;
        private TextView listNumber;
        private ImageView menuIcon;


        public HeaderViewHolder(View itemView) {
            super(itemView);

            playAllLayout= (RelativeLayout) itemView.findViewById(R.id.play_all_relative);
            listNumber= (TextView) itemView.findViewById(R.id.play_all_number);
            menuIcon= (ImageView) itemView.findViewById(R.id.play_all_select);

            //扩展菜单点击事件
            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(cxt,"menuIcon",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemNumber;
        private TextView mItemName;
        private TextView mItemAuthor;
        private ImageView mItemMenu;
        private RelativeLayout mItemLayout;

        public ListViewHolder(View itemView) {

            super(itemView);
            mItemLayout= (RelativeLayout) itemView.findViewById(R.id.song_detail_item_relative);
            mItemNumber= (TextView) itemView.findViewById(R.id.song_list_item_number);
            mItemName= (TextView) itemView.findViewById(R.id.song_list_item_name);
            mItemAuthor= (TextView) itemView.findViewById(R.id.song_list_item_artist);
            mItemMenu= (ImageView) itemView.findViewById(R.id.song_list_item_pop_menu);

            mItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(cxt,"mItemMenu",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /*
    * list item点击监听接口
    * */
    public interface onItemClickListener{
        void  onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    /*
    * 全部播放的点击事件接口
    * */
    public interface onPlayAllClickListener{
        void onHeaderItemClick(List<RankingListDetail.SongListBean> details);
    }

    public void setOnPlayAllClickListener(onPlayAllClickListener listener){

        this.mOnPlayAllClickListener=listener;
    }
}
