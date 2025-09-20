package com.example.oneuiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * MainActivity محدثة للعمل مع التخطيط الأساسي الجديد
 * تتضمن جميع الوظائف المطلوبة مع واجهة مستقرة
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // العناصر الأساسية من XML
    private TextView toolbarTitle;
    private FrameLayout contentContainer;
    private ScrollView homeContent;
    private Button btnScrollList;
    private Button btnSettings;
    private Button btnHome;
    private Button btnTestFeatures;

    // Fragment الحالي
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "بدء تهيئة التطبيق مع التخطيط الجديد");
        
        try {
            // تهيئة معالج الأخطاء
            initializeCrashHandler();
            
            // تحميل التخطيط
            setContentView(R.layout.activity_main);
            
            // تهيئة العناصر
            initializeViews();
            
            // إعداد المستمعات
            setupEventListeners();
            
            // عرض المحتوى الرئيسي افتراضياً
            showHomeContent();
            
            Log.d(TAG, "تم تهيئة التطبيق بنجاح");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ فادح في التهيئة", e);
            showErrorToast("فشل في تحميل التطبيق: " + e.getMessage());
        }
    }

    /**
     * تهيئة معالج الأخطاء
     */
    private void initializeCrashHandler() {
        try {
            CrashHandler.initialize(this);
            CrashHandler.cleanOldLogs(this);
            Log.d(TAG, "تم تهيئة معالج الأخطاء بنجاح");
        } catch (Exception e) {
            Log.w(TAG, "تحذير: فشل في تهيئة معالج الأخطاء", e);
        }
    }

    /**
     * تهيئة جميع العناصر من XML
     */
    private void initializeViews() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        contentContainer = findViewById(R.id.main_container);
        homeContent = findViewById(R.id.home_content);
        btnScrollList = findViewById(R.id.btn_scroll_list);
        btnSettings = findViewById(R.id.btn_settings);
        btnHome = findViewById(R.id.btn_home);
        btnTestFeatures = findViewById(R.id.btn_test_features);

        // التحقق من وجود العناصر الأساسية
        if (contentContainer == null) {
            throw new RuntimeException("لم يتم العثور على main_container");
        }

        Log.d(TAG, "تم تهيئة جميع العناصر بنجاح");
    }

    /**
     * إعداد مستمعات الأحداث للأزرار
     */
    private void setupEventListeners() {
        if (btnScrollList != null) {
            btnScrollList.setOnClickListener(v -> navigateToScrollFragment());
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> navigateToSettingsFragment());
        }

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> showHomeContent());
        }

        if (btnTestFeatures != null) {
            btnTestFeatures.setOnClickListener(v -> testAdvancedFeatures());
        }

        Log.d(TAG, "تم إعداد مستمعات الأحداث بنجاح");
    }

    /**
     * عرض المحتوى الرئيسي
     */
    private void showHomeContent() {
        try {
            updateToolbarTitle("تطبيق OneUI");
            
            // مسح Fragment الحالي
            clearCurrentFragment();
            
            // إظهار المحتوى الرئيسي
            if (homeContent != null) {
                homeContent.setVisibility(View.VISIBLE);
            }
            
            showSuccessToast("تم الرجوع للشاشة الرئيسية");
            Log.d(TAG, "تم عرض المحتوى الرئيسي بنجاح");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ في عرض المحتوى الرئيسي", e);
            showErrorToast("فشل في عرض المحتوى الرئيسي");
        }
    }

    /**
     * التنقل إلى قائمة التمرير
     */
    private void navigateToScrollFragment() {
        try {
            updateToolbarTitle("قائمة التمرير");
            loadFragment(new ScrollFragment(), "ScrollFragment");
            showSuccessToast("تم تحميل قائمة التمرير مع 200 عنصر");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ في تحميل قائمة التمرير", e);
            showErrorToast("فشل في تحميل قائمة التمرير");
        }
    }

    /**
     * التنقل إلى الإعدادات
     */
    private void navigateToSettingsFragment() {
        try {
            updateToolbarTitle("إعدادات التطبيق");
            loadFragment(new SettingsFragment(), "SettingsFragment");
            showSuccessToast("تم فتح إعدادات التطبيق");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ في تحميل الإعدادات", e);
            showErrorToast("فشل في تحميل الإعدادات");
        }
    }

    /**
     * اختبار الميزات المتقدمة
     */
    private void testAdvancedFeatures() {
        try {
            // إنشاء نافذة حوار لعرض معلومات التطبيق
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("الميزات المتقدمة")
                    .setMessage("التطبيق يتضمن الميزات التالية:\n\n" +
                            "✓ نظام تسجيل أخطاء متطور\n" +
                            "✓ واجهة متوافقة مع OneUI\n" +
                            "✓ إدارة ذكية للـ Fragments\n" +
                            "✓ تخطيط متجاوب للشاشات\n" +
                            "✓ معالجة شاملة للأخطاء\n\n" +
                            "جميع الميزات تعمل بشكل مستقل ومتوافق مع أجهزة Samsung وغيرها.")
                    .setPositiveButton("رائع!", null)
                    .setNeutralButton("معلومات التطبيق", (dialog, which) -> showAppInfo())
                    .show();
                    
            Log.d(TAG, "تم عرض معلومات الميزات المتقدمة");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ في عرض الميزات المتقدمة", e);
            showErrorToast("فشل في عرض المعلومات");
        }
    }

    /**
     * عرض معلومات التطبيق
     */
    private void showAppInfo() {
        try {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("معلومات التطبيق")
                    .setMessage("اسم التطبيق: تطبيق OneUI\n" +
                            "الإصدار: 1.0\n" +
                            "النظام: Android " + android.os.Build.VERSION.RELEASE + "\n" +
                            "الجهاز: " + android.os.Build.MODEL + "\n" +
                            "المطور: فريق التطوير\n\n" +
                            "تم تطوير التطبيق باستخدام مكتبات Samsung OneUI الرسمية لضمان أفضل تجربة مستخدم.")
                    .setPositiveButton("موافق", null)
                    .show();
                    
        } catch (Exception e) {
            Log.e(TAG, "خطأ في عرض معلومات التطبيق", e);
            showErrorToast("فشل في عرض معلومات التطبيق");
        }
    }

    /**
     * تحميل Fragment مع معالجة الأخطاء
     */
    private void loadFragment(Fragment fragment, String fragmentName) {
        if (contentContainer == null) {
            throw new RuntimeException("contentContainer غير متاح");
        }

        try {
            // إخفاء المحتوى الرئيسي
            if (homeContent != null) {
                homeContent.setVisibility(View.GONE);
            }

            // تحميل Fragment الجديد
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, fragment);
            transaction.addToBackStack(fragmentName);
            transaction.commit();

            currentFragment = fragment;

            Log.d(TAG, "تم تحميل " + fragmentName + " بنجاح");

        } catch (Exception e) {
            Log.e(TAG, "خطأ في تحميل " + fragmentName, e);
            // في حالة فشل تحميل Fragment، العودة للمحتوى الرئيسي
            showHomeContent();
            throw e;
        }
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
                
                Log.d(TAG, "تم مسح Fragment السابق");
                
            } catch (Exception e) {
                Log.w(TAG, "تحذير: مشكلة في إزالة Fragment", e);
            }
        }
    }

    /**
     * تحديث عنوان شريط الأدوات
     */
    private void updateToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    /**
     * عرض رسالة نجاح
     */
    private void showSuccessToast(String message) {
        Toast.makeText(this, "✓ " + message, Toast.LENGTH_SHORT).show();
    }

    /**
     * عرض رسالة خطأ
     */
    private void showErrorToast(String message) {
        Toast.makeText(this, "⚠ " + message, Toast.LENGTH_LONG).show();
    }

    /**
     * معالجة زر الرجوع
     */
    @Override
    public void onBackPressed() {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                showHomeContent();
            } else {
                // تأكيد الخروج من التطبيق
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("تأكيد الخروج")
                        .setMessage("هل تريد الخروج من التطبيق؟")
                        .setPositiveButton("خروج", (dialog, which) -> super.onBackPressed())
                        .setNegativeButton("إلغاء", null)
                        .show();
            }
        } catch (Exception e) {
            Log.e(TAG, "خطأ في معالجة زر الرجوع", e);
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
            if (toolbarTitle != null) {
                outState.putString("toolbar_title", toolbarTitle.getText().toString());
            }
            if (currentFragment != null) {
                outState.putString("current_fragment", currentFragment.getClass().getSimpleName());
            }
        } catch (Exception e) {
            Log.w(TAG, "تحذير: مشكلة في حفظ الحالة", e);
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
                String savedTitle = savedInstanceState.getString("toolbar_title", "تطبيق OneUI");
                updateToolbarTitle(savedTitle);
                
                String fragmentName = savedInstanceState.getString("current_fragment");
                if (fragmentName != null) {
                    Log.d(TAG, "تم استرداد حالة التطبيق مع Fragment: " + fragmentName);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "تحذير: مشكلة في استرداد الحالة", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "تم إغلاق التطبيق");
    }
                }
