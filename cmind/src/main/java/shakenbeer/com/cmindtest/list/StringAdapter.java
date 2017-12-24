package shakenbeer.com.cmindtest.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.databinding.ItemBinding;

class StringAdapter extends RecyclerView.Adapter<StringAdapter.ViewHolder> {

    interface ClickListener {
        void onItemClick(int position);
    }

    private List<String> strings = new ArrayList<>();
    private ClickListener clickListener;


    @Inject
    StringAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBinding binding = ItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = strings.get(position);
        holder.binding.setText(item);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    void setStrings(List<String> strings) {
        this.strings = strings;
    }

    void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ItemBinding binding;

        ViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(this.getAdapterPosition());
            }
        }
    }
}
