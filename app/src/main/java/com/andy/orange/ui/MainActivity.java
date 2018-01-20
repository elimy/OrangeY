package com.andy.orange.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.orange.R;
import com.andy.orange.adapter.MainRecyclerViewAdapter;
import com.andy.orange.anims.LandingAnimator;
import com.andy.orange.anims.ScaleInAnimationAdapter;
import com.andy.orange.base.BaseActivity;
import com.andy.orange.bean.Film;
import com.andy.orange.ui.coder.CoderActivity;
import com.andy.orange.ui.home.presenter.impl.MainPresenterImpl;
import com.andy.orange.ui.home.view.MainView;
import com.andy.orange.ui.movie.MovieDetailActivity;
import com.andy.orange.ui.movie.MovieDisplayActivity;
import com.andy.orange.ui.music.MusicActivity;
import com.andy.orange.utils.SharedPreferencesUtils;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by Andy Lau on 2017/8/8.
 */

public class MainActivity extends BaseActivity implements MainView,
        NavigationView.OnNavigationItemSelectedListener,OnRefreshListener,View.OnClickListener{

    private int count=24;
    private int start=0;
    private int randomStart= new Random().nextInt(300);
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.rv_content)
    IRecyclerView mIRecyclerView;
    @BindView(R.id.btn_message)
    ImageView mMsgBtn;

    private ImageView IvHeadImg;
    private TextView loginTv;
    private MainRecyclerViewAdapter mMainAdapter;
    private List<Film.SubjectsBean> mList = new ArrayList<>();
    private MainPresenterImpl mainPresenter;


    /*
    * 侧滑导航的选中监听
    * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //侧滑导航选中事件监听
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.menu_home:
                break;
            case R.id.menu_live:
                //启动直播页面
                Intent liveIntent=new Intent(MainActivity.this,LiveActivity.class);
                startActivity(liveIntent);
                break;
            case R.id.menu_duanzi:
                //启动搞笑段子页面
                Intent duanIntent=new Intent(MainActivity.this,DuanziActivity.class);
                startActivity(duanIntent);
                break;
            case R.id.menu_book:
                //启动搞笑段子页面
                Intent bookIntent=new Intent(MainActivity.this,BookActivity.class);
                startActivity(bookIntent);
                break;
            case R.id.menu_movie:
                //启动电影页面
                Intent intent = new Intent(MainActivity.this, MovieDisplayActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_music:
                //启动音乐页面

                Intent intent1=new Intent(MainActivity.this, MusicActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_info:
                //启动作者信息页

                Intent coderIntent=new Intent(MainActivity.this, CoderActivity.class);
                startActivity(coderIntent);
                break;
            case R.id.menu_about:
                //启动开源信息


                break;
            case R.id.menu_logout:
                //退出

                SharedPreferencesUtils.setSharedData(this,"is_login",false,"boolean");
                Intent restart=new Intent(MainActivity.this, MainActivity.class);
                startActivity(restart);
                break;
        }

        //关闭drawer
        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    * 继承自封装的BaseActivity
    * */
    @Override
    public int getChildLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mainPresenter=new MainPresenterImpl(this, this);
    }

    @Override
    public void initView() {

        //设置DrawerLayout状态栏颜色
        //SettingStatusBar.setStatusColorForDrawerLayout(this,mDrawerLayout,getResources().getColor(R.color.black),SettingStatusBar.DEFAULT_STATUS_BAR_ALPHA);
        mContext=this;
        setToolbar();
        setNavigationView();
        initData();

        mainPresenter.requestHotFilm(randomStart,count);
        randomStart=randomStart+count;
    }

    /*
   * 设置toolbar和与drawerLayout绑定
   * */
    private void setToolbar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();

        //菜单按钮可用
        actionBar.setHomeButtonEnabled(true);

        //回退按钮可用
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //将drawerLayout和toolbar绑定在一起
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.app_name,R.string.app_name);
        //初始化状态
        toggle.syncState();
        //设置drawerLayout的监听事件 打开/关闭
        mDrawerLayout.addDrawerListener(toggle);

        //去除侧滑菜单的遮罩层
        //mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        //初始化内容
        // mToolbar.setTitleTextColor(getResources().getColor(R.color.black));

        //设置右上角消息图标监听事件
        mMsgBtn.setOnClickListener(this);
    }


    /*
   * 设置导航菜单
   * */
    private void setNavigationView() {

        //设置图标显示他自己的颜色
        mNavigation.setItemIconTintList(null);

        //获取到侧滑菜单顶部
        View headerView=mNavigation.getHeaderView(0);

        //获取绑定头像和登录提示文本
        IvHeadImg= (ImageView) headerView.findViewById(R.id.header_icon);
        loginTv= (TextView) headerView.findViewById(R.id.nick_name);

        //设置导航监听
        mNavigation.setNavigationItemSelectedListener(this);

        setHomeDefaultState();

        //初始化登录状态
        initLoginState();
    }

    /**
     * 设置首页默认被选中的状态
     */
    private void  setHomeDefaultState(){
        Menu menu=mNavigation.getMenu();
        MenuItem item=menu.getItem(0).getSubMenu().getItem(0);
        item.setChecked(true);
    }

    /*
   * 初始化登录状态
   * */
    private void initLoginState() {

        //获取sharedPreference的登录状态
        Boolean isLogin=false;
        isLogin= (Boolean) SharedPreferencesUtils.readSharedData(mContext,"is_login","boolean");

        if (null !=isLogin && isLogin){

            //获取上次登录时间
            long date= (long) SharedPreferencesUtils.readSharedData(this,"login_date","long");

            //大于一周设置设置登录状态为false
            if (System.currentTimeMillis()-date>7*24*3600*1000){
                SharedPreferencesUtils.setSharedData(this,"is_login",false,"boolean");

                isLogin=false;
            }
        }

        if (null !=isLogin && isLogin){
            IvHeadImg.setImageResource(R.mipmap.head_image);
            String username= (String) SharedPreferencesUtils.readSharedData(mContext,"nick_name","string");
            loginTv.setText(username);
        }else {
            //Toast.makeText(mContext,"未等录 " ,Toast.LENGTH_SHORT).show();
            loginTv.setText("点击登录");

            IvHeadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //启动登录界面

                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);

                    //Toast.makeText(MainActivity.this,"login",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    /*
    * 初始化数据
    * */
    private void initData() {

        //初始化适配器
        mMainAdapter = new MainRecyclerViewAdapter(mContext, mList);

        //设置布局
        GridLayoutManager manager=new GridLayoutManager(mContext,2,GridLayoutManager.VERTICAL,false);

        //设置item跨度
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==0||position==1||position==mList.size()+2){
                    return 2;
                }
                return 1;
            }
        });

        //绑定布局管理器和刷新监听器
        mIRecyclerView.setLayoutManager(manager);
        mIRecyclerView.setOnRefreshListener(this);

        //MainRecyclerViewAdapter开放setOnItemClickListener()方法
        mMainAdapter.setOnItemClickListener(new MainRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, ImageView imageView) {

                //启动电影细节页
                Intent intent=new Intent(MainActivity.this, MovieDetailActivity.class);

                intent.putExtra("movieId",mList.get(position).getId());
                startActivity(intent);
            }
        });

        //设置recyclerview动画
        mIRecyclerView.setItemAnimator(new LandingAnimator());
        //设置适配器
        mIRecyclerView.setIAdapter(new ScaleInAnimationAdapter(mMainAdapter));
    }


    @Override
    public void onRefresh() {
        mIRecyclerView.setRefreshing(true);
        mainPresenter.requestHotFilm(randomStart,count);
        randomStart=randomStart+count;

    }

    /*
    * 继承自MainRecyclerViewAdapter.OnItemClickListener接口的方法
    * */

    @Override
    public void getFilmInfos(Film film) {

        mIRecyclerView.setRefreshing(false);
       /* mList.clear();*/

        for (int i=0;i<film.getSubjects().size();i++) {
            mList.add(film.getSubjects().get(i));
        }
        mMainAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_message:
                Toast.makeText(mContext,"查看推送消息!",Toast.LENGTH_LONG).show();
                Log.d("log","查看推送消息");
                break;
            default:
                break;
        }
    }
}

