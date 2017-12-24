package com.shakenbeer.bestsalmon;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.shakenbeer.bestsalmon.injection.component.ApplicationComponent;
import com.shakenbeer.bestsalmon.injection.component.DaggerApplicationComponent;
import com.shakenbeer.bestsalmon.injection.module.ApplicationModule;
import com.shakenbeer.bestsalmon.injection.module.RepoModule;
import com.shakenbeer.bestsalmon.injection.module.RxModule;
import com.shakenbeer.bestsalmon.injection.module.ApiModule;


public class SalmonApplication extends Application {

    private ApplicationComponent applicationComponent;

    public static SalmonApplication get(Context context) {
        return (SalmonApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .apiModule(new ApiModule())
                    .rxModule(new RxModule())
                    .repoModule(new RepoModule())
                    .build();
        }
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
