package com.andy.orange.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.bean.MusicMeItem;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP=0;
    private static final int TYPE_MIDDLE=1;
    private static final int TYEP_BOTTOM=2;

    private Context cxt;
    private List<MusicMeItem> mList;
    private LayoutInflater inflater;

    public MusicMeAdapter(Context mCxt, List<MusicMeItem> mList) {

        this.cxt=mCxt;
        this.mList=mList;
        this.inflater=LayoutInflater.from(mCxt);

    }

    @Override
    public int getItemViewType(int position) {
        if (position<=3){
            return TYPE_TOP;
        }else if (position==4){
            return TYPE_MIDDLE;
        }else {
            return TYEP_BOTTOM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_TOP:
                return new ItemTopViewHolder(inflater.inflate(R.layout.item_music_top,parent,false));
            case TYPE_MIDDLE:
                return new ItemMiddleViewHolder(inflater.inflate(R.layout.item_music_middle,parent,false));
            case TYEP_BOTTOM:
                return new ItemBottomViewHolder(inflater.inflate(R.layout.item_music_bottom,parent,false));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemTopViewHolder){
            setTopItemValues((ItemTopViewHolder) holder,position);
        }else if(holder instanceof ItemBottomViewHolder){
            setBottomValues((ItemBottomViewHolder)holder,position);
        }
    }

    private void setTopItemValues(ItemTopViewHolder holder,int pos) {

        MusicMeItem item=mList.get(pos);
        int count=item.getCount();
        Drawable res=item.getImageRes();
        String title=item.getTitle();

        holder.count.setText("("+count+")");
        holder.typeImage.setImageDrawable(res);
        holder.typeName.setText(title);
    }

    private void setBottomValues(ItemBottomViewHolder holder,int pos){
        MusicMeItem item=mList.get(pos-1);
        int count=item.getCount();
        Drawable res=item.getImageRes();
        String title=item.getTitle();

        holder.listTitle.setText(title);
        holder.listCount.setText(count+"首");
        holder.listImage.setImageDrawable(res);

        holder.listMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cxt,"点击了扩展菜单",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size()+1;
    }


    public class ItemTopViewHolder extends RecyclerView.ViewHolder {

        private ImageView typeImage;
        private TextView typeName;
        private TextView count;

        public ItemTopViewHolder(View itemView) {
            super(itemView);

            typeImage= (ImageView) itemView.findViewById(R.id.type_image);
            typeName= (TextView) itemView.findViewById(R.id.type_name);
            count= (TextView) itemView.findViewById(R.id.type_count);
        }
    }

    public class ItemMiddleViewHolder extends RecyclerView.ViewHolder {

        public ItemMiddleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemBottomViewHolder extends RecyclerView.ViewHolder {

        private ImageView listImage;
        private ImageView listMenu;
        private TextView listTitle;
        private TextView listCount;

        public ItemBottomViewHolder(View itemView) {
            super(itemView);
            listImage= (ImageView) itemView.findViewById(R.id.music_list_imageview);
            listTitle= (TextView) itemView.findViewById(R.id.tv_music_list_title);
            listCount= (TextView) itemView.findViewById(R.id.tv_music_list_count);
            listMenu= (ImageView) itemView.findViewById(R.id.music_list_menu_expand);

        }
    }
}
