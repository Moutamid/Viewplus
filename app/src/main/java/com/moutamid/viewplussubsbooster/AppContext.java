package com.moutamid.viewplussubsbooster;

import android.app.Application;
import android.content.Context;

import com.moutamid.viewplussubsbooster.utils.Constants;
import com.moutamid.viewplussubsbooster.utils.Utils;

import java.lang.invoke.ConstantCallSite;

public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
