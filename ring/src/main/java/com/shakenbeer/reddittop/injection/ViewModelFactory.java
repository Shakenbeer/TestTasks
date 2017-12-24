package com.shakenbeer.reddittop.injection;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.shakenbeer.reddittop.TopApplication;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private TopApplication application;

    public ViewModelFactory(TopApplication application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        T t = super.create(modelClass);
        if (t instanceof ApplicationComponent.Injectable) {
            ((ApplicationComponent.Injectable) t).inject(application.getComponent());
        }
        return t;
    }
}
