package shakenbeer.com.cmindtest.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import shakenbeer.com.cmindtest.data.DummyUrlRepository;
import shakenbeer.com.cmindtest.domain.ListGenerator;
import shakenbeer.com.cmindtest.domain.UrlRepository;
import shakenbeer.com.cmindtest.list.ListGeneratorImpl;

@Module
public class ApplicationModule {
    public static final String MAIN = "main";
    public static final String IO = "io";
    public static final String COMPUTATION = "computation";

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    @Named(MAIN)
    Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(IO)
    Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named(COMPUTATION)
    Scheduler provideComputationScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Singleton
    UrlRepository provideUrlRepository(DummyUrlRepository dummyUrlRepository) {
        return dummyUrlRepository;
    }

    @Provides
    @Singleton
    ListGenerator provideListGenerator(ListGeneratorImpl listGenerator) {
        return listGenerator;
    }
}
