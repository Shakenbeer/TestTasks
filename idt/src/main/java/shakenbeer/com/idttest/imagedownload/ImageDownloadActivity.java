package shakenbeer.com.idttest.imagedownload;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import shakenbeer.com.idttest.R;
import shakenbeer.com.idttest.databinding.ActivityImageDownloadBinding;
import shakenbeer.com.idttest.presentation.PresenterLoader;

public class ImageDownloadActivity extends AppCompatActivity implements ImageDownloadView, LoaderManager.LoaderCallbacks<ImageDownloadPresenter> {

    private static final int PRESENTER_LOADER_ID = 11;
    private static final String TAG = "ImageDownload";

    private ActivityImageDownloadBinding binding;
    private ProgressDialog dialog;
    private ImageDownloadPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_download);

        //we should clear error after we change text
        //unfortunately this still an issue of appcompat library - it should do it automatically
        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.textInputLayout.isErrorEnabled()) {
                    binding.textInputLayout.setError(null);
                    binding.textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog = new ProgressDialog(this);

        //if user tap on screen or press BACK, this indicates intent to cancel action
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                presenter.onCancel();
            }
        });

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        super.onStop();
    }

    public void onDownloadImageClick(View v) {
        presenter.processUrl(binding.editText.getText().toString());
    }

    @Override
    public void showInvalidUrlNotification() {
        binding.textInputLayout.setErrorEnabled(true);
        binding.textInputLayout.setError(getString(R.string.invalid_url));
    }

    @Override
    public void showImageProcessingIndicator(String message) {
        dialog.setMessage(message);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideImageProcessingIndicator() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showDownloadedImage(final Bitmap bitmap) {
        binding.imageView.setImageBitmap(bitmap);
    }

    @Override
    public void showPopupMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<ImageDownloadPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new ImageDownloadPresenterFactory(getApplicationContext()), TAG);
    }

    @Override
    public void onLoadFinished(Loader<ImageDownloadPresenter> loader, ImageDownloadPresenter data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<ImageDownloadPresenter> loader) {
        presenter = null;
    }
}
