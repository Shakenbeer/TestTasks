package shakenbeer.com.cmindtest.splash;


import shakenbeer.com.cmindtest.presentation.MvpView;

public interface SplashView extends MvpView {
    void showApplicationUi();

    void showError(String message);
}
