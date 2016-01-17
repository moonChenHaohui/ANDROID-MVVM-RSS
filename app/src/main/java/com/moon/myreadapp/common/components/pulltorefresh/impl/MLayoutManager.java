package com.moon.myreadapp.common.components.pulltorefresh.impl;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by moon on 16/1/17.
 * 使得smoothScrollToPosition能够按照将position View滑动到顶部
 */
public class MLayoutManager extends LinearLayoutManager {

    public MLayoutManager(Context context) {
        super(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       final RecyclerView.State state, final int position) {

        int fcvip = findFirstCompletelyVisibleItemPosition();
        int lcvip = findLastCompletelyVisibleItemPosition();
        if (position < fcvip || lcvip < position) {
            // scrolling to invisible position
            View fcvipv = findViewByPosition(fcvip);
            View lcvipv = findViewByPosition(lcvip);
            if (fcvipv == null || lcvipv == null) return;
            float fcviY = fcvipv.getY();
            float lcviY = lcvipv.getY();

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

                int currentState = RecyclerView.SCROLL_STATE_IDLE;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    if (currentState == RecyclerView.SCROLL_STATE_SETTLING
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {

                        // recursive scrolling
                        smoothScrollToPosition(recyclerView, state, position);
                    }

                    currentState = newState;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    int fcvip = findFirstCompletelyVisibleItemPosition();
                    int lcvip = findLastCompletelyVisibleItemPosition();

                    if ((dy < 0 && fcvip == position) || (dy > 0 && lcvip == position)) {
                        // stop scrolling
                        recyclerView.setOnScrollListener(null);
                    }
                }
            });

            if (position < fcvip) {
                // scroll up

                recyclerView.smoothScrollBy(0, (int) (fcviY - lcviY));
            } else {
                // scroll down

                recyclerView.smoothScrollBy(0, (int) (lcviY - fcviY));
            }
        } else {
            // scrolling to visible position

            float fromY = findViewByPosition(fcvip).getY();
            float targetY = findViewByPosition(position).getY();

            recyclerView.smoothScrollBy(0, (int) (targetY - fromY));
        }
    }
}