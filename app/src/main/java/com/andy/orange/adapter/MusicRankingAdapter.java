package com.andy.orange.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.bean.RankingListItem;
import com.andy.orange.ui.music.MusicRankingListDetailActivity;
import com.andy.orange.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MusicRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContent;
    private List<RankingListItem.ContentBeanX> mList;
    private LayoutInflater inflater;

    public MusicRankingAdapter(Context context,List<RankingListItem.ContentBeanX> mList) {

        this.mContent=context;
        this.mList=mList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicRankingViewHolder(inflater.inflate(R.layout.item_music_rangking,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MusicRankingViewHolder){
            MusicRankingViewHolder rankingViewHolder= (MusicRankingViewHolder) holder;

            String name=mList.get(position).getName();
            String firstTitle=mList.get(position).getContent().get(0).getTitle();
            String firstAuthor=mList.get(position).getContent().get(0).getAuthor();
            String displayText1=1+"."+firstTitle+"-"+firstAuthor;

            String secondTitle=mList.get(position).getContent().get(1).getTitle();
            String secondAuthor=mList.get(position).getContent().get(1).getAuthor();
            String displayText2=2+"."+secondTitle+"-"+secondAuthor;

            String threeTitle=mList.get(position).getContent().get(2).getTitle();
            String threeAuthor=mList.get(position).getContent().get(2).getAuthor();
            String displayText3=2+"."+threeTitle+"-"+threeAuthor;

            String url=mList.get(position).getPic_s192();

            ImageLoaderUtils.display(mContent,rankingViewHolder.rankingPoster,url);

            rankingViewHolder.rankingName.setText(name);
            rankingViewHolder.firstSongName.setText(displayText1);
            rankingViewHolder.secondSongName.setText(displayText2);
            rankingViewHolder.threeSongName.setText(displayText3);

            rankingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int type=mList.get(position).getType();
                    //Toast.makeText(mContent,type+"",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(mContent, MusicRankingListDetailActivity.class);
                    intent.putExtra("type",type);

                    mContent.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MusicRankingViewHolder extends RecyclerView.ViewHolder {

        private ImageView rankingPoster;
        private TextView rankingName;
        private TextView firstSongName;
        private TextView secondSongName;
        private TextView threeSongName;


        public MusicRankingViewHolder(View itemView) {

            super(itemView);

            rankingPoster= (ImageView) itemView.findViewById(R.id.ranking_poster);
            rankingName= (TextView) itemView.findViewById(R.id.ranking_name);
            firstSongName= (TextView) itemView.findViewById(R.id.tv_rank_first);
            secondSongName= (TextView) itemView.findViewById(R.id.tv_rank_second);
            threeSongName= (TextView) itemView.findViewById(R.id.tv_rank_third);
        }
    }
}
