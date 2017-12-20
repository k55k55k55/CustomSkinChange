package com.yw.skin.ywskin;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * 自定义 Application
 * <p>
 * Created by afon on 2017/1/24.
 */

public class App extends Application {
    private static Context mContext;




    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        mContext = getApplicationContext();


    }

    public static Context getAppContext() {
        return mContext;
    }




    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    public static Context getCurrentContext() {
        return mContext;
    }

}
