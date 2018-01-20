package com.andy.orange.ui.music.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.andy.orange.R;
import com.andy.orange.base.BaseFragment;
import com.andy.orange.utils.ImageLoaderUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/4.
 */

public class RoundFragment extends BaseFragment {

    @BindView(R.id.circle_poster)
    ImageView mCirclePoster;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_round;
    }

    @Override
    protected void initView() {
        if (getArguments()!=null){
            Bundle bundle=getArguments();
            String posterUrl=bundle.getString("posterUrl");
            ImageLoaderUtils.displayRound(getActivity(),mCirclePoster,posterUrl);
        }
    }
}
