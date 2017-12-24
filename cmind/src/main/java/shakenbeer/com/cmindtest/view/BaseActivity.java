package shakenbeer.com.cmindtest.view;


import android.support.v7.app.AppCompatActivity;

import shakenbeer.com.cmindtest.CmindApplication;
import shakenbeer.com.cmindtest.injection.component.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return ((CmindApplication) getApplication()).getComponent();
    }
}
