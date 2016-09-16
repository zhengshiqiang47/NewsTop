package com.example.zsq.newstop;

import net.sf.json.util.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsq on 16-8-25.
 */
public class News {
    private static final String JSON_TITLE = "NewsTitle";
    private static final String JSON_Date = "NewsDate";
    private static final String JSON_AUTORName = "NewsAuthorName";
    private static final String JSON_URL = "NewsUrl";
    private static final String JSON_PIC = "NewsPic";
    private static final String JSON_TYPE = "NewsType";

    private String title;
    private String date;
    private String autorName;
    private String url;
    private String pic_1;
    private String pic_2;
    private String pic_3;
    private String mType;

    public News(JSONObject json) throws JSONException {
        title = json.getString(JSON_TITLE);
        date = json.getString(JSON_Date);
        autorName = json.getString(JSON_AUTORName);
        url = json.getString(JSON_URL);
        pic_1 = pic_2 = pic_3 = json.getString(JSON_PIC);
        mType = json.getString(JSON_TYPE);
    }

    public News() {

    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }

    private String realType;

    public String getPic_1() {
        return pic_1;
    }

    public void setPic_1(String pic_1) {
        this.pic_1 = pic_1;
    }

    public String getPic_2() {
        return pic_2;
    }

    public void setPic_2(String pic_2) {
        this.pic_2 = pic_2;
    }

    public String getPic_3() {
        return pic_3;
    }

    public void setPic_3(String pic_3) {
        this.pic_3 = pic_3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAutorName() {
        return autorName;
    }

    public void setAutorName(String autorName) {
        this.autorName = autorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String mDate = date.substring(5);
        this.date = mDate;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_TITLE, title);
        json.put(JSON_Date, date);
        json.put(JSON_AUTORName, autorName);
        json.put(JSON_PIC, pic_1);
        json.put(JSON_URL, url);
        json.put(JSON_TYPE, mType);
        return json;
    }

}
