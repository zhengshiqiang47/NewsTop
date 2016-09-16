package com.example.zsq.newstop;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by zsq on 16-8-28.
 */
public class NewsWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public NewsWebView(Context context) {
        super(context);
    }

    public NewsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewsWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }
}
