package shakenbeer.com.cmindtest.list;

import java.util.List;

import shakenbeer.com.cmindtest.presentation.MvpView;


interface ListView extends MvpView {
    void showLoadingIndicator();

    void hideLoadingIndicator();

    void showList(List<String> strings);

    void showError(String message);

    void showPreviousUi();
}
