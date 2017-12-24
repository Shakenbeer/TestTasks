package shakenbeer.com.cmindtest.list;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.R;
import shakenbeer.com.cmindtest.databinding.ActivityListBinding;
import shakenbeer.com.cmindtest.view.BaseActivity;

public class ListActivity extends BaseActivity implements ListView, StringAdapter.ClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, ListActivity.class);
        context.startActivity(starter);
    }

    @Inject
    ListPresenter listPresenter;

    @Inject
    StringAdapter adapter;

    private ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        getApplicationComponent().inject(this);

        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.itemsRecyclerView.addItemDecoration(new SimpleDividerDecoration(this));
        binding.itemsRecyclerView.setAdapter(adapter);

        adapter.setClickListener(this);
        listPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listPresenter.detachView();
    }

    @Override
    public void showLoadingIndicator() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showList(List<String> strings) {
        adapter.setStrings(strings);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, getString(R.string.error_occurred, message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPreviousUi() {
        onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            listPresenter.onFirstItemClicked();
        }
    }
}
