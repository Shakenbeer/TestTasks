package shakenbeer.com.idttest.imagedownload;

import android.graphics.Bitmap;

import shakenbeer.com.idttest.presentation.MvpView;

/**
 * Provides user interface and forward user's action to presenter
 */
public interface ImageDownloadView extends MvpView {

    void showInvalidUrlNotification();

    void showImageProcessingIndicator(String message);

    void hideImageProcessingIndicator();

    void showDownloadedImage(Bitmap bitmap);

    void showPopupMessage(String text);
}
