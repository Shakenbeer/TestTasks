package com.shakenbeer.bestsalmon.presentation;


@SuppressWarnings("unused")
public interface MvpPresenter<V extends MvpView> {
    void attachView(V mvpView);

    void detachView();

    @SuppressWarnings("EmptyMethod")
    void onDestroyed();
}
