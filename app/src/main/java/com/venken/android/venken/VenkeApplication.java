package com.venken.android.venken;

import android.app.Application;

import com.fota.android.commonlib.app.AppVariables;
import com.fota.android.commonlib.utils.SharedPreferencesUtil;
import com.tencent.bugly.Bugly;

import java.util.HashMap;
import java.util.Map;

public class VenkeApplication extends Application {

    public static String description;

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "6b329e2663", BuildConfig.DEBUG);
        SharedPreferencesUtil.init(this);
        AppVariables.put("3", this);
    }


    public final static Map<String, String> PHONE = new HashMap<String, String>() {
        {
            put("15669076382", "");
            put("13388695776", "");
        }
    };

}
