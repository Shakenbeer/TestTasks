package com.shakenbeer.reddittop.repo;


import com.shakenbeer.reddittop.model.Child;
import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.model.Top;
import com.shakenbeer.reddittop.model.TopData;
import com.shakenbeer.reddittop.source.local.ChildDao;
import com.shakenbeer.reddittop.source.local.RedditDatabase;
import com.shakenbeer.reddittop.source.remote.RestApi;
import com.shakenbeer.reddittop.util.ContextUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static com.shakenbeer.reddittop.repo.RedditRepositoryImpl.LIMIT;
import static com.shakenbeer.reddittop.repo.RedditRepositoryImpl.NO_NETWORK_SHOW_CACHED_DATA;
import static com.shakenbeer.reddittop.repo.RedditRepositoryImpl.YOU_NEED_ONLY_TOP_50;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedditRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RestApi restApi;

    @Mock
    RedditDatabase database;

    @Mock
    ContextUtils contextUtils;

    @InjectMocks
    @Spy
    RedditRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @Test
    public void ifReloadThenResetPage() {
        repository.getMoreItems(true);
        verify(repository).resetPage();
    }

    @Test
    public void ifLastPageThenReturnNothing() {
        repository.page = 5;
        repository.getMoreItems(false).test()
                .assertValue(envelope -> envelope.getMessage().equals(YOU_NEED_ONLY_TOP_50))
                .assertValue(envelope -> envelope.getChildren().size() == 0);
    }

    @Test
    public void ifNetworkIsAvailableThenLoadFromApi() {
        repository.page = 3;

        ChildData e = new ChildData();
        e.setId("Some_id");
        Child child = new Child();
        child.setData(e);
        List<Child> children = new ArrayList<>(1);
        children.add(child);
        TopData data = new TopData();
        data.setAfter("after");
        data.setChildren(children);
        Top top = new Top();
        top.setData(data);

        when(contextUtils.isNetworkAvailable()).thenReturn(true);
        when(restApi.loadMore(LIMIT, repository.after)).thenReturn(Flowable.just(top));
        when(database.childDao()).thenReturn(new ChildDao() {
            @Override
            public List<ChildData> getChildren(int page) {
                return null;
            }

            @Override
            public void addChildren(List<ChildData> children) {

            }

            @Override
            public void cleanPage(int page) {

            }
        });

        repository.getMoreItems(false).test().awaitDone(5, TimeUnit.SECONDS)
                .assertValue(envelope -> !envelope.isNotify())
                .assertValue(envelope -> envelope.getChildren().size() == 1);

        verify(repository).processChildData(e);

        verify(repository).saveToDb(argThat(argument
                -> argument.size() == 1 && argument.get(0).getId().equals("Some_id")));

        assertEquals(repository.page, 4);

        assertEquals(repository.after, "after");
    }

    @Test
    public void ifNetworkIsUnavailableThenLoadFromDb() {
        List<ChildData> children = new ArrayList<>(1);
        ChildData e = new ChildData();
        e.setId("Some_id");
        children.add(e);

        when(contextUtils.isNetworkAvailable()).thenReturn(false);

        when(database.childDao()).thenReturn(new ChildDao() {
            @Override
            public List<ChildData> getChildren(int page) {
                return children;
            }

            @Override
            public void addChildren(List<ChildData> children) {

            }

            @Override
            public void cleanPage(int page) {

            }
        });

        repository.getMoreItems(false).test().awaitDone(5, TimeUnit.SECONDS)
                .assertValue(envelope -> envelope.isNotify())
                .assertValue(envelope -> envelope.getChildren().size() == 1)
                .assertValue(envelope -> envelope.getMessage().equals(NO_NETWORK_SHOW_CACHED_DATA));
    }

}
