package com.example.zsq.newstop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by zsq on 16-8-25.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private SwipeRefreshLayout refreshLayout;
    private MaterialListView materialListView;
    private String type;
    private ArrayList<News> newses;
    private Toolbar toolbar;
    private HomeFragmentCallBack fragmentCallBack;
    private ImageView refreshIcon;
    private TextView tongzhilan;
    private TextView toolbarTitle;
    private MaterialDialog materialDialog;

    public interface HomeFragmentCallBack{
        public abstract void snackBar(String text);
    }

    public static HomeFragment newInstance(String type) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("type");
        newses=NewsLab.get(getActivity()).getNews(type);
        new FetchNews().execute(type);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int writePermission= ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission!= PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        View v = inflater.inflate(R.layout.fragment_top, container, false);
        materialListView = (MaterialListView) v.findViewById(R.id.top_list);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.top_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.red,
                R.color.light_blue_1,
                R.color.light_blue_3);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newses.clear();
                        if(!JSONAnalyze.getNews(type, getActivity())){
                            mHandler.sendEmptyMessage(2);
                        }else {
                            newses = NewsLab.get(getActivity()).getNews(type);
                            mHandler.sendEmptyMessage(1);
                        }

                    }
                }).start();
            }
        });
        tongzhilan = (TextView) v.findViewById(R.id.tongzhilan_main);
        toolbar = (Toolbar) v.findViewById(R.id.main_toolbar);

        toolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
        switch (type) {
            case "top":
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tongzhilan.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                toolbarTitle.setText("今日热点");
                break;
            case "keji":
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tongzhilan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbarTitle.setText("科技");
                break;
            case "yule":
                toolbar.setBackgroundColor(getResources().getColor(R.color.fense_dark));
                tongzhilan.setBackgroundColor(getResources().getColor(R.color.fense_dark));
                toolbarTitle.setText("娱乐");
                break;
            case "tiyu":
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple_dark_2));
                tongzhilan.setBackgroundColor(getResources().getColor(R.color.purple_dark_2));
                toolbarTitle.setText("体育");
                break;
            case "guoji":
                toolbar.setBackgroundColor(getResources().getColor(R.color.red_dark));
                tongzhilan.setBackgroundColor(getResources().getColor(R.color.red_dark));
                toolbarTitle.setText("国际");
                break;
        }
        refreshIcon = (ImageView) v.findViewById(R.id.refresh_icon_top);
        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation rotate = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotate.setDuration(1000);
                refreshIcon.startAnimation(rotate);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newses.clear();
                        Log.i(TAG, "refresh");
                        if(!JSONAnalyze.getNews(type, getActivity())){
                            mHandler.sendEmptyMessage(2);
                        }else {
                            newses = NewsLab.get(getActivity()).getNews(type);
                            mHandler.sendEmptyMessage(1);
                        }

                    }
                }).start();

            }
        });
        materialListView.setItemAnimator(new SlideInLeftAnimator());
        materialListView.getItemAnimator().setAddDuration(300);
        materialListView.getItemAnimator().setRemoveDuration(300);
        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(@NonNull Card card, int position) {
                String url = card.getTag().toString();
                String title = NewsLab.get(getActivity()).getNews(type).get(position).getTitle();
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_EXTRA, url);
                intent.putExtra(WebViewActivity.TITLE_EXTRA, title);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

            }

            @Override
            public void onItemLongClick(@NonNull Card card, final int position) {
                materialDialog = new MaterialDialog(getActivity())
                        .setTitle("收藏")
                        .setMessage("是否收藏:\n"+newses.get(position).getTitle()+"?")
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                collectNews(newses.get(position));
                            }
                        }).setNegativeButton("否", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                collectNews(null);
                            }
                        });
                materialDialog.show();
            }
        });
        materialListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {

            }
        });

        return v;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            showToolBar();
        }
        super.onHiddenChanged(hidden);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLayout.setRefreshing(false);
                    ((HomeFragmentCallBack)getActivity()).snackBar("刷新成功");
                    addNewsAndUpdate();
                    break;
                case 2:
                    refreshLayout.setRefreshing(false);
                    ((HomeFragmentCallBack)getActivity()).snackBar( "刷新失败，请检查网络并重试");
                    break;
                default:
                    break;
            }
        }
    };

