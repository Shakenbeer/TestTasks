package shakenbeer.com.cmindtest.domain;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.DefaultSubscriber;

public abstract class Interactor<T> {
    private final Scheduler workScheduler;
    private final Scheduler mainScheduler;

    private Disposable subscription = Disposables.empty();

    public Interactor(Scheduler workScheduler, Scheduler mainScheduler) {
        this.workScheduler = workScheduler;
        this.mainScheduler = mainScheduler;
    }

    public abstract Observable buildInteractorObservable(String... params);

    @SuppressWarnings("unchecked")
    public void execute(Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete, String... params) {
        this.subscription = this.buildInteractorObservable(params)
                .subscribeOn(workScheduler)
                .observeOn(mainScheduler)
                .subscribe(onNext, onError, onComplete);
    }

    public void unsubscribe() {
        if (!subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
