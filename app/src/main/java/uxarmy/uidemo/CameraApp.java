package uxarmy.uidemo;

import android.app.Application;

import java.lang.Thread.UncaughtExceptionHandler;

import uxarmy.uidemo.shared_prefs.AppPreferences;


/**
 * Created by user on 01/12/16.
 */
public class CameraApp extends Application {


    public static String version;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AppPreferences.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    throwable.printStackTrace();
                    System.exit(1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
