package com.example.zsq.newstop;

import android.support.v7.widget.RecyclerView;

/**
 * Created by zsq on 16-8-27.
 */
public abstract class MaterialListScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD=20;
    private int scrollDistance=0;
    private boolean controlsVisible=true;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(scrollDistance>HIDE_THRESHOLD&&controlsVisible&&dy>15){
            onHide();
            controlsVisible=false;
            scrollDistance=0;
        } else if (scrollDistance < -HIDE_THRESHOLD && !controlsVisible&&dy<-30) {
            onShow();
            controlsVisible=true;
            scrollDistance=0;
        }
        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrollDistance+=dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();
}
