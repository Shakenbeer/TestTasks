package shakenbeer.com.idttest.presentation;

/**
 * We use MVP pattern for presentation logic decoupling:
 * https://en.wikipedia.org/wiki/Model-view-presenter
 * Every class that is Presenter in context of MVP should implement this interface.
 * Most of time we extend BasePresenter class.
 */
public interface MvpPresenter<V extends MvpView> {
    void attachView(V mvpView);

    void detachView();
}
