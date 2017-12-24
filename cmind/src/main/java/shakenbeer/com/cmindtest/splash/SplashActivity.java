package shakenbeer.com.cmindtest.splash;


import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.R;
import shakenbeer.com.cmindtest.main.MainActivity;
import shakenbeer.com.cmindtest.view.BaseActivity;

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getApplicationComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showApplicationUi() {
        MainActivity.start(this);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, getString(R.string.error_occurred, message), Toast.LENGTH_SHORT).show();
    }
}
