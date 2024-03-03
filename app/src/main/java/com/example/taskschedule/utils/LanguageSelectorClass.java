package com.example.taskschedule.utils;
/*
import java.util.Locale;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.ComponentActivity

@Singleton
class LanguageManager @Inject constructor() {

    // Current application's lang
    var currentLang: AppLanguage = AppLanguage.getFromCode(Locale.getDefault().language.lowercase())

    // Method to change the App's language setting a new locale
    fun changeLang(lang: AppLanguage, context: Context, recreate: Boolean = true) {

        // Check if there's any difference in language variables
        if (lang != currentLang || currentLang.code != Locale.getDefault().language) {

            // With the context create a new Locale and update configuration
            context.resources.apply {
                val locale = Locale(lang.code)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)

                @Suppress("DEPRECATION")
                        context.resources.updateConfiguration(config, displayMetrics)
            }

            // Update current language
            currentLang = lang

            // If asked recreate the interface (this does not finish the activity)
            if (recreate) context.getActivity()?.recreate()
        }
    }
}

 */