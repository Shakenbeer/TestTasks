package com.shakenbeer.reddittop.viewmodel.list;


import com.shakenbeer.reddittop.model.ChildData;
import com.shakenbeer.reddittop.model.Envelope;
import com.shakenbeer.reddittop.repo.RedditRepository;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChildrenViewModelTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    RedditRepository redditRepository;

    @InjectMocks
    @Spy
    private ChildrenViewModel childrenViewModel;


    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @Test
    public void ifReloadDataThenClearCache() {
        Envelope envelope = new Envelope(true, "Hello", new ArrayList<>());
        when(redditRepository.getMoreItems(true)).thenReturn(Flowable.just(envelope));

        childrenViewModel.loadChildren(true).subscribe();
        verify(childrenViewModel).clearQuickCache();
    }

    @Test
    public void ifLoadDataThenCheckIfProperCache() {
        List<ChildData> children = new ArrayList<>(1);
        ChildData e = new ChildData();
        e.setId("Some_id");
        children.add(e);
        Envelope envelope = new Envelope(true, "Hello", children);

        when(redditRepository.getMoreItems(false)).thenReturn(Flowable.just(envelope));

        childrenViewModel.loadChildren(false).subscribe();
        childrenViewModel.loadCache().test().assertNoErrors()
                .assertValue(childDatas -> childDatas.size() == 1)
                .assertValue(childDatas -> childDatas.get(0).getId().equals("Some_id"));
    }

    @Test
    public void ifLoadDataThenCheckIfProperChildren() {
        List<ChildData> children = new ArrayList<>(1);
        ChildData e = new ChildData();
        e.setId("Some_id");
        children.add(e);
        Envelope envelope = new Envelope(true, "Hello", children);

        when(redditRepository.getMoreItems(false)).thenReturn(Flowable.just(envelope));

        childrenViewModel.loadChildren(false).test().awaitDone(5, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(Envelope::isNotify)
                .assertValue(envelope1 -> envelope1.getMessage().equals("Hello"))
                .assertValue(envelope1 -> envelope1.getChildren().size() == 1)
                .assertValue(envelope1 -> envelope1.getChildren().get(0).getId().equals("Some_id"));
    }


}
