package shakenbeer.com.cmindtest.list;


import javax.inject.Inject;

import shakenbeer.com.cmindtest.presentation.BasePresenter;

public class ListPresenter extends BasePresenter<ListView> {

    private final ListInteractor listInteractor;

    @Inject
    ListPresenter(ListInteractor listInteractor) {
        this.listInteractor = listInteractor;
    }

    @Override
    public void attachView(ListView mvpView) {
        super.attachView(mvpView);
        getMvpView().showLoadingIndicator();
        listInteractor.execute(strings -> getMvpView().showList(strings),
                e -> getMvpView().showError(e.getMessage()),
                () -> getMvpView().hideLoadingIndicator());
    }

    @Override
    public void detachView() {
        super.detachView();
        listInteractor.unsubscribe();
    }

    void onFirstItemClicked() {
        getMvpView().showPreviousUi();
    }
}
