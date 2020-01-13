package com.bill.marqueeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Bill on 2020/1/13.
 * Describe ：
 */
public class MarqueeViewAdapter extends RecyclerView.Adapter<MarqueeViewAdapter.ViewHolder> {

    private Context context;
    private List<String> mDataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MarqueeViewAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<String> dataList) {
        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_text, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int realPosition = 0;
        if (mDataList.size() != 0) {
            realPosition = position % mDataList.size();
        }
        holder.update(realPosition);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }

        public void update(final int position) {
            textView.setText(String.valueOf(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position);
                }
            });
        }

    }

}