package com.example.oneuiapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerListAdapter drawerAdapter;
    private final List<Fragment> fragments = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // إعداد الـ Toolbar مع OneUI
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // إعداد الـ DrawerLayout الأساسي (سيتم استبداله بـ OneUI DrawerLayout لاحقاً)
        drawerLayout = findViewById(R.id.drawerLayout);
        
        // إعداد الـ ActionBar لـ OneUI
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("تطبيق OneUI");
        }

        // تهيئة المكونات
        initFragments();
        initDrawer();

        // رسالة ترحيب للمستخدم
        Toast.makeText(this, "مرحباً بك في تطبيق OneUI! اسحب لأسفل للوصول السهل.", Toast.LENGTH_LONG).show();
    }

    private void initFragments() {
        // إضافة الفراجمنتس المختلفة للتطبيق
        // ملاحظة: ستحتاج لإنشاء هذه الفراجمنتس منفصلة
        
        // يمكن إضافة فراجمنتس حقيقية هنا لاحقاً
        // fragments.add(new DrawerFragment());
        // fragments.add(new ScrollFragment());
        // fragments.add(new SettingsFragment());

        // للاختبار المبدئي، سنترك هذا فارغاً
        // ويمكن إضافة فراجمنت افتراضي بسيط
    }

    private void initDrawer() {
        // إعداد قائمة التنقل الجانبي
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        if (drawerList != null) {
            drawerList.setLayoutManager(new LinearLayoutManager(this));
            
            // إعداد بيانات قائمة التنقل
            List<DrawerItem> items = new ArrayList<>();
            items.add(new DrawerItem("الشاشة الرئيسية", android.R.drawable.ic_menu_gallery));
            items.add(new DrawerItem("قائمة التمرير", android.R.drawable.ic_menu_sort_by_size)); 
            items.add(new DrawerItem("الإعدادات", android.R.drawable.ic_menu_preferences));
            
            // ملاحظة: ستحتاج لإنشاء DrawerListAdapter و DrawerItem
            // drawerAdapter = new DrawerListAdapter(items, position -> onDrawerItemSelected(position));
            // drawerList.setAdapter(drawerAdapter);
        }
    }

    private boolean onDrawerItemSelected(int position) {
        // تبديل المحتوى حسب اختيار المستخدم
        // هذا سيحتاج للتطوير عند إضافة الفراجمنتس الفعلية
        
        // إغلاق الدرج بعد الاختيار
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
        
        // تحديث العنوان
        if (getSupportActionBar() != null) {
            switch (position) {
                case 0:
                    getSupportActionBar().setTitle("الشاشة الرئيسية");
                    break;
                case 1:
                    getSupportActionBar().setTitle("قائمة التمرير");
                    break;
                case 2:
                    getSupportActionBar().setTitle("الإعدادات");
                    break;
                default:
                    getSupportActionBar().setTitle("تطبيق OneUI");
                    break;
            }
        }
        
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        // التعامل مع تغيرات اتجاه الشاشة والوضع الداكن
        if (Build.VERSION.SDK_INT <= 28) {
            // لدعم الإصدارات الأقدم من Android
            final Resources res = getResources();
            res.getConfiguration().setTo(newConfig);
        }
        
        // تحديث حالة الدرج إذا كان موجوداً
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // التعامل مع نقرات شريط الأدوات
        if (item.getItemId() == android.R.id.home) {
            // تبديل حالة الدرج الجانبي
            if (drawerLayout != null) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.drawer_list))) {
                    drawerLayout.closeDrawer(findViewById(R.id.drawer_list));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.drawer_list));
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // التعامل مع زر الرجوع
        if (drawerLayout != null && drawerLayout.isDrawerOpen(findViewById(R.id.drawer_list))) {
            // إذا كان الدرج مفتوحاً، أغلقه
            drawerLayout.closeDrawer(findViewById(R.id.drawer_list));
        } else {
            // إصلاح memory leak في Android O
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTaskRoot()) {
                finishAfterTransition();
            } else {
                super.onBackPressed();
            }
        }
    }

    // كلاس مساعد لعناصر الدرج - ستحتاج لإنشاء هذا في ملف منفصل
    public static class DrawerItem {
        private String title;
        private int iconResId;
        
        public DrawerItem(String title, int iconResId) {
            this.title = title;
            this.iconResId = iconResId;
        }
        
        public String getTitle() { return title; }
        public int getIconResId() { return iconResId; }
    }
}
