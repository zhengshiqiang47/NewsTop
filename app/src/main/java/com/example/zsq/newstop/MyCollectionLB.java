package com.example.zsq.newstop;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zsq on 16-8-29.
 */
public class MyCollectionLB {
    private static final String FileName="MyCollection.json";
    private ArrayList<News> newses;
    private static MyCollectionLB collectionLb;
    private Context mAppContext;
    private NewsJSONSerializer newsJSONSerializer;

    private MyCollectionLB(Context context) {
        mAppContext = context;
        newsJSONSerializer = new NewsJSONSerializer(mAppContext, FileName);
        try {
            newses = newsJSONSerializer.loadNewes();
            Log.i("MyCollection", "加载成功");
        } catch (Exception e) {
            Log.e("MyCollection", "加载失败");
            newses = new ArrayList<News>();
        }
    }

    public static MyCollectionLB get(Context context) {
        if (collectionLb == null) {
            collectionLb = new MyCollectionLB(context);
        }
        return collectionLb;
    }

    public ArrayList<News> getCollectionNewses() {
        return newses;
    }

    public boolean saveNewes() {
        try {
            newsJSONSerializer.saveNewses(newses);
            return true;
        } catch (Exception e) {
            Log.e("MyCollection", "Error saving News",e);
            return false;
        }
    }
}
