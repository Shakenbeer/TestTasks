package com.shakenbeer.reddittop.injection;


import com.shakenbeer.reddittop.viewmodel.list.ChildrenViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(ChildrenViewModel childrenViewModel);

    interface Injectable {
        void inject(ApplicationComponent applicationComponent);
    }
}
