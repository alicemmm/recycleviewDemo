package com.perasia.recycleviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class HasHeadRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseRecycleViewAdapter {
    private static final String TAG = HasHeadRecycleViewAdapter.class.getSimpleName();

    private boolean hasHeader;

    protected OnRecycleViewHeadClickListener itemHeadClickListener;

    public interface OnRecycleViewHeadClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecycleViewHeadClickListener(OnRecycleViewHeadClickListener l) {
        this.itemHeadClickListener = l;
    }

    public abstract VH onCreateHeaderViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(final VH holder, int position);

    public HasHeadRecycleViewAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_footer, parent, false);
            return new FooterViewHolder(view);
        }

        return onCreateItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            if (itemHeadClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemHeadClickListener.onItemClick(v, position);
                    }
                });
            }
            onBindHeaderViewHolder((VH) holder, position);
            return;
        } else if (position == getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
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
        onBindItemViewHolder((VH) holder, position - (hasHeader ? 1 : 0));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader) {
            return TYPE_HEADER;
        }

        if (position == getBasicItemCount() && hasFooter) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        if (this.hasHeader != hasHeader) {
            this.hasHeader = hasHeader;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + (hasHeader ? 1 : 0) + (hasFooter ? 1 : 0);
    }
}
