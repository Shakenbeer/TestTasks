package shakenbeer.com.cmindtest.main;


import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import shakenbeer.com.cmindtest.presentation.BasePresenter;

public class MainPresenter extends BasePresenter<MainView> {

    private static final String YAHOO_COM = "yahoo.com";

    private final ImageInteractor imageInteractor;

    @Inject
    MainPresenter(ImageInteractor imageInteractor) {
        this.imageInteractor = imageInteractor;
    }

    @Override
    public void detachView() {
        super.detachView();
        imageInteractor.unsubscribe();
    }

    void onImageClicked() {
        imageInteractor.execute(s -> {
            if (isViewAttached()) {
                getMvpView().showImage(s);
            }
        }, e -> {
            if (isViewAttached()) {
                getMvpView().showError(e.getMessage());
            }
        }, () -> {
        });
    }

    boolean checkLink(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
            if (url.getHost().contains(YAHOO_COM) && isViewAttached()) {
                getMvpView().showListUi();
                return true;
            }
        } catch (MalformedURLException ignored) {
        }
        return false;
    }
}
