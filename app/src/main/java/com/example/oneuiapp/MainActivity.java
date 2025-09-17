package com.example.oneuiapp;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * النشاط الرئيسي للتطبيق - يستخدم مكتبات OneUI لتوفير تجربة Samsung المحسنة
 * يحتوي على درج جانبي للتنقل وإدارة للفراجمنتس المختلفة
 */
public class MainActivity extends AppCompatActivity {

    // المتغيرات الأساسية للتطبيق
    private final List<Fragment> fragments = new ArrayList<>();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // إعداد شريط الأدوات مع دعم OneUI
        setupToolbar();
        
        // إعداد الدرج الجانبي للتنقل
        setupDrawerLayout();
        
        // تهيئة الفراجمنتس والمحتوى
        initializeFragments();
        
        // رسالة ترحيب تشير للميزة الخاصة بك
        showWelcomeMessage();
    }

    /**
     * إعداد شريط الأدوات مع تحسينات OneUI
     * يتضمن تفعيل أيقونة التنقل والعنوان المناسب
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            
            // تفعيل أيقونة الرجوع/القائمة
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("تطبيق OneUI");
            }
        }
    }

    /**
     * إعداد الدرج الجانبي مع مكونات OneUI المحسنة
     * يتضمن إعداد قائمة التنقل والسلوكيات المختلفة
     */
    private void setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawerLayout);
        
        // إعداد قائمة التنقل
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        if (drawerList != null) {
            drawerList.setLayoutManager(new LinearLayoutManager(this));
            // يمكن إضافة الـ adapter هنا عند الحاجة
        }
    }

    /**
     * تهيئة الفراجمنتس المختلفة للتطبيق
     * يبدأ بعرض الفراجمنت الأول افتراضياً
     */
    private void initializeFragments() {
        // إضافة فراجمنت بسيط للاختبار
        // يمكن إضافة المزيد من الفراجمنتس لاحقاً
        
        // عرض محتوى افتراضي في الحاوية الرئيسية
        // سيتم تطويره لاحقاً مع إضافة الفراجمنتس الفعلية
    }

    /**
     * عرض رسالة ترحيب تشير للميزة المطلوبة
     * تذكر المستخدم بإمكانية السحب للأسفل للوصول السهل
     */
    private void showWelcomeMessage() {
        Toast.makeText(this, 
            "مرحباً! استخدم ميزة السحب لأسفل للوصول السهل لعناصر أعلى الشاشة", 
            Toast.LENGTH_LONG).show();
    }

    /**
     * التعامل مع تغييرات إعدادات الجهاز مثل الدوران أو الوضع الداكن
     * يتضمن دعم خاص للإصدارات الأقدم من Android
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        // دعم خاص للإصدارات الأقدم من Android (قبل OneUI)
        if (Build.VERSION.SDK_INT <= 28) {
            // يمكن إضافة منطق إضافي هنا للتعامل مع الوضع الداكن في الإصدارات القديمة
        }
    }

    /**
     * التعامل مع نقرات عناصر شريط الأدوات
     * يتضمن منطق فتح وإغلاق الدرج الجانبي
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // تبديل حالة الدرج الجانبي
            handleDrawerToggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * منطق التعامل مع فتح وإغلاق الدرج الجانبي
     * يتحقق من الحالة الحالية ويبدلها
     */
    private void handleDrawerToggle() {
        if (drawerLayout != null) {
            RecyclerView drawerList = findViewById(R.id.drawer_list);
            if (drawerList != null) {
                if (drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.closeDrawer(drawerList);
                } else {
                    drawerLayout.openDrawer(drawerList);
                }
            }
        }
    }

    /**
     * التعامل مع زر الرجوع مع تحسينات خاصة
     * يتضمن إصلاحاً لمشكلة memory leak في Android O
     */
    @Override
    public void onBackPressed() {
        // إذا كان الدرج مفتوحاً، أغلقه أولاً
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        if (drawerLayout != null && drawerList != null && drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
            return;
        }
        
        // إصلاح مشكلة memory leak في Android O
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTaskRoot()) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * طريقة مساعدة للتبديل بين الفراجمنتس المختلفة
     * تستقبل موقع الفراجمنت وتعرضه مخفية الآخرين
     */
    private boolean switchToFragment(int position) {
        if (position >= 0 && position < fragments.size()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            
            // إخفاء جميع الفراجمنتس
            for (Fragment fragment : fragments) {
                transaction.hide(fragment);
            }
            
            // عرض الفراجمنت المطلوب
            transaction.show(fragments.get(position)).commit();
            
            // تحديث عنوان شريط الأدوات
            updateToolbarTitle(position);
            
            return true;
        }
        return false;
    }

    /**
     * تحديث عنوان شريط الأدوات حسب الفراجمنت النشط
     * يوفر سياقاً واضحاً للمستخدم حول المحتوى الحالي
     */
    private void updateToolbarTitle(int position) {
        if (getSupportActionBar() != null) {
            String title = "تطبيق OneUI";
            switch (position) {
                case 0:
                    title = "الشاشة الرئيسية";
                    break;
                case 1:
                    title = "القوائم والتمرير";
                    break;
                case 2:
                    title = "الإعدادات";
                    break;
            }
            getSupportActionBar().setTitle(title);
        }
    }
}
