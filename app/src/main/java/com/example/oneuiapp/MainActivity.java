package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * MainActivity مبسطة ومحسنة للاستقرار
 * تركز على الوظائف الأساسية مع تجنب التعقيدات غير الضرورية
 */
public class MainActivity extends AppCompatActivity {

    // المتغيرات الأساسية
    private CollapsingToolbarLayout collapsingToolbar;
    private FrameLayout contentContainer;
    // تم إزالة SwipeRefreshLayout لحل مشكلة ArrayIndexOutOfBoundsException
    private Fragment currentFragment;
    
    // الألوان الثابتة
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");
    private static final int TEXT_PRIMARY = Color.parseColor("#212121");
    private static final int TEXT_SECONDARY = Color.parseColor("#757575");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // تهيئة معالج الأخطاء
        try {
            CrashHandler.initialize(this);
            CrashHandler.cleanOldLogs(this);
        } catch (Exception e) {
            Log.w("MainActivity", "تحذير: فشل في تهيئة معالج الأخطاء", e);
        }
        
        try {
            // تحميل التخطيط
            setContentView(R.layout.activity_main);
            
            // تهيئة العناصر
            initializeComponents();
            
            // إعداد الواجهة
            setupInterface();
            
            // تحميل المحتوى الأساسي
            displayHomeContent();
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ فادح في تهيئة التطبيق", e);
            createEmergencyInterface();
        }
    }

    /**
     * تهيئة المكونات الأساسية
     */
    private void initializeComponents() {
        // البحث عن العناصر مع معالجة الأخطاء - تم إزالة swipeRefreshLayout
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        contentContainer = findViewById(R.id.main_container);
        
        // التحقق من وجود العناصر الأساسية
        if (contentContainer == null) {
            throw new RuntimeException("لم يتم العثور على main_container في التخطيط");
        }
        
        Log.d("MainActivity", "تم تهيئة المكونات بنجاح");
    }

    /**
     * إعداد الواجهة الأساسية
     */
    private void setupInterface() {
        // إعداد العنوان - تم إزالة إعداد SwipeRefreshLayout لحل مشكلة ArrayIndexOutOfBoundsException
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle("تطبيق OneUI");
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        }
        
        Log.d("MainActivity", "تم إعداد الواجهة بنجاح");
    }

    /**
     * عرض المحتوى الرئيسي
     */
    private void displayHomeContent() {
        try {
            // مسح المحتوى السابق
            contentContainer.removeAllViews();
            
            // إنشاء التخطيط الرئيسي
            LinearLayout mainLayout = createMainLayout();
            
            // إضافة المحتوى
            addWelcomeSection(mainLayout);
            addNavigationButtons(mainLayout);
            addInformationSection(mainLayout);
            
            // عرض التخطيط
            contentContainer.addView(mainLayout);
            
            Log.d("MainActivity", "تم عرض المحتوى الرئيسي بنجاح");
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في عرض المحتوى الرئيسي", e);
            displayErrorContent("خطأ في تحميل المحتوى الرئيسي");
        }
    }

    /**
     * إنشاء التخطيط الأساسي
     */
    private LinearLayout createMainLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
        
        return layout;
    }

    /**
     * إضافة قسم الترحيب
     */
    private void addWelcomeSection(LinearLayout parent) {
        // العنوان الرئيسي
        TextView titleView = new TextView(this);
        titleView.setText("مرحباً بك في تطبيق OneUI");
        titleView.setTextSize(24);
        titleView.setTextColor(ONEUI_BLUE);
        titleView.setPadding(0, 0, 0, dpToPx(8));
        parent.addView(titleView);
        
        // الوصف
        TextView descriptionView = new TextView(this);
        descriptionView.setText("تطبيق محسن باستخدام مكتبات Samsung OneUI مع ميزات متقدمة للتنقل والتفاعل");
        descriptionView.setTextSize(16);
        descriptionView.setTextColor(TEXT_SECONDARY);
        descriptionView.setLineSpacing(dpToPx(4), 1.2f);
        descriptionView.setPadding(0, 0, 0, dpToPx(24));
        parent.addView(descriptionView);
    }

    /**
     * إضافة أزرار التنقل
     */
    private void addNavigationButtons(LinearLayout parent) {
        // زر قائمة التمرير
        Button scrollButton = createNavigationButton("📋 قائمة التمرير", "عرض 200 عنصر مع تمرير محسن");
        scrollButton.setOnClickListener(v -> navigateToScrollFragment());
        parent.addView(scrollButton);
        
        addSpacing(parent, 16);
        
        // زر الإعدادات
        Button settingsButton = createNavigationButton("⚙️ الإعدادات", "تخصيص واجهة التطبيق");
        settingsButton.setOnClickListener(v -> navigateToSettingsFragment());
        parent.addView(settingsButton);
        
        addSpacing(parent, 16);
        
        // زر الرئيسية
        Button homeButton = createNavigationButton("🏠 الرئيسية", "العودة للشاشة الرئيسية");
        homeButton.setOnClickListener(v -> returnToHome());
        parent.addView(homeButton);
    }

    /**
     * إنشاء زر تنقل محسن
     */
    private Button createNavigationButton(String title, String subtitle) {
        Button button = new Button(this);
        button.setText(title + "\n" + subtitle);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(70)));
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(ONEUI_BLUE);
        button.setAllCaps(false);
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        
        return button;
    }

    /**
     * إضافة قسم المعلومات
     */
    private void addInformationSection(LinearLayout parent) {
        addSpacing(parent, 32);
        
        TextView infoTitle = new TextView(this);
        infoTitle.setText("ميزات التطبيق:");
        infoTitle.setTextSize(18);
        infoTitle.setTextColor(TEXT_PRIMARY);
        infoTitle.setPadding(0, 0, 0, dpToPx(12));
        parent.addView(infoTitle);
        
        TextView infoContent = new TextView(this);
        infoContent.setText(
            "• واجهة مستخدم محسنة بتقنية Samsung OneUI\n" +
            "• قائمة تمرير متقدمة مع 200 عنصر\n" +
            "• إعدادات شاملة للتخصيص\n" +
            "• دعم السحب للتحديث\n" +
            "• تسجيل تلقائي للأخطاء\n" +
            "• تصميم متجاوب يدعم جميع أحجام الشاشات");
        infoContent.setTextSize(14);
        infoContent.setTextColor(TEXT_SECONDARY);
        infoContent.setLineSpacing(dpToPx(6), 1.4f);
        parent.addView(infoContent);
    }

    /**
     * التنقل إلى قائمة التمرير
     */
    private void navigateToScrollFragment() {
        try {
            updateTitle("قائمة التمرير");
            loadFragment(new ScrollFragment());
            showMessage("تم تحميل قائمة التمرير");
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تحميل قائمة التمرير", e);
            showMessage("فشل في تحميل قائمة التمرير");
        }
    }

    /**
     * التنقل إلى الإعدادات
     */
    private void navigateToSettingsFragment() {
        try {
            updateTitle("الإعدادات");
            loadFragment(new SettingsFragment());
            showMessage("تم فتح الإعدادات");
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تحميل الإعدادات", e);
            showMessage("فشل في تحميل الإعدادات");
        }
    }

    /**
     * العودة للرئيسية
     */
    private void returnToHome() {
        try {
            updateTitle("تطبيق OneUI");
            clearCurrentFragment();
            displayHomeContent();
            showMessage("تم الرجوع للرئيسية");
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في الرجوع للرئيسية", e);
            showMessage("حدث خطأ أثناء الرجوع للرئيسية");
        }
    }

    /**
     * تحميل Fragment مع معالجة الأخطاء
     */
    private void loadFragment(Fragment fragment) {
        if (contentContainer == null) {
            throw new RuntimeException("contentContainer غير متاح");
        }
        
        // إزالة المحتوى الحالي
        contentContainer.removeAllViews();
        
        // إضافة Fragment الجديد
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        
        currentFragment = fragment;
        
        Log.d("MainActivity", "تم تحميل Fragment بنجاح: " + fragment.getClass().getSimpleName());
    }

    /**
     * مسح Fragment الحالي
     */
    private void clearCurrentFragment() {
        if (currentFragment != null) {
            try {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(currentFragment);
                transaction.commit();
                currentFragment = null;
            } catch (Exception e) {
                Log.w("MainActivity", "تحذير: مشكلة في إزالة Fragment", e);
            }
        }
    }

    /**
     * تحديث العنوان
     */
    private void updateTitle(String title) {
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(title);
        }
    }

    /**
     * معالجة التحديث البديل - تم إزالة SwipeRefreshLayout لحل مشكلة ArrayIndexOutOfBoundsException
     * يتم التحديث الآن عبر الأزرار أو التنقل
     */
    private void handleManualRefresh() {
        try {
            // تحديث المحتوى الحالي
            if (currentFragment != null) {
                // إعادة تحميل Fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.detach(currentFragment);
                transaction.attach(currentFragment);
                transaction.commit();
            } else {
                // تحديث المحتوى الرئيسي
                displayHomeContent();
            }
            
            showMessage("تم التحديث بنجاح");
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في التحديث", e);
            showMessage("فشل في التحديث");
        }
    }

    /**
     * عرض رسالة خطأ
     */
    private void displayErrorContent(String errorMessage) {
        try {
            contentContainer.removeAllViews();
            
            LinearLayout errorLayout = new LinearLayout(this);
            errorLayout.setOrientation(LinearLayout.VERTICAL);
            errorLayout.setPadding(dpToPx(20), dpToPx(40), dpToPx(20), dpToPx(40));
            errorLayout.setGravity(android.view.Gravity.CENTER);
            
            TextView errorText = new TextView(this);
            errorText.setText("⚠️ " + errorMessage);
            errorText.setTextSize(18);
            errorText.setTextColor(Color.RED);
            errorText.setGravity(android.view.Gravity.CENTER);
            
            Button retryButton = new Button(this);
            retryButton.setText("إعادة المحاولة");
            retryButton.setOnClickListener(v -> displayHomeContent());
            
            errorLayout.addView(errorText);
            addSpacing(errorLayout, 24);
            errorLayout.addView(retryButton);
            
            contentContainer.addView(errorLayout);
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ حتى في عرض رسالة الخطأ!", e);
        }
    }

    /**
     * إنشاء واجهة طوارئ في حالة الفشل الكامل
     */
    private void createEmergencyInterface() {
        try {
            LinearLayout emergencyLayout = new LinearLayout(this);
            emergencyLayout.setOrientation(LinearLayout.VERTICAL);
            emergencyLayout.setBackgroundColor(Color.WHITE);
            emergencyLayout.setPadding(40, 100, 40, 100);
            emergencyLayout.setGravity(android.view.Gravity.CENTER);
            
            TextView emergencyText = new TextView(this);
            emergencyText.setText("🚨 خطأ نظام\n\nفشل في تحميل التطبيق\nيرجى التحقق من سجلات الأخطاء");
            emergencyText.setTextSize(16);
            emergencyText.setTextColor(Color.RED);
            emergencyText.setGravity(android.view.Gravity.CENTER);
            emergencyText.setLineSpacing(dpToPx(8), 1.5f);
            
            emergencyLayout.addView(emergencyText);
            setContentView(emergencyLayout);
            
        } catch (Exception e) {
            Log.e("MainActivity", "فشل حتى في إنشاء واجهة الطوارئ!", e);
        }
    }

    /**
     * إضافة مساحة فارغة
     */
    private void addSpacing(LinearLayout parent, int dp) {
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(dp)));
        parent.addView(spacer);
    }

    /**
     * عرض رسالة للمستخدم
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * تحويل dp إلى pixels
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * معالجة زر الرجوع
     */
    @Override
    public void onBackPressed() {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                returnToHome();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في معالجة زر الرجوع", e);
            super.onBackPressed();
        }
    }

    /**
     * حفظ حالة التطبيق
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (collapsingToolbar != null && collapsingToolbar.getTitle() != null) {
                outState.putString("current_title", collapsingToolbar.getTitle().toString());
            }
        } catch (Exception e) {
            Log.w("MainActivity", "تحذير: مشكلة في حفظ الحالة", e);
        }
    }

    /**
     * استرداد حالة التطبيق
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                String savedTitle = savedInstanceState.getString("current_title", "تطبيق OneUI");
                updateTitle(savedTitle);
            }
        } catch (Exception e) {
            Log.w("MainActivity", "تحذير: مشكلة في استرداد الحالة", e);
        }
    }
}
