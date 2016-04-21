package com.perasia.recycleviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private static final String TAG = BaseRecycleViewAdapter.class.getSimpleName();

    protected static final int TYPE_HEADER = -1;
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_FOOTER = 1;

    protected boolean hasFooter;

    protected Context mContext;

    protected ArrayList<T> mList = new ArrayList<>();

    protected OnRecycleViewItemClickListener itemClickListener;

    protected RequestLoadMoreListener mRequestLoadMoreListener;

    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public void setOnLoadMoreListener(int pageSize, RequestLoadMoreListener requestLoadMoreListener) {
        if (getItemCount() < pageSize) {
            return;
        }
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public interface OnRecycleViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener l) {
        this.itemClickListener = l;
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent);

    public abstract void onBindItemViewHolder(final VH holder, int position);

    public BaseRecycleViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return onCreateItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
            if (hasFooter && mRequestLoadMoreListener != null) {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }

        onBindItemViewHolder((VH) holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getBasicItemCount() && hasFooter) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }


    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        if (this.hasFooter != hasFooter) {
            this.hasFooter = hasFooter;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + (hasFooter ? 1 : 0);
    }

    protected int getBasicItemCount() {
        return mList.size();
    }

    private T getItem(int position) {
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    public List<T> getList() {
        return mList;
    }

    public void append(T t) {
        if (null == t) {
            return;
        }
        mList.add(t);
    }

    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position < mList.size() - 1 && position >= 0) {
            mList.remove(position);
        }
    }

    public void clear() {
        mList.clear();
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
