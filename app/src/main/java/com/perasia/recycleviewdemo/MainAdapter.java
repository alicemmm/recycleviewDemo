package com.perasia.recycleviewdemo;


import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter extends BaseRecycleViewAdapter {

    private ArrayList<String> datas;

    public MainAdapter(Context context, ArrayList<String> datas) {
        super(context);
        this.datas = datas;
    }

    @Override
    public int getItemResource() {
        return R.layout.recycle_view_item;
    }

    @Override
    public void onBindItemViewHolder(BaseViewHolder holder, int position) {
        TextView titleTv = holder.getView(R.id.recycleview_item_title_tv);
        titleTv.setText(datas.get(position));
    }
}
