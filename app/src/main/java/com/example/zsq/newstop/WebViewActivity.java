package com.example.zsq.newstop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by zsq on 16-8-19.
 */
public class WebViewActivity extends Activity{
    public final static String URL_EXTRA = "URL";
    public final static String TITLE_EXTRA = "Title";
    private NewsWebView mWebView;
    private WebSettings settings;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView titleTv;
    private ImageView backIcon;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String Url = getIntent().getStringExtra(URL_EXTRA);
        title = getIntent().getStringExtra(TITLE_EXTRA);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        mWebView = (NewsWebView) findViewById(R.id.content_webview);
        mWebView.setOnScrollChangedCallback(new NewsWebView.OnScrollChangedCallback() {
            private static final int HIDE_THRESHOLD=150;
            private int scrollDistance=0;
            private boolean controlsVisible=true;
            @Override
            public void onScroll(int dx, int dy) {
                if(scrollDistance>HIDE_THRESHOLD&&controlsVisible&&dy>15){
                    hideToolBar();
                    controlsVisible=false;
                    scrollDistance=0;
                } else if (scrollDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    showToolBar();
                    controlsVisible=true;
                    scrollDistance=0;
                }
                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrollDistance+=dy;
                }
            }
        });
        backIcon = (ImageView) findViewById(R.id.webview_back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        Animation sclae2 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.25f, 0.5f, 0.0f);
        sclae2.setFillAfter(true);
        toolbar.setAnimation(sclae2);
        progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        Animation animation = new TranslateAnimation(0, 0, -350, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(2000);
        settings=mWebView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setJavaScriptEnabled(true);
        titleTv = (TextView) findViewById(R.id.webview_title_tv);
        new setTitleTask().execute();
        mWebView.loadUrl(Url);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
//                mWebView.loadUrl("javascript:document.body.style.paddingTop=\"%\"; void 0");
            }
        });
        mWebView.setPadding(0, 120, 0, 0);
        Animation sclae = new ScaleAnimation(1.0f, 1.0f, 0.25f, 1.0f, 0.5f, 0.0f);
        sclae.setDuration(1000);
        sclae.setFillAfter(true);
        toolbar.setAnimation(sclae);
        toolbar.setTitle("标题");
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_slide_exit,R.anim.back_slide_enter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideToolBar() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showToolBar() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private class setTitleTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            AnimationSet animationSet = new AnimationSet(true);
            AlphaAnimation alpha = new AlphaAnimation(0.0f,1.0f);
            alpha.setFillAfter(true);
            alpha.setDuration(800);
            Log.i("WebView", "title"+title);
            titleTv.setText(title);
            titleTv.setTextColor(getResources().getColor(R.color.white));
            titleTv.setAnimation(alpha);
        }
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
