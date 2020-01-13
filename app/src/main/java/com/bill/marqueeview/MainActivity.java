package com.bill.marqueeview;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements Runnable {

    private AppCompatTextView mTv;
    private RecyclerView mRv;
    private List<String> mDataList = new ArrayList<>();
    private MarqueeViewAdapter mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.text_view);
        mRv = findViewById(R.id.recycler_view);

        mAdapter = new MarqueeViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(mAdapter);

        RecyclerViewSlider slider = new RecyclerViewSlider();
        mRv.addOnItemTouchListener(slider);

        // 1，1和2二选一
        slider.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("Bill", "p:" + position);

                int realPosition = 0;
                if (mDataList.size() != 0) {
                    realPosition = position % mDataList.size();
                }
                mTv.setText(String.valueOf(realPosition));
            }
        });

        // 2，1和2二选一
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("Bill", "p:" + position);
                mTv.setText(String.valueOf(position));
            }
        });

        getData();
        mAdapter.setDataList(mDataList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void run() {
        mRv.scrollBy(3, 0);
        mHandler.postDelayed(this, 10);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(this, 10);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(this);
    }

    private void getData() {
        for (int i = 0; i < 5; i++) {
            mDataList.add(String.valueOf(i + 1));
        }
    }
}
