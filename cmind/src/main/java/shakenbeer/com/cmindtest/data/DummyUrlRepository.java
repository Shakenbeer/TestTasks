package shakenbeer.com.cmindtest.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import shakenbeer.com.cmindtest.domain.UrlRepository;


public class DummyUrlRepository implements UrlRepository {

    private static final List<String> urls = new ArrayList<>();

    static {
        urls.add("https://openclipart.org/image/600px/svg_to_png/247289/ferme.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/220150/Tiere-coloured.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/175392/funny-animal.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/173854/chinese-horoscope-animals-v1.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/28007/kablam-Party-Animal.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/17144/liakad-cartoon-animal-head.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256547/Found-Cartoon-Animals-Request-9-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256546/Found-Cartoon-Animals-Request-8-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256545/Found-Cartoon-Animals-Request-7-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256544/Found-Cartoon-Animals-Request-6-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256543/Found-Cartoon-Animals-Request-5-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256542/Found-Cartoon-Animals-Request-4-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256541/Found-Cartoon-Animals-Request-3-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256540/Found-Cartoon-Animals-Request-2-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256539/Found-Cartoon-Animals-Request-1-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/256538/Found-Cartoon-Animals-Request-2016072659.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/219687/animals-2015053102.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195794/square-animal-12-racoon.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195789/square-animal-7-turtle.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195785/square-animal-3-pig.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195793/square-animal-11-elephant.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195792/square-animal-10-rabbit.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/195791/square-animal-9-bear.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/176706/1364918969.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/65113/ribbit7.png");
        urls.add("https://openclipart.org/image/600px/svg_to_png/71131/seven.png");

    }

    @Inject
    public DummyUrlRepository() {
    }

    @Override
    public Observable<List<String>> getImageUrls() {
        return Observable.just(urls);
    }
}
