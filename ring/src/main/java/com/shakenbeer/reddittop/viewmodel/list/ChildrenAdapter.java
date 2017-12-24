package com.shakenbeer.reddittop.viewmodel.list;

import com.shakenbeer.reddittop.R;
import com.shakenbeer.reddittop.databinding.ItemRedditBinding;
import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.ui.BindingAdapter;
import com.shakenbeer.reddittop.ui.BindingViewHolder;

public class ChildrenAdapter extends BindingAdapter<ChildData> {

    private ThumbnailListener thumbnailListener;

    public void setThumbnailListener(ThumbnailListener thumbnailListener) {
        this.thumbnailListener = thumbnailListener;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemRedditBinding binding = (ItemRedditBinding) holder.getBinding();
        ChildData childData = items.get(position);
        binding.imageView.setOnClickListener(view -> {
            if (thumbnailListener != null) {
                thumbnailListener.onThumbnailClick(childData);
            }
        });
    }

    @Override
    protected Object getObjForPosition(int position) {
        return items.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.item_reddit;
    }

    public interface ThumbnailListener {
        void onThumbnailClick(ChildData childData);
    }
}
