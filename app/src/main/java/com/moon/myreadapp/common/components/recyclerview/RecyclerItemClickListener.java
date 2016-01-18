package com.moon.myreadapp.common.components.recyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.moon.appframework.common.log.XLog;

/**
 * Created by moon on 15/11/7.
 *
 * 使用GestureDetector接管目标view 的touch事件
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private OnItemClickListener mListener;
    private boolean canelAction;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    private RecyclerView mView;
    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mView != null){
                    View childView = mView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mListener != null ) {
                        mListener.onItemClick(childView, mView.getChildPosition(childView));
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //XLog.d("GestureDetector  loLongPress");
                if (mView != null){
                    View childView = mView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mListener != null ) {

                        //当touch事件没有被cancel时才触发,否则可能出现bug
                        if (!canelAction) {
                            mListener.onItemLongClick(childView, mView.getChildPosition(childView));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        //XLog.d("onInterceptTouchEvent : 0 down;1 up;2 move:" + e.getAction());
        mView = view;
        if (e.getAction() == MotionEvent.ACTION_CANCEL){
            //当取消touch的时候需要标记,用来取消GestureDetector的onLongPress
            canelAction = true;
        } else {
            canelAction = false;
        }
        return mGestureDetector.onTouchEvent(e);
    }


    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
}