package shakenbeer.com.idttest.imagedownload;

import android.content.Context;

import shakenbeer.com.idttest.presentation.PresenterFactory;


public class ImageDownloadPresenterFactory implements PresenterFactory<ImageDownloadPresenter> {

    private final Context context;

    public ImageDownloadPresenterFactory(Context context) {
        this.context = context;
    }

    @Override
    public ImageDownloadPresenter create() {
        return new ImageDownloadPresenter(context);
    }
}