//    private class NewsAdapter extends ArrayAdapter<News> {
//        private TextView titleTv;
//        private TextView dateTv;
//        private ImageView imageView;
//        private TextView autorTv;
//        private CardView cardView;
//
//        public NewsAdapter(ArrayList<News> list){
//            super(getActivity(),0,list);
//        }
//
//
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater().inflate(R.layout.news_item, null);
//            }
//            News news = getItem(position);
//            cardView = (CardView) convertView.findViewById(R.id.notification_card);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    News news=(News) getItem(position);
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                    intent.putExtra("URL", news.getUrl());
//                    intent.putExtra("TITLE", news.getTitle());
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.slide_enter,R.anim.slide_exit);
//                }
//            });
//            titleTv = (TextView) convertView.findViewById(R.id.news_title_tv);
//            titleTv.setText(news.getTitle());
//            dateTv = (TextView) convertView.findViewById(R.id.news_date);
//            dateTv.setText(news.getDate());
//            imageView = (ImageView) convertView.findViewById(R.id.news_pic);
//            autorTv = (TextView) convertView.findViewById(R.id.news_author_tv);
//            autorTv.setText(news.getAutorName());
//            return convertView;
//        }
//
//    }


    @Override
    public void onPause() {
        super.onPause();
        MyCollectionLB.get(getActivity()).saveNewes();
    }

    private void addNewsAndUpdate() {
        newses=NewsLab.get(getActivity()).getNews(type);
        materialListView.getAdapter().clear();
        for(int i=0;i<newses.size();i++) {
            News news = newses.get(i);
            String title=new String();
            title += ("" + news.getTitle());
            if(title.length()>25){
                title = title.substring(0, 24);
                title += ".....";
            }
            Card card = new Card.Builder(getActivity())
                    .setTag(news.getUrl())
                    .setDismissible()
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.card_list_item)
                    .setDescription(title)
                    .setDescriptionGravity(Gravity.START)
                    .setDescriptionResourceColor(R.color.black_button)
                    .setDrawable(news.getPic_1())
                    .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                        @Override
                        public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                            requestCreator.fit();
                        }
                    })
                    .addAction(R.id.left_text, new TextViewAction(getActivity())
                            .setText(news.getDate())
                            .setTextResourceColor(R.color.gray))
                    .addAction(R.id.right_text, new TextViewAction(getActivity())
                            .setText(news.getAutorName())
                            .setTextResourceColor(R.color.orange_button))
                    .endConfig()
                    .build();
            materialListView.getAdapter().add(card);

        }
        materialListView.getAdapter().notifyDataSetChanged();
        materialListView.scrollToPosition(0);
        materialListView.setOnScrollListener(new MaterialListScrollListener() {
            @Override
            public void onHide() {
                hideToolBar();
            }

            @Override
            public void onShow() {
                showToolBar();
            }
        });

    }

    private class FetchNews extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strs) {
            JSONAnalyze.getNews(strs[0], getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            adapter.notifyDataSetChanged();
            addNewsAndUpdate();
        }
    }

    private void hideToolBar() {
        toolbar.animate().translationY(-toolbar.getHeight()-100).setInterpolator(new AccelerateInterpolator(2));
        tongzhilan.animate().translationY(-tongzhilan.getHeight()-150).setInterpolator(new AccelerateInterpolator(4));
    }

    private void showToolBar() {
        tongzhilan.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private void collectNews(News news) {
        materialDialog.dismiss();
        if (news == null) {
            return;
        }
        MyCollectionLB.get(getActivity()).getCollectionNewses().add(news);
        ((HomeFragmentCallBack) getActivity()).snackBar("收藏成功");
    }
}
