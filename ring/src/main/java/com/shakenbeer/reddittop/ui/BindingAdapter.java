package com.shakenbeer.reddittop.ui;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BindingAdapter<I> extends RecyclerView.Adapter<BindingViewHolder> implements BindingViewHolder.ClickListener {

    protected List<I> items = new ArrayList<>();
    private ItemClickListener<I> itemClickListener;

    @Override
    public void onHolderViewClick(int position, View... shared) {
        if (itemClickListener != null) {
            itemClickListener.onClick(items.get(position), position, shared);
        }
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        return new BindingViewHolder(binding, this);
    }

    public void onBindViewHolder(BindingViewHolder holder,
                                 int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);

    public List<I> getItems() {
        return items;
    }

    public I getItem(int position) {
        return items.get(position);
    }

    public void setItems(List<I> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(I item) {
        addItem(item, items.size());
    }

    public void addItem(I item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void addItems(List<I> itemsToAdd) {
        addItems(itemsToAdd, items.size());
    }

    public void addItems(List<I> itemsToAdd, int position) {
        items.addAll(position, itemsToAdd);
        notifyItemRangeInserted(position, itemsToAdd.size());
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void replaceItem(I item, int position) {
        items.set(position, item);
        notifyItemChanged(position);
    }

    public void clear() {
        int size = items.size();
        if (size > 0) {
            items.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void setItemClickListener(ItemClickListener<I> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void changeData(List<I> items, boolean notify) {
        this.items = new ArrayList<>(items);
        if (notify) notifyDataSetChanged();
    }

    public interface ItemClickListener<I> {
        void onClick(I item, int position, View... shared);
    }
}