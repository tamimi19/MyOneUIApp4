package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

/**
 * فراجمنت الإعدادات مع مكونات OneUI محسنة
 * يستخدم SeekBar و Switch العادية مع ستايلات OneUI
 */
public class SettingsFragment extends Fragment {

    private NestedScrollView scrollView;
    private LinearLayout contentLayout;
    private SeekBar volumeSeekBar;
    private SeekBar animationSeekBar;
    private Switch darkModeSwitch;
    private Switch vibrationSwitch;
    private Switch notificationSwitch;
    
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");
    private static final int ONEUI_SURFACE = Color.parseColor("#F5F5F5");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                           @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        
        // إنشاء التخطيط الرئيسي
        scrollView = createScrollableLayout();
        
        // إنشاء محتوى الإعدادات
        contentLayout = createContentLayout();
        
        // إضافة جميع إعدادات التطبيق
        addAllSettings();
        
        // إضافة المحتوى إلى التخطيط القابل للتمرير
        scrollView.addView(contentLayout);
        
        return scrollView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // إضافة تأثير الانتقال
        addTransitionEffect();
        
        // رسالة ترحيب
        Toast.makeText(requireContext(), 
            "مرحباً بك في إعدادات التطبيق!", 
            Toast.LENGTH_SHORT).show();
    }

    /**
     * إنشاء تخطيط قابل للتمرير
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
     * إنشاء تخطيط المحتوى
     */
    private LinearLayout createContentLayout() {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        return layout;
    }

    /**
     * إضافة جميع الإعدادات
     */
    private void addAllSettings() {
        // عنوان الإعدادات
        contentLayout.addView(createSettingsHeader());
        contentLayout.addView(createSpacer(24));
        
        // قسم الصوت
        contentLayout.addView(createSectionHeader("إعدادات الصوت"));
        contentLayout.addView(createVolumeSettings());
        contentLayout.addView(createSpacer(32));
        
        // قسم العرض
        contentLayout.addView(createSectionHeader("إعدادات العرض"));
        contentLayout.addView(createDisplaySettings());
        contentLayout.addView(createSpacer(32));
        
        // قسم الإشعارات
        contentLayout.addView(createSectionHeader("إعدادات الإشعارات"));
        contentLayout.addView(createNotificationSettings());
        contentLayout.addView(createSpacer(32));
        
        // قسم الأداء
        contentLayout.addView(createSectionHeader("إعدادات الأداء"));
        contentLayout.addView(createPerformanceSettings());
        
        // نصائح في النهاية
        contentLayout.addView(createSpacer(40));
        contentLayout.addView(createTipsSection());
    }

    /**
     * إنشاء عنوان الإعدادات
     */
    private TextView createSettingsHeader() {
        TextView header = new TextView(requireContext());
        header.setText("إعدادات التطبيق");
        header.setTextSize(24);
        header.setTextColor(ONEUI_BLUE);
        header.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        return header;
    }

    /**
     * إنشاء عنوان قسم
     */
    private TextView createSectionHeader(String title) {
        TextView sectionHeader = new TextView(requireContext());
        sectionHeader.setText(title);
        sectionHeader.setTextSize(18);
        sectionHeader.setTextColor(ONEUI_BLUE);
        sectionHeader.setPadding(0, 0, 0, dpToPx(16));
        sectionHeader.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        return sectionHeader;
    }

    /**
     * إنشاء إعدادات الصوت
     */
    private LinearLayout createVolumeSettings() {
        LinearLayout volumeLayout = new LinearLayout(requireContext());
        volumeLayout.setOrientation(LinearLayout.VERTICAL);
        
        // نص مستوى الصوت
        TextView volumeLabel = new TextView(requireContext());
        volumeLabel.setText("مستوى الصوت: 50%");
        volumeLabel.setTextSize(16);
        volumeLabel.setPadding(0, 0, 0, dpToPx(8));
        
        // شريط تمرير الصوت
        volumeSeekBar = new SeekBar(requireContext());
        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress(50);
        volumeSeekBar.setPadding(0, 0, 0, dpToPx(16));
        
        // إعداد مستمع التغيير
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    volumeLabel.setText("مستوى الصوت: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // بداية التفاعل
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(requireContext(), 
                    "تم تحديث مستوى الصوت إلى " + seekBar.getProgress() + "%", 
                    Toast.LENGTH_SHORT).show();
            }
        });
        
        volumeLayout.addView(volumeLabel);
        volumeLayout.addView(volumeSeekBar);
        
        return volumeLayout;
    }

    /**
     * إنشاء إعدادات العرض
     */
    private LinearLayout createDisplaySettings() {
        LinearLayout displayLayout = new LinearLayout(requireContext());
        displayLayout.setOrientation(LinearLayout.VERTICAL);
        
        // سرعة الرسوم المتحركة
        TextView animationLabel = new TextView(requireContext());
        animationLabel.setText("سرعة الرسوم المتحركة: 75%");
        animationLabel.setTextSize(16);
        animationLabel.setPadding(0, 0, 0, dpToPx(8));
        
        animationSeekBar = new SeekBar(requireContext());
        animationSeekBar.setMax(100);
        animationSeekBar.setProgress(75);
        animationSeekBar.setPadding(0, 0, 0, dpToPx(16));
        
        animationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    animationLabel.setText("سرعة الرسوم المتحركة: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(requireContext(), 
                    "تم تحديث سرعة الرسوم المتحركة", 
                    Toast.LENGTH_SHORT).show();
            }
        });
        
        // الوضع الداكن
        darkModeSwitch = createStyledSwitch("الوضع الداكن", true);
        
        displayLayout.addView(animationLabel);
        displayLayout.addView(animationSeekBar);
        displayLayout.addView(darkModeSwitch);
        
        return displayLayout;
    }

    /**
     * إنشاء إعدادات الإشعارات
     */
    private LinearLayout createNotificationSettings() {
        LinearLayout notificationLayout = new LinearLayout(requireContext());
        notificationLayout.setOrientation(LinearLayout.VERTICAL);
        
        // إعدادات الإشعارات
        notificationSwitch = createStyledSwitch("تفعيل الإشعارات", true);
        vibrationSwitch = createStyledSwitch("الاهتزاز مع الإشعارات", false);
        
        notificationLayout.addView(notificationSwitch);
        notificationLayout.addView(createSpacer(16));
        notificationLayout.addView(vibrationSwitch);
        
        return notificationLayout;
    }

    /**
     * إنشاء إعدادات الأداء
     */
    private LinearLayout createPerformanceSettings() {
        LinearLayout performanceLayout = new LinearLayout(requireContext());
        performanceLayout.setOrientation(LinearLayout.VERTICAL);
        
        Switch highPerformanceSwitch = createStyledSwitch("الأداء العالي", false);
        Switch batterySaverSwitch = createStyledSwitch("توفير البطارية", true);
        Switch backgroundSyncSwitch = createStyledSwitch("المزامنة في الخلفية", true);
        
        performanceLayout.addView(highPerformanceSwitch);
        performanceLayout.addView(createSpacer(16));
        performanceLayout.addView(batterySaverSwitch);
        performanceLayout.addView(createSpacer(16));
        performanceLayout.addView(backgroundSyncSwitch);
        
        return performanceLayout;
    }

    /**
     * إنشاء Switch مع تصميم OneUI
     */
    private Switch createStyledSwitch(String text, boolean defaultChecked) {
        Switch switchView = new Switch(requireContext());
        switchView.setText(text);
        switchView.setTextSize(16);
        switchView.setChecked(defaultChecked);
        switchView.setPadding(0, dpToPx(12), 0, dpToPx(12));
        
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "تم التفعيل" : "تم الإيقاف";
            Toast.makeText(requireContext(), 
                text + ": " + status, 
                Toast.LENGTH_SHORT).show();
        });
        
        return switchView;
    }

    /**
     * إنشاء قسم النصائح
     */
    private LinearLayout createTipsSection() {
        LinearLayout tipsLayout = new LinearLayout(requireContext());
        tipsLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tipsHeader = new TextView(requireContext());
        tipsHeader.setText("نصائح مفيدة");
        tipsHeader.setTextSize(18);
        tipsHeader.setTextColor(ONEUI_BLUE);
        tipsHeader.setPadding(0, 0, 0, dpToPx(16));
        
        TextView tipsContent = new TextView(requireContext());
        tipsContent.setText(
            "استخدم ميزة Pull-to-Reach لسهولة الوصول للعناصر العلوية\n" +
            "الوضع الداكن يوفر البطارية على الشاشات OLED\n" +
            "تقليل سرعة الرسوم المتحركة يحسن الأداء\n" +
            "إيقاف المزامنة في الخلفية يوفر البيانات\n" +
            "تفعيل الاهتزاز مفيد في البيئات الصاخبة\n\n" +
            "تم تطوير هذا التطبيق باستخدام مكتبات OneUI Project الأصلية لضمان أفضل تجربة مستخدم ممكنة على جميع أجهزة Android."
        );
        tipsContent.setTextSize(14);
        tipsContent.setTextColor(Color.GRAY);
        tipsContent.setLineSpacing(dpToPx(4), 1.3f);
        
        tipsLayout.addView(tipsHeader);
        tipsLayout.addView(tipsContent);
        
        return tipsLayout;
    }

    /**
     * إنشاء مساحة فارغة
     */
    private View createSpacer(int dp) {
        View spacer = new View(requireContext());
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(dp)));
        return spacer;
    }

    /**
     * إضافة تأثير الانتقال
     */
    private void addTransitionEffect() {
        if (getView() != null) {
            getView().setAlpha(0f);
            getView().animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start();
        }
    }

    /**
     * تحويل dp إلى pixels
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
                               }
