package com.example.oneuiapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;

/**
 * فراجمنت محسن للإعدادات باستخدام مكونات Samsung One UI المتقدمة
 * 
 * الميزات الجديدة المضافة:
 * - واجهة إعدادات شاملة مع تصميم One UI
 * - حفظ واسترداد الإعدادات في SharedPreferences
 * - إعدادات متقدمة للعرض والتفاعل
 * - أزرار تحكم محسنة بألوان Samsung
 * - مؤشرات التقدم والتبديل المتطورة
 * - تخطيط قابل للتمرير لاستيعاب إعدادات كثيرة
 */
public class SettingsFragment extends Fragment {
    
    // مفاتيح حفظ الإعدادات
    private static final String PREFS_NAME = "OneUIAppPrefs";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_SOUND_VOLUME = "sound_volume";
    private static final String KEY_ANIMATION_SPEED = "animation_speed";
    private static final String KEY_LANGUAGE = "app_language";
    
    // مراجع للمكونات
    private SharedPreferences sharedPrefs;
    private Switch darkModeSwitch;
    private Switch notificationSwitch;
    private SeekBar volumeSeekBar;
    private SeekBar animationSeekBar;
    private TextView volumeValueText;
    private TextView animationValueText;
    
    // ألوان One UI المميزة
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");
    private static final int ONEUI_SURFACE = Color.parseColor("#F5F5F5");
    private static final int ONEUI_ON_SURFACE = Color.parseColor("#1C1C1C");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        
        // تهيئة SharedPreferences
        initializePreferences();
        
        // إنشاء التخطيط المحسن بدلاً من XML معقد
        NestedScrollView scrollView = createScrollableLayout();
        
        // إضافة جميع أقسام الإعدادات
        LinearLayout mainLayout = createMainSettingsLayout();
        scrollView.addView(mainLayout);
        
        // تحميل الإعدادات المحفوظة
        loadSavedSettings();
        
