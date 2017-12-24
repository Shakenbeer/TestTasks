package shakenbeer.com.cmindtest.injection.component;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import shakenbeer.com.cmindtest.injection.module.ApplicationModule;
import shakenbeer.com.cmindtest.list.ListActivity;
import shakenbeer.com.cmindtest.main.MainActivity;
import shakenbeer.com.cmindtest.splash.SplashActivity;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context context();

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(ListActivity listActivity);
}
