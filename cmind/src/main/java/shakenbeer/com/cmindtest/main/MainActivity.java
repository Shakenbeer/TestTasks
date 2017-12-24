package shakenbeer.com.cmindtest.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.R;
import shakenbeer.com.cmindtest.databinding.ActivityMainBinding;
import shakenbeer.com.cmindtest.list.ListActivity;
import shakenbeer.com.cmindtest.view.BaseActivity;

public class MainActivity extends BaseActivity implements MainView {

    private static final String GOOGLE_COM = "https://www.google.com";
    private static final int PAGE_MARGIN_DP = 8;

    @Inject
    MainPresenter presenter;

    private ActivityMainBinding binding;

    private int marginInPx;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getApplicationComponent().inject(this);
        presenter.attachView(this);

        marginInPx = dpToPx(PAGE_MARGIN_DP);

        loadStartPage();
        setupImageView();
        applyProperLayout(getResources().getConfiguration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        applyProperLayout(newConfig);
    }

    private void loadStartPage() {
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String stringUrl) {
                return presenter.checkLink(stringUrl);
            }
        });
        binding.webView.loadUrl(GOOGLE_COM);
    }


    private void setupImageView() {
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onImageClicked();
            }
        });
    }

    private void applyProperLayout(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            applyLandOrientationLayout();
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            applyPortraitOrientationLayout();
        }
    }

    public void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean networkAvailable = networkInfo != null && networkInfo.isConnected();
        if (!networkAvailable) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showListUi() {
        ListActivity.start(this);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, getString(R.string.error_occurred, message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showImage(String imageUrl) {
        Glide.with(this).load(imageUrl).error(R.drawable.no_image_250dp).into(binding.imageView);
    }

    private void applyLandOrientationLayout() {
        binding.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
        binding.webView.setLayoutParams(params);
        binding.imageView.setLayoutParams(params);

    }

    private void applyPortraitOrientationLayout() {
        binding.linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        params.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
        binding.webView.setLayoutParams(params);
        binding.imageView.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
