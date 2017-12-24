package com.shakenbeer.bestsalmon.base;

import android.support.v7.app.AppCompatActivity;

import com.shakenbeer.bestsalmon.SalmonApplication;
import com.shakenbeer.bestsalmon.injection.component.ApplicationComponent;


@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        return SalmonApplication.get(this).getComponent();
    }
}
