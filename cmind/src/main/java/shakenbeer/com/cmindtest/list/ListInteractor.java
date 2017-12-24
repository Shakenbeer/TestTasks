package shakenbeer.com.cmindtest.list;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import shakenbeer.com.cmindtest.domain.Interactor;
import shakenbeer.com.cmindtest.domain.ListGenerator;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;


public class ListInteractor extends Interactor<List<String>> {

    private final ListGenerator listGenerator;

    @Inject
    ListInteractor(@Named(ApplicationModule.COMPUTATION) Scheduler workScheduler,
                          @Named(ApplicationModule.MAIN) Scheduler mainScheduler,
                          ListGenerator listGenerator) {
        super(workScheduler, mainScheduler);
        this.listGenerator = listGenerator;
    }

    @Override
    public Observable<List<String>> buildInteractorObservable(String... params) {
        return Observable.fromCallable(listGenerator::generate);
    }
}
