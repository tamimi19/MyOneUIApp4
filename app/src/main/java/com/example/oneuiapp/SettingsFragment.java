package com.example.oneuiapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // قائمة اللغة
        ListPreference langPref = findPreference("pref_language");
        langPref.setOnPreferenceChangeListener((pref, newValue) -> {
            String langCode = newValue.toString();
            // تغيير اللغة في التطبيق دون إعادة تشغيل
            LocaleHelper.setLocale(getContext(), langCode);
            recreateActivity();
            return true;
        });

        // قائمة الثيم
        ListPreference themePref = findPreference("pref_theme");
        themePref.setOnPreferenceChangeListener((pref, newValue) -> {
            String theme = newValue.toString();
            if ("light".equals(theme)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            return true;
        });

        // زر الإشعارات (على سبيل المثال، يفعل إعداد إشعارات)
        Preference notifPref = findPreference("pref_notifications");
        notifPref.setOnPreferenceClickListener(preference -> {
            // منطق فتح إعدادات الإشعارات أو إظهار Toast
            Toast.makeText(getContext(), "إعدادات الإشعارات", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void recreateActivity() {
        // إعادة إنشاء Activity الحالية لتطبيق اللغة الجديدة
        requireActivity().recreate();
    }
}
