package com.shakenbeer.bestsalmon.domain;


import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public abstract class Interactor {
    private final Scheduler workScheduler;
    private final Scheduler mainScheduler;

    private Subscription subscription = Subscriptions.empty();

    public Interactor(Scheduler workScheduler, Scheduler mainScheduler) {
        this.workScheduler = workScheduler;
        this.mainScheduler = mainScheduler;
    }

    public abstract Observable buildInteractorObservable(String... params);

    @SuppressWarnings("unchecked")
    public void execute(Subscriber interactorSubscriber, String... params) {
        this.subscription = this.buildInteractorObservable(params)
                .subscribeOn(workScheduler)
                .observeOn(mainScheduler)
                .subscribe(interactorSubscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
