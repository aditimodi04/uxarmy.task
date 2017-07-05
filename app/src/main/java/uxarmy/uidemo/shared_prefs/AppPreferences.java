package uxarmy.uidemo.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by phoosaram on 11/4/2015.
 */
public class AppPreferences {
    public static final String APP_TOKEN_IN_NUMBERS = "367959694031";
    private static Context mContext;
    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor;
    public static String PREFERENCES_NAME = "app_prefs";


    public static void init(Context mContext) {
        if (AppPreferences.preferences == null || AppPreferences.editor == null) {
            AppPreferences.mContext = mContext;
            AppPreferences.preferences = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            AppPreferences.editor = preferences.edit();
        }
    }

    public static void savePreferences() {
        editor.commit();
    }

    public static void removeValueForKey(String key) {
        if (key != null && !key.isEmpty()) {
            editor.remove(key);
            editor.commit();
        }
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }

    public static void setImageStringBitmap(String stringBitmap) {
        editor.putString("stringBitmap", stringBitmap);
    }

    public static String getImageStringBitmap() {
        return preferences.getString("stringBitmap", null);
    }

}
