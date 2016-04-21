package com.perasia.recycleviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter extends BaseRecycleViewAdapter {

    private Context mContext;
    private ArrayList<String> datas;

    public MainAdapter(Context context, ArrayList<String> datas) {
        super(context);
        mContext = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new MyRecycleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycle_view_item, parent, false));
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyRecycleViewHolder myRecycleViewHolder = (MyRecycleViewHolder) holder;
        myRecycleViewHolder.titleTv.setText(datas.get(position));
    }

    public static class MyRecycleViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;

        public MyRecycleViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.recycleview_item_title_tv);
        }
    }
}
