package com.example.zsq.newstop;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zsq on 16-8-25.
 */
public class JSONAnalyze {
    private static final String TAG = "JSONAnalyze";

    public static boolean getNews(String type, Context context) {
        ArrayList<News> newses=NewsLab.get(context).getNews(type);
        Log.i(TAG, "size:" + newses.size());
        if (newses == null) {
            Log.i(TAG, "newes为空"+type);
        }
        JSONObject resultObj = GetAPI.getRequest(type);
        int count=0;
        try {
            JSONArray dataArray = resultObj.getJSONArray("data");
            Log.i(TAG, "成功获取，数据为:" + resultObj);
            for (int i = 0; i < dataArray.length(); i++) {
                count++;
                try {
                    JSONObject mNews = dataArray.getJSONObject(i);
                    String title = mNews.getString("title");
                    String date = mNews.getString("date");
                    String author_name = mNews.getString("author_name");
                    String url = mNews.getString("url");
                    String pic = mNews.getString("thumbnail_pic_s");
                    String mType;
                    if (type.equals("top")) {
                        mType = mNews.getString("type");
                    }else {
                        mType = mNews.getString("category");
                    }
                    News news = new News();
                    news.setTitle(title);
                    news.setDate(date);
                    news.setAutorName(author_name);
                    news.setUrl(url);
                    news.setPic_1(pic);news.setPic_2(pic);news.setPic_3(pic);
                    news.setmType(type);
                    newses.add(news);
                } catch (Exception e) {
                    Log.i(TAG, "单个解析出错");
                }
            }
            Log.i(TAG, "count" + count);
            return true;
        } catch (Exception e) {
            Log.i(TAG, "解析出错");
        }
        return false;
    }
}