        return scrollView;
    }

    /**
     * تهيئة نظام حفظ الإعدادات باستخدام SharedPreferences
     * يضمن حفظ جميع اختيارات المستخدم بين جلسات التطبيق
     */
    private void initializePreferences() {
        sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
    }

    /**
     * إنشاء تخطيط قابل للتمرير لاستيعاب جميع الإعدادات
     * يستخدم NestedScrollView للتوافق مع مكتبات SESL
     */
    private NestedScrollView createScrollableLayout() {
        NestedScrollView scrollView = new NestedScrollView(requireContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setFillViewport(true);
        scrollView.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        
        return scrollView;
    }

    /**
     * إنشاء التخطيط الرئيسي لجميع أقسام الإعدادات
     * ينظم الإعدادات في مجموعات منطقية سهلة الاستخدام
     */
    private LinearLayout createMainSettingsLayout() {
        LinearLayout mainLayout = new LinearLayout(requireContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));

        // إضافة عنوان الصفحة
        mainLayout.addView(createPageTitle());
        mainLayout.addView(createSectionSpacer());
        
        // قسم المظهر والعرض
        mainLayout.addView(createDisplaySection());
        mainLayout.addView(createSectionSpacer());
        
        // قسم الصوت والإشعارات
        mainLayout.addView(createSoundSection());
        mainLayout.addView(createSectionSpacer());
        
        // قسم الأداء والرسوم المتحركة
        mainLayout.addView(createPerformanceSection());
        mainLayout.addView(createSectionSpacer());
        
        // قسم اللغة والمنطقة
        mainLayout.addView(createLanguageSection());
        mainLayout.addView(createSectionSpacer());
        
        // أزرار الإجراءات
        mainLayout.addView(createActionButtons());
        
        return mainLayout;
    }

    /**
     * إنشاء عنوان مرحب للصفحة يشرح الغرض من الإعدادات
     */
    private TextView createPageTitle() {
        TextView titleText = new TextView(requireContext());
        titleText.setText("⚙️ إعدادات التطبيق\n\nخصص تجربتك مع تطبيق OneUI حسب تفضيلاتك الشخصية");
        titleText.setTextSize(18);
        titleText.setTextColor(ONEUI_ON_SURFACE);
        titleText.setLineSpacing(dpToPx(4), 1.3f);
        titleText.setPadding(0, 0, 0, dpToPx(16));
        
        return titleText;
    }

    /**
     * إنشاء قسم إعدادات العرض والمظهر
     * يتضمن خيارات الوضع الداكن وتخصيصات البصرية
     */
    private LinearLayout createDisplaySection() {
        LinearLayout section = createSectionContainer("🎨 العرض والمظهر");
        
        // تبديل الوضع الداكن مع شرح مفصل
        LinearLayout darkModeRow = createSettingRow(
            "الوضع الداكن", 
            "يقلل إجهاد العين في الإضاءة المنخفضة ويوفر البطارية على شاشات OLED");
        
        darkModeSwitch = new Switch(requireContext());
        darkModeSwitch.setLayoutParams(createSwitchLayoutParams());
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleDarkModeToggle(isChecked);
        });
        
        darkModeRow.addView(darkModeSwitch);
        section.addView(darkModeRow);
        
        return section;
    }

    /**
     * إنشاء قسم إعدادات الصوت والإشعارات
     * يوفر تحكماً دقيقاً في مستوى الصوت وأنواع الإشعارات
     */
    private LinearLayout createSoundSection() {
        LinearLayout section = createSectionContainer("🔊 الصوت والإشعارات");
        
        // تبديل الإشعارات
        LinearLayout notificationRow = createSettingRow(
            "تفعيل الإشعارات", 
            "السماح بعرض إشعارات التطبيق والتحديثات المهمة");
        
        notificationSwitch = new Switch(requireContext());
        notificationSwitch.setLayoutParams(createSwitchLayoutParams());
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleNotificationToggle(isChecked);
        });
        
        notificationRow.addView(notificationSwitch);
        section.addView(notificationRow);
        
        // مؤشر مستوى الصوت مع نص ديناميكي
        section.addView(createVolumeControl());
        
        return section;
    }

    /**
     * إنشاء قسم إعدادات الأداء والرسوم المتحركة
     * يسمح بتخصيص سرعة الانتقالات والتأثيرات البصرية
     */
    private LinearLayout createPerformanceSection() {
        LinearLayout section = createSectionContainer("⚡ الأداء والرسوم المتحركة");
        
        // مؤشر سرعة الرسوم المتحركة
        section.addView(createAnimationSpeedControl());
        
        return section;
    }

    /**
     * إنشاء عنصر تحكم مستوى الصوت مع مؤشر قيمة ديناميكي
     * يستخدم SeekBar العادي مع تصميم OneUI
     */
    private LinearLayout createVolumeControl() {
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(0, dpToPx(16), 0, dpToPx(8));
        
        // عنوان وشرح عنصر التحكم
        TextView volumeLabel = new TextView(requireContext());
        volumeLabel.setText("مستوى الصوت");
        volumeLabel.setTextSize(16);
        volumeLabel.setTextColor(ONEUI_ON_SURFACE);
        volumeLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView volumeDescription = new TextView(requireContext());
        volumeDescription.setText("تحكم في مستوى أصوات التطبيق والتنبيهات");
        volumeDescription.setTextSize(14);
        volumeDescription.setTextColor(Color.GRAY);
        volumeDescription.setPadding(0, dpToPx(2), 0, dpToPx(8));
        
        // إنشاء SeekBar للتحكم في الصوت
        volumeSeekBar = new SeekBar(requireContext());
        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress(50); // القيمة الافتراضية
        volumeSeekBar.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        // نص عرض القيمة الحالية
        volumeValueText = new TextView(requireContext());
        volumeValueText.setText("50%");
        volumeValueText.setTextSize(14);
        volumeValueText.setTextColor(ONEUI_BLUE);
        volumeValueText.setGravity(android.view.Gravity.CENTER);
        volumeValueText.setPadding(0, dpToPx(4), 0, 0);
        
        // مستمع تغيير القيمة مع تحديث فوري للنص
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    volumeValueText.setText(progress + "%");
                    saveVolumeSettings(progress);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // يمكن إضافة تأثيرات بصرية عند بدء السحب
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                showMessage("تم تعديل مستوى الصوت إلى " + seekBar.getProgress() + "%");
            }
        });
        
        container.addView(volumeLabel);
        container.addView(volumeDescription);
        container.addView(volumeSeekBar);
        container.addView(volumeValueText);
        
        return container;
    }

    /**
     * إنشاء عنصر تحكم سرعة الرسوم المتحركة
     * يسمح للمستخدم بتخصيص سرعة انتقالات الواجهة حسب تفضيله
     */
    private LinearLayout createAnimationSpeedControl() {
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(0, dpToPx(16), 0, dpToPx(8));
        
        TextView animationLabel = new TextView(requireContext());
        animationLabel.setText("سرعة الرسوم المتحركة");
        animationLabel.setTextSize(16);
        animationLabel.setTextColor(ONEUI_ON_SURFACE);
        animationLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView animationDescription = new TextView(requireContext());
        animationDescription.setText("تحكم في سرعة انتقالات الواجهة والتأثيرات البصرية");
        animationDescription.setTextSize(14);
        animationDescription.setTextColor(Color.GRAY);
        animationDescription.setPadding(0, dpToPx(2), 0, dpToPx(8));
        
        animationSeekBar = new SeekBar(requireContext());
        animationSeekBar.setMax(200); // من 0% إلى 200%
        animationSeekBar.setProgress(100); // السرعة العادية كافتراضي
        animationSeekBar.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        animationValueText = new TextView(requireContext());
        animationValueText.setText("عادي (100%)");
        animationValueText.setTextSize(14);
        animationValueText.setTextColor(ONEUI_BLUE);
        animationValueText.setGravity(android.view.Gravity.CENTER);
        animationValueText.setPadding(0, dpToPx(4), 0, 0);
        
        animationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateAnimationSpeedText(progress);
                    saveAnimationSettings(progress);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String speedText = getAnimationSpeedDescription(seekBar.getProgress());
                showMessage("تم تعديل سرعة الرسوم المتحركة إلى " + speedText);
            }
        });
        
        container.addView(animationLabel);
        container.addView(animationDescription);
        container.addView(animationSeekBar);
        container.addView(animationValueText);
        
        return container;
    }

    /**
     * إنشاء قسم إعدادات اللغة والمنطقة
     * يوفر خيارات تغيير لغة التطبيق وتفضيلات المنطقة
     */
    private LinearLayout createLanguageSection() {
        LinearLayout section = createSectionContainer("🌍 اللغة والمنطقة");
        
        // زر تغيير اللغة مع معلومات اللغة الحالية
        Button languageButton = createActionButton(
            "تغيير اللغة", 
            "العربية (الافتراضي)");
        languageButton.setOnClickListener(v -> showLanguageOptions());
        section.addView(languageButton);
        
        // زر إعدادات المنطقة
        Button regionButton = createActionButton(
            "إعدادات المنطقة", 
            "تخصيص تنسيق التاريخ والأرقام");
        regionButton.setOnClickListener(v -> showRegionSettings());
        section.addView(regionButton);
        
        return section;
    }

    /**
     * إنشاء مجموعة أزرار الإجراءات المتقدمة
     * تتضمن خيارات إعادة التعيين والنسخ الاحتياطي
     */
    private LinearLayout createActionButtons() {
        LinearLayout section = createSectionContainer("🔧 إجراءات متقدمة");
        
        // زر حفظ الإعدادات
        Button saveButton = createPrimaryActionButton("حفظ جميع الإعدادات");
        saveButton.setOnClickListener(v -> saveAllSettings());
        section.addView(saveButton);
        
        section.addView(createButtonSpacer());
        
        // زر إعادة تعيين الإعدادات
        Button resetButton = createSecondaryActionButton("إعادة تعيين للافتراضي");
        resetButton.setOnClickListener(v -> resetToDefaultSettings());
        section.addView(resetButton);
        
        return section;
    }

    /**
     * إنشاء حاوية قسم مع عنوان منسق
     * يوفر تخطيطاً موحداً لجميع أقسام الإعدادات
     */
    private LinearLayout createSectionContainer(String title) {
        LinearLayout section = new LinearLayout(requireContext());
        section.setOrientation(LinearLayout.VERTICAL);
        section.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        section.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
        section.setBackgroundColor(ONEUI_SURFACE);
        
        // عنوان القسم
        TextView sectionTitle = new TextView(requireContext());
        sectionTitle.setText(title);
        sectionTitle.setTextSize(18);
        sectionTitle.setTextColor(ONEUI_BLUE);
        sectionTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        sectionTitle.setPadding(0, 0, 0, dpToPx(12));
        
        section.addView(sectionTitle);
        return section;
    }

    /**
     * إنشاء صف إعداد مع عنوان ووصف
     * يوفر تخطيطاً موحداً للإعدادات التي تحتوي على مفاتيح تبديل
     */
    private LinearLayout createSettingRow(String title, String description) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setPadding(0, dpToPx(8), 0, dpToPx(8));
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // حاوية النصوص
        LinearLayout textContainer = new LinearLayout(requireContext());
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        
        TextView titleText = new TextView(requireContext());
        titleText.setText(title);
        titleText.setTextSize(16);
        titleText.setTextColor(ONEUI_ON_SURFACE);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView descText = new TextView(requireContext());
        descText.setText(description);
        descText.setTextSize(14);
        descText.setTextColor(Color.GRAY);
        descText.setPadding(0, dpToPx(2), 0, 0);
        
        textContainer.addView(titleText);
        textContainer.addView(descText);
        row.addView(textContainer);
        
        return row;
    }

    /**
     * معالجة تبديل الوضع الداكن مع حفظ الإعداد وتطبيق التغيير فورياً
     */
    private void handleDarkModeToggle(boolean isEnabled) {
        // حفظ الإعداد في SharedPreferences
        sharedPrefs.edit().putBoolean(KEY_DARK_MODE, isEnabled).apply();
        
        // تطبيق التغيير على التطبيق فورياً
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            showMessage("تم تفعيل الوضع الداكن - يوفر طاقة البطارية ويقلل إجهاد العين");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            showMessage("تم تفعيل الوضع الفاتح - مثالي للاستخدام في الإضاءة الجيدة");
        }
    }

    /**
     * معالجة تبديل الإشعارات مع شرح تأثير التغيير
     */
    private void handleNotificationToggle(boolean isEnabled) {
        sharedPrefs.edit().putBoolean(KEY_NOTIFICATIONS, isEnabled).apply();
        
        String message = isEnabled ? 
            "تم تفعيل الإشعارات - ستصلك التحديثات المهمة" : 
            "تم إيقاف الإشعارات - لن تصلك أي تنبيهات من التطبيق";
        showMessage(message);
    }

    /**
     * حفظ إعدادات مستوى الصوت
     */
    private void saveVolumeSettings(int volume) {
        sharedPrefs.edit().putInt(KEY_SOUND_VOLUME, volume).apply();
    }

    /**
     * حفظ إعدادات سرعة الرسوم المتحركة
     */
    private void saveAnimationSettings(int speed) {
        sharedPrefs.edit().putInt(KEY_ANIMATION_SPEED, speed).apply();
    }

    /**
     * تحديث نص سرعة الرسوم المتحركة بناءً على القيمة المحددة
     */
    private void updateAnimationSpeedText(int progress) {
        String speedText;
        if (progress <= 25) {
            speedText = "بطيء جداً (" + progress + "%)";
        } else if (progress <= 75) {
            speedText = "بطيء (" + progress + "%)";
        } else if (progress <= 125) {
            speedText = "عادي (" + progress + "%)";
        } else if (progress <= 175) {
            speedText = "سريع (" + progress + "%)";
        } else {
            speedText = "سريع جداً (" + progress + "%)";
        }
        animationValueText.setText(speedText);
    }

    /**
     * الحصول على وصف سرعة الرسوم المتحركة
     */
    private String getAnimationSpeedDescription(int progress) {
        if (progress <= 25) return "بطيء جداً";
        if (progress <= 75) return "بطيء";
        if (progress <= 125) return "عادي";
        if (progress <= 175) return "سريع";
        return "سريع جداً";
    }

    /**
     * تحميل جميع الإعدادات المحفوظة وتطبيقها على الواجهة
     */
    private void loadSavedSettings() {
        // تحميل إعداد الوضع الداكن
        boolean darkMode = sharedPrefs.getBoolean(KEY_DARK_MODE, false);
        if (darkModeSwitch != null) {
            darkModeSwitch.setChecked(darkMode);
        }
        
        // تحميل إعداد الإشعارات
        boolean notifications = sharedPrefs.getBoolean(KEY_NOTIFICATIONS, true);
        if (notificationSwitch != null) {
            notificationSwitch.setChecked(notifications);
        }
        
        // تحميل مستوى الصوت
        int volume = sharedPrefs.getInt(KEY_SOUND_VOLUME, 50);
        if (volumeSeekBar != null && volumeValueText != null) {
            volumeSeekBar.setProgress(volume);
            volumeValueText.setText(volume + "%");
        }
        
        // تحميل سرعة الرسوم المتحركة
        int animationSpeed = sharedPrefs.getInt(KEY_ANIMATION_SPEED, 100);
        if (animationSeekBar != null && animationValueText != null) {
            animationSeekBar.setProgress(animationSpeed);
            updateAnimationSpeedText(animationSpeed);
        }
    }

    /**
     * عرض خيارات اللغة المتاحة
     */
    private void showLanguageOptions() {
        String[] languages = {"العربية", "English", "Français", "Deutsch", "한국어", "日本語"};
        String message = "اللغات المتاحة:\n";
        for (int i = 0; i < languages.length; i++) {
            message += (i + 1) + ". " + languages[i] + "\n";
        }
        message += "\nميزة تغيير اللغة ستكون متاحة في التحديث القادم";
        
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * عرض إعدادات المنطقة
     */
    private void showRegionSettings() {
        showMessage("إعدادات المنطقة: تنسيق التاريخ الهجري/الميلادي، نظام الأرقام، اتجاه النص\nقيد التطوير في الإصدار القادم");
    }

    /**
     * حفظ جميع الإعدادات الحالية
     */
    private void saveAllSettings() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        
        if (darkModeSwitch != null) {
            editor.putBoolean(KEY_DARK_MODE, darkModeSwitch.isChecked());
        }
        if (notificationSwitch != null) {
            editor.putBoolean(KEY_NOTIFICATIONS, notificationSwitch.isChecked());
        }
        if (volumeSeekBar != null) {
            editor.putInt(KEY_SOUND_VOLUME, volumeSeekBar.getProgress());
        }
        if (animationSeekBar != null) {
            editor.putInt(KEY_ANIMATION_SPEED, animationSeekBar.getProgress());
        }
        
        editor.apply();
        showMessage("✅ تم حفظ جميع الإعدادات بنجاح!");
    }

    /**
     * إعادة تعيين جميع الإعدادات إلى القيم الافتراضية
     */
    private void resetToDefaultSettings() {
        // إعادة تعيين الواجهة للقيم الافتراضية
        if (darkModeSwitch != null) darkModeSwitch.setChecked(false);
        if (notificationSwitch != null) notificationSwitch.setChecked(true);
        if (volumeSeekBar != null && volumeValueText != null) {
            volumeSeekBar.setProgress(50);
            volumeValueText.setText("50%");
        }
        if (animationSeekBar != null && animationValueText != null) {
            animationSeekBar.setProgress(100);
            updateAnimationSpeedText(100);
        }
        
        // حفظ القيم الافتراضية
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(KEY_DARK_MODE, false);
        editor.putBoolean(KEY_NOTIFICATIONS, true);
        editor.putInt(KEY_SOUND_VOLUME, 50);
        editor.putInt(KEY_ANIMATION_SPEED, 100);
        editor.apply();
        
        // تطبيق الوضع الفاتح
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        
        showMessage("🔄 تم إعادة تعيين جميع الإعدادات إلى الوضع الافتراضي");
    }

    /**
     * إنشاء أزرار الإجراءات المختلفة بتصاميم متمايزة
     */
    private Button createActionButton(String title, String subtitle) {
        Button button = new Button(requireContext());
        button.setText(title + "\n" + subtitle);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        button.setBackgroundColor(Color.parseColor("#E3F2FD"));
        button.setTextColor(ONEUI_BLUE);
        
        return button;
    }

    private Button createPrimaryActionButton(String text) {
        Button button = new Button(requireContext());
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setAllCaps(false);
        button.setTextSize(16);
        button.setTypeface(null, android.graphics.Typeface.BOLD);
        button.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        button.setBackgroundColor(ONEUI_BLUE);
        button.setTextColor(Color.WHITE);
        
        return button;
    }

    private Button createSecondaryActionButton(String text) {
        Button button = new Button(requireContext());
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        button.setBackgroundColor(Color.parseColor("#FFEBEE"));
        button.setTextColor(Color.parseColor("#C62828"));
        
        return button;
    }

    /**
     * إنشاء معاملات تخطيط موحدة لمفاتيح التبديل
     */
    private LinearLayout.LayoutParams createSwitchLayoutParams() {
        return new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * إنشاء مساحة بين الأقسام لتحسين التخطيط البصري
     */
    private View createSectionSpacer() {
        View spacer = new View(requireContext());
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(16)));
        return spacer;
    }

    private View createButtonSpacer() {
        View spacer = new View(requireContext());
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(8)));
        return spacer;
    }

    /**
     * تحويل وحدات dp إلى pixels للحصول على أبعاد دقيقة
     */
    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * عرض رسائل تأكيد وتوضيحية للمستخدم
     */
    private void showMessage(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
    }
