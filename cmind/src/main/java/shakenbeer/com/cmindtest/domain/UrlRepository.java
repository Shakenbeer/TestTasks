package shakenbeer.com.cmindtest.domain;


import java.util.List;

import io.reactivex.Observable;


public interface UrlRepository {
    Observable<List<String>> getImageUrls();
}
