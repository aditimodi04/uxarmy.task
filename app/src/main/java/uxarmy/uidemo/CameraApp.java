package uxarmy.uidemo;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;

import uxarmy.uidemo.shared_prefs.AppPreferences;


/**
 * Created by user on 01/12/16.
 */
public class CameraApp extends Application {
    private static Typeface typeface;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AppPreferences.init(this);
            AssetManager am = getApplicationContext().getAssets();

            typeface = Typeface.createFromAsset(am, String.format(Locale.ENGLISH, "fonts/%s", "OpenSans-SemiBold.ttf"));

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

    public static Typeface getTypeFaceSemiBold() {
        return typeface;
    }
}
