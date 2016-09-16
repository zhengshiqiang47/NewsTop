package com.example.zsq.newstop;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by zsq on 16-8-29.
 */
public class NewsJSONSerializer {
    private Context mContext;
    private String mFilename;

    public NewsJSONSerializer(Context context, String f) {
        mContext = context;
        mFilename=f;
    }

    public void saveNewses(ArrayList<News> newses) {
        JSONArray array = new JSONArray();
        for (News news : newses) {
            try {
                array.put(news.toJSON());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("NewsJSON", "JsonArray大小" + array.length());
        Writer writer = null;
        try {
            OutputStream out = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), mFilename));
                Log.i("NewesJSON","path"+Environment.getExternalStorageDirectory()+mFilename);
            }else{
                out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                Log.i("NewesJSON","path"+mFilename);
            }

            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
            Log.i("NewesJSON", "写入成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public ArrayList<News> loadNewes() throws IOException, JSONException {
        ArrayList<News> newses = new ArrayList<News>();
        BufferedReader reader = null;
        InputStream in=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            in=new FileInputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath(),mFilename));
            Log.i("JSonSerializer", "LoadingFile");
        }else
            in= mContext.openFileInput(mFilename);
        reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder jsonString = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
        for(int i=0;i<jsonArray.length();i++) {
            newses.add(new News(jsonArray.getJSONObject(i)));
        }
        Log.i("JSonSerializer", "加载成功");

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newses;
    }
}
