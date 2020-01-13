package com.bill.marqueeview;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Bill on 2020/1/13.
 * Describe ：
 */
public class RecyclerViewSlider implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener onItemClickListener;

    public RecyclerViewSlider() {

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private float downX, downY;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Bill", downX + "|" + downY + "||" + event.getX() + "|" + event.getY());

                if (Math.abs(event.getX() - downX) < 5 && Math.abs(event.getY() - downY) < 5) {
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                View childView = rv.findChildViewUnder(event.getX(), event.getY());
                int position = -1;
                if (childView != null)
                    position = rv.getChildLayoutPosition(childView);

                Log.e("Bill", "-----------up--------:" + position);

                if (position >= 0 && onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
                break;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
