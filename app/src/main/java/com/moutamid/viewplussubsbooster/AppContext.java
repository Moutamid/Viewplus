package com.moutamid.viewplussubsbooster;

import android.app.Application;

import com.moutamid.viewplussubsbooster.utils.Utils;

public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
