package com.yisingle.baselibray.baseadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/5/25.
 */

public abstract class SectionSimpleRecyclerAdapter<H, I, F> extends SectionedRecyclerViewAdapter<RecyclerViewHolder, RecyclerViewHolder, RecyclerViewHolder> {


    private List<GroupData<H, I, F>> groupDataList;

    private int heardLayoutId;

    private int itemLayoutId;

    private int footerLayoutId;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public SectionSimpleRecyclerAdapter(List<GroupData<H, I, F>> groupDataList, @LayoutRes int heardLayoutId, @LayoutRes int itemLayoutId, @LayoutRes int footerLayoutId) {
        if (groupDataList == null)
            this.groupDataList = new ArrayList<>();
        else
            this.groupDataList = groupDataList;

        this.heardLayoutId = heardLayoutId;
        this.itemLayoutId = itemLayoutId;
        this.footerLayoutId = footerLayoutId;

    }


    public SectionSimpleRecyclerAdapter(List<GroupData<H, I, F>> groupDataList, @LayoutRes int heardLayoutId, @LayoutRes int itemLayoutId) {
        this(groupDataList, heardLayoutId, itemLayoutId, 0);

    }


    public GridLayoutManager getGridLayoutManager(Context context, int count) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, count);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(this, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        return layoutManager;
    }


    @Override
    protected int getSectionCount() {
        return groupDataList == null ? 0 : groupDataList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count;
        if (null != groupDataList && null != groupDataList.get(section) && null != groupDataList.get(section).getItemList()) {
            count = groupDataList.get(section).getItemList().size();
        } else {
            count = 0;
        }
        return count;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View headerView = LayoutInflater.from(parent.getContext()).inflate(heardLayoutId, parent, false);
        return new RecyclerViewHolder(headerView);
    }

    @Override
    protected RecyclerViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View footerView = LayoutInflater.from(parent.getContext()).inflate(footerLayoutId, parent, false);
        return new RecyclerViewHolder(footerView);
    }

    @Override
    protected RecyclerViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(RecyclerViewHolder holder, int section) {

        H heardData = groupDataList.get(section).headerData;

        onBindHeaderData(holder, section, heardData);

    }


    @Override
    protected void onBindItemViewHolder(RecyclerViewHolder holder, int section, int position) {
        I itemData = groupDataList.get(section).getItemList().get(position);

        onBindItemData(holder, section, position, itemData);
        setupItemClickListener(holder, section, position);
        setupItemLongClickListener(holder, section, position);
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerViewHolder holder, int section) {

        F footerData = groupDataList.get(section).footerData;
        onBindFooterData(holder, section, footerData);

    }


    protected void setupItemClickListener(final RecyclerViewHolder viewHolder, final int section, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(section, position, getItemData(section, position));
                }
            }
        });
    }

    protected void setupItemLongClickListener(final RecyclerViewHolder viewHolder, final int section, final int position) {
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(section, position, getItemData(section, position));
                }
                return false;
            }
        });
    }

    protected abstract void onBindHeaderData(RecyclerViewHolder holder, int section, H item);

    protected abstract void onBindItemData(RecyclerViewHolder holder, int section, int position, I item);

    protected abstract void onBindFooterData(RecyclerViewHolder holder, int section, F item);

    public static class GroupData<H, I, F> {
        private H headerData;
        private List<I> itemList;
        private F footerData;


        public GroupData(@NonNull H headerData, @NonNull List<I> itemList, @NonNull F footerData) {
            this.headerData = headerData;
            this.itemList = itemList;
            this.footerData = footerData;
        }


        public H getHeaderData() {
            return headerData;
        }

        public void setHeaderData(H headerData) {
            this.headerData = headerData;
        }

        public List<I> getItemList() {
            return itemList;
        }

        public void setItemList(List<I> itemList) {
            this.itemList = itemList;
        }

        public F getFooterData() {
            return footerData;
        }

        public void setFooterData(F footerData) {
            this.footerData = footerData;
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int section, int position, Object item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int section, int position, Object item);
    }

    private I getItemData(int section, int position) {
        return groupDataList.get(section).getItemList().get(position);
    }

    private H getHeaderData(int section) {
        return groupDataList.get(section).headerData;
    }

    private F getFooterData(int section) {
        return groupDataList.get(section).footerData;
    }


    public void refreshWithNewData(@NonNull List<GroupData<H, I, F>> items) {
        groupDataList.clear();
        groupDataList.addAll(items);
        notifyDataSetChanged();
    }
}
