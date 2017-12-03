package com.yisingle.baselibray.baseadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.yisingle.baselibray.baseadapter.viewholder.ListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView的通用adapter
 */
@SuppressWarnings({"unused", "CanBeFinal", "SameReturnValue"})
public abstract class ListAdapter<D> extends BaseAdapter implements DataHelper<D> {

    protected List<D> mDataSet;
    private int[] layoutIds;

    public ListAdapter(List<D> data, int... layoutIds) {
        if (data == null)
            this.mDataSet = new ArrayList<>();
        else
            this.mDataSet = data;
        this.layoutIds = layoutIds;
    }

    @Override
    public int getViewTypeCount() {
        return layoutIds.length;
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIndex(position, mDataSet.get(position));
    }

    @Override
    public int getCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public D getItem(int position) {
        return mDataSet == null ? null : mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 指定item布局样式在layoutIds的索引。默认为第一个
     * 多条目类型时复写此方法
     *
     * @param position 角标
     * @param item     对象
     * @return 该item使用第几个layoutId
     */
    public int getLayoutIndex(int position, D item) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId = layoutIds[getItemViewType(position)];
        ListViewHolder viewHolder = ListViewHolder.get(convertView, parent, layoutId);
        onBindData(viewHolder, position, getItem(position));
        return viewHolder.getItemView();
    }

    /**
     * 绑定数据到Item View上
     *
     * @param holder   viewholder
     * @param position 数据的位置
     * @param item     数据项
     */
    protected abstract void onBindData(ListViewHolder holder, int position, D item);

    @Override
    public boolean contains(D d) {
        return mDataSet.contains(d);
    }

    @Override
    public void addItem(D item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItemToHead(D item) {
        mDataSet.add(0, item);
        notifyDataSetChanged();
    }

    @Override
    public void addItemsToHead(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(0, items);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void remove(D item) {
        mDataSet.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    @Override
    public void refreshWithNewData(List<D> items) {
        mDataSet.clear();
        if (items != null && !items.isEmpty())
            mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void modify(D oldData, D newData) {
        modify(mDataSet.indexOf(oldData), newData);
    }

    @Override
    public void modify(int index, D newData) {
        mDataSet.set(index, newData);
        notifyDataSetChanged();
    }
}