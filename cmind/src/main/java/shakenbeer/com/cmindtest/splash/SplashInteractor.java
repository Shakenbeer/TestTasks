package shakenbeer.com.cmindtest.splash;


import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import shakenbeer.com.cmindtest.domain.Interactor;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;

public class SplashInteractor extends Interactor<Object> {

    @Inject
    public SplashInteractor(@Named(ApplicationModule.IO) Scheduler workScheduler,
                            @Named(ApplicationModule.MAIN) Scheduler mainScheduler) {
        super(workScheduler, mainScheduler);
    }

    @Override
    public Observable<Object> buildInteractorObservable(String... params) {
        //Simply wait 3 seconds
        return Observable.fromCallable(() -> {
            Thread.sleep(3000);
            return new Object();
        });
    }


}
