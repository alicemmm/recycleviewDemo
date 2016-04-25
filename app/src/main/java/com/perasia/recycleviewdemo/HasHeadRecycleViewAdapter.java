package com.perasia.recycleviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class HasHeadRecycleViewAdapter<T> extends BaseRecycleViewAdapter {
    private static final String TAG = HasHeadRecycleViewAdapter.class.getSimpleName();

    private boolean hasHeader;

    protected OnRecycleViewHeadClickListener itemHeadClickListener;

    public interface OnRecycleViewHeadClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecycleViewHeadClickListener(OnRecycleViewHeadClickListener l) {
        this.itemHeadClickListener = l;
    }

    public abstract int getHeadResource();

    public abstract void onBindHeaderViewHolder(BaseViewHolder holder, int position);

    public HasHeadRecycleViewAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(mContext).inflate(getHeadResource(), parent, false);
            return new BaseViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_footer, parent, false);
            return new FooterViewHolder(view);
        }

        view = LayoutInflater.from(mContext).inflate(getItemResource(), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) holder;

            if (itemHeadClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemHeadClickListener.onItemClick(v, position);
                    }
                });
            }
            onBindHeaderViewHolder(baseViewHolder, position);
            return;
        } else if (position == getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
            if (hasFooter && mRequestLoadMoreListener != null) {
                mRequestLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }

        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }

        onBindItemViewHolder(baseViewHolder, position - (hasHeader ? 1 : 0));
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
