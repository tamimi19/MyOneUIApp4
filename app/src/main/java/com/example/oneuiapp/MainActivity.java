package com.example.oneuiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

/**
 * MainActivity محسنة لاستخدام مكونات OneUI الأصلية
 * تعتمد على ملف XML بدلاً من إنشاء العناصر برمجياً
 */
public class MainActivity extends AppCompatActivity {

    // العناصر الأساسية من XML
    private DrawerLayout drawerLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private FrameLayout contentContainer;
    private LinearLayout homeContent;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    
    // الأزرار
    private MaterialButton btnScrollList;
    private MaterialButton btnSettings;
    private MaterialButton btnHome;
    
    // Fragment الحالي
    private Fragment currentFragment;

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
            // تحميل التخطيط من XML
            setContentView(R.layout.activity_main);
            
            // تهيئة العناصر
            initializeViews();
            
            // إعداد الواجهة
            setupUserInterface();
            
            // إعداد المستمعات
            setupEventListeners();
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ فادح في تهيئة التطبيق", e);
            showErrorMessage("فشل في تحميل التطبيق");
        }
    }

    /**
     * تهيئة جميع العناصر من XML
     */
    private void initializeViews() {
        // العناصر الأساسية
        drawerLayout = findViewById(R.id.drawer_layout);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        contentContainer = findViewById(R.id.main_container);
        homeContent = findViewById(R.id.home_content);
        nestedScrollView = findViewById(R.id.nested_scroll);
        appBarLayout = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        navigationView = findViewById(R.id.nav_view);
        
        // الأزرار
        btnScrollList = findViewById(R.id.btn_scroll_list);
        btnSettings = findViewById(R.id.btn_settings);
        btnHome = findViewById(R.id.btn_home);
        
        // التحقق من وجود العناصر الأساسية
        if (contentContainer == null || homeContent == null) {
            throw new RuntimeException("فشل في العثور على العناصر الأساسية في التخطيط");
        }
        
        Log.d("MainActivity", "تم تهيئة جميع العناصر بنجاح");
    }

    /**
     * إعداد الواجهة الأساسية
     */
    private void setupUserInterface() {
        // إعداد شريط الأدوات
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        
        // إعداد CollapsingToolbar
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle("تطبيق OneUI");
        }
        
        // إعداد FAB
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Toast.makeText(this, "الإجراءات السريعة قريباً!", Toast.LENGTH_SHORT).show();
            });
        }
        
        // إظهار المحتوى الرئيسي افتراضياً
        showHomeContent();
        
        Log.d("MainActivity", "تم إعداد الواجهة بنجاح");
    }

    /**
     * إعداد مستمعات الأحداث
     */
    private void setupEventListeners() {
        // مستمعات الأزرار الرئيسية
        if (btnScrollList != null) {
            btnScrollList.setOnClickListener(v -> navigateToScrollFragment());
        }
        
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> navigateToSettingsFragment());
        }
        
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> showHomeContent());
        }
        
        // مستمعات القائمة الجانبية
        if (navigationView != null) {
            View navHome = navigationView.findViewById(R.id.nav_home);
            View navSettings = navigationView.findViewById(R.id.nav_settings);
            View navAbout = navigationView.findViewById(R.id.nav_about);
            
            if (navHome != null) {
                navHome.setOnClickListener(v -> {
                    showHomeContent();
                    closeDrawer();
                });
            }
            
            if (navSettings != null) {
                navSettings.setOnClickListener(v -> {
                    navigateToSettingsFragment();
                    closeDrawer();
                });
            }
            
            if (navAbout != null) {
                navAbout.setOnClickListener(v -> {
                    showAboutDialog();
                    closeDrawer();
                });
            }
        }
        
        Log.d("MainActivity", "تم إعداد مستمعات الأحداث بنجاح");
    }

    /**
     * عرض المحتوى الرئيسي
     */
    private void showHomeContent() {
        try {
            // مسح Fragment الحالي إن وجد
            clearCurrentFragment();
            
            // إظهار المحتوى الرئيسي
            if (homeContent != null) {
                homeContent.setVisibility(View.VISIBLE);
            }
            
            // تحديث العنوان
            updateTitle("تطبيق OneUI");
            
            Log.d("MainActivity", "تم عرض المحتوى الرئيسي");
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في عرض المحتوى الرئيسي", e);
            showErrorMessage("فشل في عرض المحتوى الرئيسي");
        }
    }

    /**
     * التنقل إلى قائمة التمرير
     */
    private void navigateToScrollFragment() {
        try {
            updateTitle("قائمة التمرير");
            loadFragment(new ScrollFragment());
            showToast("تم تحميل قائمة التمرير");
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تحميل قائمة التمرير", e);
            showToast("فشل في تحميل قائمة التمرير");
        }
    }

    /**
     * التنقل إلى الإعدادات
     */
    private void navigateToSettingsFragment() {
        try {
            updateTitle("الإعدادات");
            loadFragment(new SettingsFragment());
            showToast("تم فتح الإعدادات");
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تحميل الإعدادات", e);
            showToast("فشل في تحميل الإعدادات");
        }
    }

    /**
     * تحميل Fragment مع معالجة الأخطاء
     */
    private void loadFragment(Fragment fragment) {
        if (contentContainer == null) {
            throw new RuntimeException("contentContainer غير متاح");
        }
        
        // إخفاء المحتوى الرئيسي
        if (homeContent != null) {
            homeContent.setVisibility(View.GONE);
        }
        
        // تحميل Fragment الجديد
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
                
                Log.d("MainActivity", "تم مسح Fragment السابق");
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
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * إغلاق القائمة الجانبية
     */
    private void closeDrawer() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        }
    }

    /**
     * عرض نافذة حول التطبيق
     */
    private void showAboutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("حول تطبيق OneUI")
                .setMessage("تطبيق تجريبي مطور باستخدام مكتبات Samsung OneUI الرسمية\n\nالإصدار: 1.0\nالمطور: فريق التطوير")
                .setPositiveButton("موافق", null)
                .show();
    }

    /**
     * عرض رسالة خطأ
     */
    private void showErrorMessage(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("خطأ")
                .setMessage(message)
                .setPositiveButton("موافق", null)
                .setNeutralButton("إعادة المحاولة", (dialog, which) -> recreate())
                .show();
    }

    /**
     * عرض رسالة Toast
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * معالجة زر الرجوع
     */
    @Override
    public void onBackPressed() {
        try {
            // إغلاق القائمة الجانبية إذا كانت مفتوحة
            if (drawerLayout != null && drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
                return;
            }
            
            // العودة للرئيسية إذا كان هناك Fragment
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                showHomeContent();
                return;
            }
            
            // الخروج من التطبيق
            super.onBackPressed();
            
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
