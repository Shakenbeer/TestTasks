package shakenbeer.com.cmindtest;


import android.app.Application;

import shakenbeer.com.cmindtest.injection.component.ApplicationComponent;
import shakenbeer.com.cmindtest.injection.component.DaggerApplicationComponent;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;

public class CmindApplication extends Application {

    private ApplicationComponent applicationComponent;

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

}
