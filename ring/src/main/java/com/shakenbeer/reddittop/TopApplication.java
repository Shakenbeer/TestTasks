package com.shakenbeer.reddittop;

import android.app.Application;
import android.content.Context;

import com.shakenbeer.reddittop.injection.ApplicationComponent;
import com.shakenbeer.reddittop.injection.ApplicationModule;
import com.shakenbeer.reddittop.injection.DaggerApplicationComponent;


public class TopApplication extends Application {

    private ApplicationComponent applicationComponent;

    public static TopApplication get(Context context) {
        return (TopApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }
}
