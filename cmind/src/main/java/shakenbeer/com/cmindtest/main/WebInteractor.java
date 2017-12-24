package shakenbeer.com.cmindtest.main;


import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import shakenbeer.com.cmindtest.domain.Interactor;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;

public class WebInteractor extends Interactor<Boolean> {

    private static final String YAHOO_COM = "yahoo.com";

    @Inject
    public WebInteractor(@Named(ApplicationModule.IO) Scheduler workScheduler,
                         @Named(ApplicationModule.MAIN) Scheduler mainScheduler) {
        super(workScheduler, mainScheduler);
    }

    @Override
    public Observable<Boolean> buildInteractorObservable(final String... params) {
        return Observable.fromCallable(() -> {
            URL url = new URL(params[0]);
            return url.getHost().contains(YAHOO_COM);
        });
    }


}
