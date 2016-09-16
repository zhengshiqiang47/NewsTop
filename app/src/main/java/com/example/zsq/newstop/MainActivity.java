package com.example.zsq.newstop;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentCallBack{
    private CoordinatorLayout coordinatorLayout;
    private TextView toolbarTitle;
    private Toolbar toolbar;
    private HomeFragment from;
    private Fragment mContent;
    private HomeFragment yuleFragment;
    private HomeFragment kejiFragment;
    private HomeFragment tiyuFragment;
    private HomeFragment quanqiuFragment;
    private HomeFragment homeFragment;
    private HomeFragment to;
    private CollectionFragment collectionFragment;
    private FragmentManager fm = getSupportFragmentManager();
    private BottomNavigationBar bottomNavigationBar;
    
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        int writePermission= ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission!= PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_white_48dp, "首页").setActiveColor(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.ic_computer_white_48dp, "科技").setActiveColor(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_movie_white_48dp, "影音娱乐").setActiveColor(R.color.fense_dark))
                .addItem(new BottomNavigationItem(R.mipmap.ic_directions_bike_white_48dp, "体育").setActiveColor(R.color.purple_dark_2))
                .addItem(new BottomNavigationItem(R.mipmap.ic_public_white_48dp, "国际").setActiveColor(R.color.red_dark))
                .addItem(new BottomNavigationItem(R.mipmap.ic_person_white_48dp, "我的").setActiveColor(R.color.dark_green))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        switchContent(mContent,homeFragment);
                        break;
                    case 1:
                        if (kejiFragment == null) {
                            kejiFragment = HomeFragment.newInstance("keji");
                        }
                        switchContent(mContent,kejiFragment);
                        break;
                    case 2:
                        if (yuleFragment == null) {
                            yuleFragment = HomeFragment.newInstance("yule");
                        }
                        switchContent(mContent, yuleFragment);
                        break;
                    case 3:
                        if (tiyuFragment == null) {
                            tiyuFragment = HomeFragment.newInstance("tiyu");
                        }
                        switchContent(mContent,tiyuFragment);
                        break;
                    case 4:
                        if (quanqiuFragment == null) {
                            quanqiuFragment = HomeFragment.newInstance("guoji");
                        }
                        switchContent(mContent,quanqiuFragment);
                        break;
                    case 5:
                        if (collectionFragment == null) {
                            collectionFragment = new CollectionFragment();
                        }
                        switchContent(mContent, collectionFragment);
                        break;
                    default:
                        setDeafultFragment();
                }

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
//        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
//        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setDeafultFragment();
    }

    private void setDeafultFragment() {
        homeFragment= HomeFragment.newInstance("top");
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container,homeFragment);
//        toolbarTitle.setText("头条热点");
//        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        transaction.commit();
        mContent=homeFragment;
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(coordinatorLayout,"是否退出?",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void snackBar(String text) {
        Snackbar.make(coordinatorLayout,text,Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = fm.beginTransaction();
            mContent = to;
            transaction.setCustomAnimations(R.anim.slide_enter_fragment, R.anim.slide_exit_fragment);
            if (to != null) {
                if (!to.isAdded()) {    // 先判断是否被add过
                    transaction.hide(from).add(R.id.fragment_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
                }
            }
        }
    }

    public void hideBottom() {
        Log.i("MainActivity", "隐藏Bottom");
        bottomNavigationBar.animate().translationY(bottomNavigationBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void showBottom() {
        bottomNavigationBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }
}
