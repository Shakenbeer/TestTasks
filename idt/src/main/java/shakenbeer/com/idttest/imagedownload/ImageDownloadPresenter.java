package shakenbeer.com.idttest.imagedownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Patterns;

import java.io.File;

import shakenbeer.com.idttest.R;
import shakenbeer.com.idttest.presentation.BasePresenter;

/**
 * In order to keep simplicity for such little project we put business logic here.
 * In other case we prefer Clean Architecture approach,
 * boilerplate project can be found there
 * https://github.com/Shakenbeer/MyAndroidBoilerplate
 */
public class ImageDownloadPresenter extends BasePresenter<ImageDownloadView> {

    private final Context context;

    private IntentFilter imageStoredFilter;
    private BroadcastReceiver imageDownloadReceiver;

    private volatile boolean downloading = false;
    private Bitmap rotated = null;

    public ImageDownloadPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(ImageDownloadView mvpView) {
        super.attachView(mvpView);
        restoreView();
        registerDownloadReceiver();
    }

    private void restoreView() {
        if (rotated != null) {
            getMvpView().showDownloadedImage(rotated);
        }
        if (downloading) {
            getMvpView().showImageProcessingIndicator(context.getString(R.string.downloading));
        }
    }

    private void registerDownloadReceiver() {
        imageStoredFilter = new IntentFilter(ImageDownloadService.IMAGE_DOWNLOAD);
        imageDownloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                downloading = false;
                if (isViewAttached()) {
                    boolean isSuccess = intent.getBooleanExtra(ImageDownloadService.SUCCESS_EXTRA, false);
                    if (!isSuccess) {
                        processDownloadingError(intent);
                    } else {
                        processDownloadingSuccess();
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(imageDownloadReceiver, imageStoredFilter);
    }

    private void processDownloadingSuccess() {
        getMvpView().showImageProcessingIndicator(context.getString(R.string.rotating));
        File file = new File(context.getFilesDir(), ImageDownloadService.FILE_NAME);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        getMvpView().hideImageProcessingIndicator();
        getMvpView().showDownloadedImage(rotated);
    }

    private void processDownloadingError(Intent intent) {
        getMvpView().hideImageProcessingIndicator();
        String error = intent.getStringExtra(ImageDownloadService.MESSAGE_EXTRA);
        getMvpView().showPopupMessage(error != null ? error : context.getString(R.string.undefined_error));
    }

    @Override
    public void detachView() {
        getMvpView().hideImageProcessingIndicator();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(imageDownloadReceiver);
        super.detachView();
    }

    public void processUrl(String url) {
        if (!validImageUrl(url)) {
            getMvpView().showInvalidUrlNotification();
        } else {
            downloadImage(url);
        }
    }

    private boolean validImageUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    private void downloadImage(String url) {
        if (!isNetworkAvailable()) {
            getMvpView().showPopupMessage(context.getString(R.string.no_network));
            return;
        }

        downloading = true;
        getMvpView().showImageProcessingIndicator(context.getString(R.string.downloading));

        Intent intent = new Intent(context, ImageDownloadService.class);
        intent.putExtra(ImageDownloadService.IMAGE_URL_EXTRA, url);
        context.startService(intent);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void onCancel() {
        //this is how we communicate with IntentService -
        //call startCommand again with specific Intent
        //another way is to replace IntentService with own Service implementation and
        //use Binder to make calls to Service
        Intent intent = new Intent(context, ImageDownloadService.class);
        intent.putExtra(ImageDownloadService.SHOULD_CANCEL_EXTRA, true);
        context.startService(intent);
    }
}
