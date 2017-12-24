package shakenbeer.com.cmindtest.splash;


import javax.inject.Inject;

import shakenbeer.com.cmindtest.presentation.BasePresenter;

public class SplashPresenter extends BasePresenter<SplashView> {

    private final SplashInteractor splashInteractor;

    @Inject
    SplashPresenter(SplashInteractor splashInteractor) {
        this.splashInteractor = splashInteractor;
    }

    @Override
    public void attachView(SplashView mvpView) {
        super.attachView(mvpView);
        splashInteractor.execute(o -> {
                },
                e -> getMvpView().showError(e.getMessage()),
                () -> getMvpView().showApplicationUi());
    }

    @Override
    public void detachView() {
        super.detachView();
        splashInteractor.unsubscribe();
    }
}
