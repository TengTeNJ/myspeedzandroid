package com.potent.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.potent.common.Constants;

import java.util.Locale;

public class AppLanguageUtils {

    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context) {
        SharedPreferences m_wsp = SPUtils.getInstance(context, Constants.SPNAME, Context.MODE_PRIVATE);
        String Language = m_wsp.getString(Constants.Language, "English");
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = Language.equals("English") ? Locale.ENGLISH : Locale.FRANCE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        SharedPreferences m_wsp = SPUtils.getInstance(context, Constants.SPNAME, Context.MODE_PRIVATE);
        String Language = m_wsp.getString(Constants.Language, "English");
        Resources resources = context.getResources();
        Locale locale = Language.equals("English") ? Locale.ENGLISH : Locale.FRANCE;

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

}

