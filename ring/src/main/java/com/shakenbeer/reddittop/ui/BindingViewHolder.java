package com.shakenbeer.reddittop.ui;


import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shakenbeer.reddittop.BR;

public class BindingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ClickListener clickListener;
    private final ViewDataBinding binding;

    BindingViewHolder(ViewDataBinding binding, @NonNull ClickListener clickListener) {
        super(binding.getRoot());
        binding.getRoot().setOnClickListener(this);
        this.binding = binding;
        this.clickListener = clickListener;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.obj, obj);
        binding.executePendingBindings();
    }

    @Override
    public void onClick(View v) {
        clickListener.onHolderViewClick(getAdapterPosition(), sharedViews());
    }

    //override in case want to use shared views in transition
    @SuppressWarnings("WeakerAccess")
    public View[] sharedViews() {
        return new View[]{};
    }

    interface ClickListener {
        void onHolderViewClick(int position, View... shared);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}