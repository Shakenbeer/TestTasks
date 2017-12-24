package shakenbeer.com.idttest.presentation;

/**
 * We use MVP pattern for presentation logic decoupling:
 * https://en.wikipedia.org/wiki/Model-view-presenter
 * Every class that is View in context of MVP should implement this interface.
 * Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {
}
