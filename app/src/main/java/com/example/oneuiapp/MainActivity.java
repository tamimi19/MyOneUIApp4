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

// استبدال TipPopup بـ Toast أو Snackbar كبديل مؤقت
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerListAdapter drawerAdapter;
    private final List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة قائمة الـ Drawer
        initFragments();
        initDrawer();

        // استبدال TipPopup بـ Toast كبديل مؤقت
        Toast.makeText(this, "مرحباً! قم بسحب القائمة لأعلى ولأسفل.", Toast.LENGTH_LONG).show();
    }

    private void initFragments() {
        // إضافة الفراجمنتس التي سنعرضها. أولاً شاشة التمرير ثم الإعدادات مثلاً.
        fragments.add(new ScrollFragment());      // شاشة التمرير (200 عنصر)
        fragments.add(new SettingsFragment());    // شاشة الإعدادات
        // يمكن إضافة فراجمنتات إضافية إن لزم الأمر

        // عرض الفراجمنت الأول افتراضياً
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_container, fragments.get(0));
        ft.add(R.id.main_container, fragments.get(1));
        ft.hide(fragments.get(1));
        ft.commit();
    }

    private void initDrawer() {
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        drawerList.setLayoutManager(new LinearLayoutManager(this));
        // إعداد بيانات قائمة التنقل (العنوان والأيقونة لكل عنصر)
        List<DrawerItem> items = new ArrayList<>();
        items.add(new DrawerItem("شاشة التمرير", R.drawable.ic_menu_sort)); 
        items.add(new DrawerItem("الإعدادات", R.drawable.ic_menu_manage));
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
            getSupportActionBar().setTitle("شاشة التمرير");
        } else if (position == 1) {
            getSupportActionBar().setTitle("الإعدادات");
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // تعامل مع تغيرات التكوين في الوضع الداكن (إذا كان SDK ≤ 28)
        if (Build.VERSION.SDK_INT <= 28) {
            final Resources res = getResources();
            // هنا يمكن تطبيق منطق DarkModeUtils كما في مكتبة OneUI إذا لزم الأمر
            res.getConfiguration().setTo(newConfig);
        }
    }
}
