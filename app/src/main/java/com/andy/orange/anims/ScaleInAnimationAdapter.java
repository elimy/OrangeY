package com.andy.orange.anims;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andy.orange.adapter.MovieAdapter;
import com.andy.orange.adapter.MusicSongListAdapter;


/**
 * Created by Andy Lau on 2017/8/11.
 */

public class ScaleInAnimationAdapter extends AnimationAdapter {

    private static final float DEFAULT_SCALE_FROM=.5f;
    private float mFrom;

    public ScaleInAnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this(adapter,DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, float from) {
        super(adapter);
        mFrom=from;
    }

    @Override
    protected Animator[] getAnimators(View view) {

        ObjectAnimator scaleX=ObjectAnimator.ofFloat(view,"scaleX",mFrom,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);

        return new Animator[]{scaleX,scaleY};
    }
}
