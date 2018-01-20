package com.andy.orange.ui;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.utils.SharedPreferencesUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/10.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_close)
    ImageView closeImageView;

    @BindView(R.id.login_username)
    EditText userNameText;

    @BindView(R.id.login_password)
    EditText passwordText;

    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_login;

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mContext=this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesUtils.setSharedData(mContext,"is_login",true,"boolean");
                SharedPreferencesUtils.setSharedData(mContext,"nick_name",userNameText.getText().toString(),"string");
                SharedPreferencesUtils.setSharedData(mContext,"login_pass",passwordText.getText().toString(),"string");
                SharedPreferencesUtils.setSharedData(mContext,"login_date", System.currentTimeMillis(),"long");

                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
