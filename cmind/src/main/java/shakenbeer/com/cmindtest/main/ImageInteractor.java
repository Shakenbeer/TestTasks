package shakenbeer.com.cmindtest.main;


import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import shakenbeer.com.cmindtest.domain.Interactor;
import shakenbeer.com.cmindtest.domain.UrlRepository;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;

public class ImageInteractor extends Interactor<String> {

    private static final String IMAGE_NOT_FOUND_URL = "http://loremflickr.com/600/600";

    private final UrlRepository urlRepository;

    @Inject
    public ImageInteractor(@Named(ApplicationModule.IO) Scheduler workScheduler,
                           @Named(ApplicationModule.MAIN) Scheduler mainScheduler,
                           UrlRepository urlRepository) {
        super(workScheduler, mainScheduler);
        this.urlRepository = urlRepository;
    }

    @Override
    public Observable<String> buildInteractorObservable(String... params) {
        return urlRepository.getImageUrls().map(strings -> {
            if (strings.size() > 0) {
                Random random = new Random(System.currentTimeMillis());
                int index = random.nextInt(strings.size());
                return strings.get(index);
            } else {
                return IMAGE_NOT_FOUND_URL;
            }
        });
    }
}
