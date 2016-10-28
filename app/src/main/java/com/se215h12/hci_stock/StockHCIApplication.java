package com.se215h12.hci_stock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import com.se215h12.hci_stock.util.Utils;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.Preferences;

/**
 * Created by TungHo on 10/26/2016.
 */
public class StockHCIApplication extends Application {

    private static StockHCIApplication instance = null;
    private ISharedPreferences preferences;

    public static StockHCIApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = ApplicationPreferences.getInstance(this);

    }

    public ISharedPreferences getPreferences()
    {
        return preferences;
    }

    public static class ApplicationPreferences implements ISharedPreferences
    {
        public String KEY_SHOW_VOLUME = "isShowVolume";

        private static ApplicationPreferences instance;

        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        private ApplicationPreferences(Context context)
        {
            preferences = context.getSharedPreferences(ApplicationPreferences.class.getSimpleName(), 2);
            editor = preferences.edit();
        }

        public static ApplicationPreferences getInstance(Context context)
        {
            if (instance == null)
                instance = new ApplicationPreferences(context);
            return instance;
        }
        @Override
        public boolean isShowVolume() {
            return preferences.getBoolean(KEY_SHOW_VOLUME, false);
        }

        @Override
        public void setIsShowVolume(boolean isShow) {
            editor.putBoolean(KEY_SHOW_VOLUME, isShow);
            editor.commit();
        }
    }
}
