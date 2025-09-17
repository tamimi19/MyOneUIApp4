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

// استبدال TipPopup بـ Toast أو Snackbar كبديل مؤقت
import android.widget.Toast;

// استبدال TipPopup بـ Toast أو Snackbar كبديل مؤقت
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

        // إعداد الـ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // إعداد الـ DrawerLayout مع أيقونة الهامبرغر
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, 
                drawerLayout, 
                toolbar,
                R.string.drawer_open, 
                R.string.drawer_close
        );
        
        // تعيين أيقونة OneUI للـ drawer
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // تعيين أيقونة OneUI مخصصة للـ Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_oui_list);

        // تهيئة قائمة الـ Drawer
        initFragments();
        initDrawer();

        // استبدال TipPopup بـ Toast كبديل مؤقت
        Toast.makeText(this, "مرحباً! قم بسحب القائمة لأعلى ولأسفل.", Toast.LENGTH_LONG).show();
    }

    private void initFragments() {
        // إضافة الفراجمنتس التي سنعرضها
        fragments.add(new DrawerFragment());      // شاشة الدرج الجانبي
        fragments.add(new ScrollFragment());      // شاشة التمرير (200 عنصر)
        fragments.add(new SettingsFragment());    // شاشة الإعدادات

        // عرض الفراجمنت الأول افتراضياً
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_container, fragments.get(0));
        ft.add(R.id.main_container, fragments.get(1));
        ft.add(R.id.main_container, fragments.get(2));
        ft.hide(fragments.get(1));
        ft.hide(fragments.get(2));
        ft.commit();
    }

    private void initDrawer() {
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        drawerList.setLayoutManager(new LinearLayoutManager(this));
        
        // إعداد بيانات قائمة التنقل مع أيقونات OneUI الصحيحة
        List<DrawerItem> items = new ArrayList<>();
        items.add(new DrawerItem("الدرج الجانبي", R.drawable.ic_oui_drawer));
        items.add(new DrawerItem("شاشة التمرير", R.drawable.ic_oui_list)); 
        items.add(new DrawerItem("الإعدادات", R.drawable.ic_oui_settings_outline));
        
        drawerAdapter = new DrawerListAdapter(items, position -> onDrawerItemSelected(position));
        drawerList.setAdapter(drawerAdapter);
    }

    private boolean onDrawerItemSelected(int position) {
        // تبديل الفراجمنت المعروض حسب اختيار المستخدم
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment frag : fragments) {
            transaction.hide(frag);
        }
        transaction.show(fragments.get(position)).commit();
        
        // تحديث العنوان في شريط الأدوات
        if (position == 0) {
            getSupportActionBar().setTitle("الدرج الجانبي");
        } else if (position == 1) {
            getSupportActionBar().setTitle("شاشة التمرير");
        } else if (position == 2) {
            getSupportActionBar().setTitle("الإعدادات");
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // تحديث حالة الـ DrawerToggle مع التكوين الجديد
        drawerToggle.onConfigurationChanged(newConfig);
        
        // تعامل مع تغيرات التكوين في الوضع الداكن (إذا كان SDK ≤ 28)
        if (Build.VERSION.SDK_INT <= 28) {
            final Resources res = getResources();
            // هنا يمكن تطبيق منطق DarkModeUtils كما في مكتبة OneUI إذا لزم الأمر
            res.getConfiguration().setTo(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // التعامل مع نقرة أيقونة الـ Navigation Drawer
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.drawer_list))) {
                drawerLayout.closeDrawer(findViewById(R.id.drawer_list));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.drawer_list));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
            }
