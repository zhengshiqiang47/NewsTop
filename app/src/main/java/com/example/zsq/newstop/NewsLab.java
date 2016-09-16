package com.example.zsq.newstop;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by zsq on 16-8-25.
 */
public class NewsLab {
    private static final String TAG = "NewsLab";

    private ArrayList<News> topnewses;
    private ArrayList<News> kejinewses;
    private ArrayList<News> yulenewses;
    private ArrayList<News> tiyunewses;
    private ArrayList<News> guojinewses;
    private static NewsLab newsLab;
    private Context mAppContext;
    private String mType;

    private NewsLab(Context context) {
        mAppContext = context;
        topnewses = new ArrayList<News>();
        kejinewses = new ArrayList<News>();
        yulenewses = new ArrayList<News>();
        tiyunewses = new ArrayList<News>();
        guojinewses = new ArrayList<News>();
    }

    public static NewsLab get(Context context) {
        if (newsLab == null) {
            newsLab = new NewsLab(context);
        }
        return newsLab;
    }

    public  ArrayList<News> getNews(String type) {
        mType=type;
        if (mType.equals("top")) {
            return topnewses;
        } else if (mType.equals("keji")) {
            return kejinewses;
        }else if (mType.equals("yule")) {
            return yulenewses;
        }else if (mType.equals("tiyu")) {
            return tiyunewses;
        }else if (mType.equals("guoji")) {
            return guojinewses;
        }
        return null;
    }

    public void clear(String type) {
        if (type.equals("top")) {
            topnewses.clear();
        } else if (type.equals("keji")) {
            kejinewses.clear();
        }else if (type.equals("yule")) {
            yulenewses.clear();
        }else if (type.equals("tiyu")) {
            tiyunewses.clear();
        }else if (type.equals("guoji")) {
            guojinewses.clear();
        }
    }
}
