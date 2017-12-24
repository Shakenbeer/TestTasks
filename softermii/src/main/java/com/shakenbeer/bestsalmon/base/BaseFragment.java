package com.shakenbeer.bestsalmon.base;

import android.support.v4.app.Fragment;

import com.shakenbeer.bestsalmon.SalmonApplication;
import com.shakenbeer.bestsalmon.injection.component.ApplicationComponent;


public abstract class BaseFragment extends Fragment {

    protected ApplicationComponent getApplicationComponent() {
        return SalmonApplication.get(getContext()).getComponent();
    }
}
