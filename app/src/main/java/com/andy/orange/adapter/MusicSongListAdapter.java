package com.andy.orange.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.bean.SongListInfo;
import com.andy.orange.ui.music.MusicSongListDetailActivity;
import com.andy.orange.utils.DisplayUtil;
import com.andy.orange.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MusicSongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCxt;
    private List<SongListInfo.ContentBean> mList;
    private LayoutInflater inflater;

    public MusicSongListAdapter(Context context, List<SongListInfo.ContentBean> mList) {

        this.mCxt=context;
        this.mList=mList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongListViewHolder(inflater.inflate(R.layout.item_song_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof SongListViewHolder) {

            SongListViewHolder songHolder= (SongListViewHolder) holder;

            final SongListInfo.ContentBean info = mList.get(position);

            int count = Integer.parseInt(info.getListenum());

            String name = info.getTitle();
            String url = info.getPic_300();

            songHolder.songName.setText(name);
            ImageLoaderUtils.display(mCxt,songHolder.songListPoster,url);
            if (count > 10000) {
                count = count / 10000;
                songHolder.songCount.setText(count+"万");
            }else {
                songHolder.songCount.setText(info.getListenum());
            }

            int width= DisplayUtil.getScreenWidth(mCxt);
            ViewGroup.LayoutParams params=songHolder.relativeLayout.getLayoutParams();
            params.width=width/2-40;
            songHolder.relativeLayout.setLayoutParams(params);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(mCxt,"你即将查看细节",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mCxt, MusicSongListDetailActivity.class);
                    intent.putExtra("songListId",info.getListid());
                    intent.putExtra("islocal", false);
                    intent.putExtra("songListPhoto", info.getPic_300());
                    intent.putExtra("songListname", info.getTitle());
                    intent.putExtra("songListTag", info.getTag());
                    intent.putExtra("songListCount", info.getListenum());
                    mCxt.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mList.size()-2;
    }

    public class SongListViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayout;
        private ImageView songListPoster;
        private TextView songCount;
        private TextView songName;

        public SongListViewHolder(View itemView) {
            super(itemView);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.RLayout_song_list_item);
            songListPoster= (ImageView) itemView.findViewById(R.id.song_list_image);
            songCount= (TextView) itemView.findViewById(R.id.song_list_count);
            songName= (TextView) itemView.findViewById(R.id.song_list_name );


        }
    }
}
