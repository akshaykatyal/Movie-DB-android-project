package com.example.jeevan.moviedatabase.utility;

/**
 * Created by jeevan on 20/3/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener, RecyclerView
            view) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector
                .SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if(childView instanceof NetworkImageView)
        {
            Log.e("image view","true");
        }
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            if (view.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}