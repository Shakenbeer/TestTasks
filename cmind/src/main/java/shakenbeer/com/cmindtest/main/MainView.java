package shakenbeer.com.cmindtest.main;


import shakenbeer.com.cmindtest.presentation.MvpView;

interface MainView extends MvpView {

    void showError(String message);

    void showImage(String imageUrl);

    void showListUi();
}
