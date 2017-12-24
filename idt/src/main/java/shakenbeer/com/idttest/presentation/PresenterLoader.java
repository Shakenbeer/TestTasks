package shakenbeer.com.idttest.presentation;

import android.support.v4.content.Loader;
import android.content.Context;
import android.util.Log;


/**
 * We want our presenters to survive configuration changes. Thus we use Loaders to achieve this.
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf
 * (from Android Weekly #195)
 */
public final class PresenterLoader<T extends MvpPresenter> extends Loader<T> {

    private final PresenterFactory<T> factory;
    private final String tag;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }

    @Override
    protected void onStartLoading() {
        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {

    }

    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter = null;
        }
    }
}