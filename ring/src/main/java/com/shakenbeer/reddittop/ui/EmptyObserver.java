package com.shakenbeer.reddittop.ui;

import android.support.v7.widget.RecyclerView;


public class EmptyObserver extends RecyclerView.AdapterDataObserver {

    public interface Listener {
        void onChange(boolean isEnpty);
    }

    private final Listener listener;
    private final RecyclerView.Adapter adapter;

    public EmptyObserver(Listener listener, RecyclerView.Adapter adapter) {
        this.listener = listener;
        this.adapter = adapter;
    }

    private void checkIfEmpty() {
        if (adapter != null && listener != null) {
            if (adapter.getItemCount() == 0) {
                listener.onChange(true);
            } else {
                listener.onChange(false);
            }
        }
    }

    @Override
    public void onChanged() {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        checkIfEmpty();
    }
}
