package com.andy.orange.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andy.orange.R;

/**
 * Created by Administrator on 2017/9/30.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private OnShareItemClickListener shareItemClickListener;
    private boolean mCancelable=true;
    private OnCancelListener mCancelListener;

    public ShareDialog(Context context) {

        super(context);
        mContext=context;

    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    /*
    * 初始化分享按钮的时候直接传入选项的单击响应
    * */
    public ShareDialog(Context context, int themeResId,OnShareItemClickListener shareItemClickListener) {
        super(context, themeResId);
        mContext=context;
        this.shareItemClickListener=shareItemClickListener;
    }

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        mCancelable=cancelable;
        mCancelListener=cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=view = View.inflate(mContext, R.layout.dialog_share, null);
        setContentView(view);

        ViewGroup fendLayout= (ViewGroup) view.findViewById(R.id.fend_layout);
        ViewGroup wxLayout= (ViewGroup) view.findViewById(R.id.wx_layout);
        ViewGroup zoneLayout= (ViewGroup) view.findViewById(R.id.zone_layout);
        ViewGroup qqLayout= (ViewGroup) view.findViewById(R.id.qq_layout);
        ViewGroup weiboLayout= (ViewGroup) view.findViewById(R.id.weibo_layout);
        Button cancel= (Button) view.findViewById(R.id.btn_share_cancel);

        //设置监听
        fendLayout.setOnClickListener(this);
        wxLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        weiboLayout.setOnClickListener(this);
        zoneLayout.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Window window=getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams params=window.getAttributes();
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity= Gravity.BOTTOM;
        window.setAttributes(params);

    }

    @Override
    public void onClick(View v) {
        shareItemClickListener.onItemClick(v);
    }

    /*
    * 提供外部shareDialog设置监听器
    * */
    public void setShareItemClickListener(OnShareItemClickListener listener){
        shareItemClickListener=listener;
    }

    public interface OnShareItemClickListener{
        void onItemClick(View view);
    }


}
