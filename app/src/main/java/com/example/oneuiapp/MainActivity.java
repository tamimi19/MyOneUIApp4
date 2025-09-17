// ملف MainActivity.java
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

    // إزالة المرجع للكلاس المفقود مؤقتاً
    // private DrawerListAdapter drawerAdapter;
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

        // إعداد الـ DrawerLayout
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
        fragments.add(new ScrollFragment());
        fragments.add(new SettingsFragment());

        // عرض الفراجمنت الأول افتراضياً
        if (!fragments.isEmpty()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                ft.add(R.id.main_container, fragments.get(i));
                if (i > 0) {
                    ft.hide(fragments.get(i));
                }
            }
            ft.commit();
        }
    }

    private void initDrawer() {
        // إعداد قائمة التنقل الجانبي
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        if (drawerList != null) {
            drawerList.setLayoutManager(new LinearLayoutManager(this));
            
            // يمكن إضافة الـ adapter لاحقاً عند إنشاء DrawerListAdapter
            // drawerList.setAdapter(drawerAdapter);
        }
    }

    private boolean onDrawerItemSelected(int position) {
        // تبديل المحتوى حسب اختيار المستخدم
        if (position < fragments.size()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            for (Fragment frag : fragments) {
                transaction.hide(frag);
            }
            transaction.show(fragments.get(position)).commit();
        }
        
        // إغلاق الدرج بعد الاختيار
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
        
        // تحديث العنوان
        if (getSupportActionBar() != null) {
            switch (position) {
                case 0:
                    getSupportActionBar().setTitle("قائمة التمرير");
                    break;
                case 1:
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
                RecyclerView drawerList = findViewById(R.id.drawer_list);
                if (drawerList != null) {
                    if (drawerLayout.isDrawerOpen(drawerList)) {
                        drawerLayout.closeDrawer(drawerList);
                    } else {
                        drawerLayout.openDrawer(drawerList);
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // التعامل مع زر الرجوع
        RecyclerView drawerList = findViewById(R.id.drawer_list);
        if (drawerLayout != null && drawerList != null && drawerLayout.isDrawerOpen(drawerList)) {
            // إذا كان الدرج مفتوحاً، أغلقه
            drawerLayout.closeDrawer(drawerList);
        } else {
            // إصلاح memory leak في Android O
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTaskRoot()) {
                finishAfterTransition();
            } else {
                super.onBackPressed();
            }
        }
    }
}

// -------------------------------------------------------------------
// ملف ScrollFragment.java - يجب إنشاؤه في ملف منفصل
package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ScrollFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // إنشاء RecyclerView برمجياً لتجنب مشاكل XML
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        
        // إعداد البيانات
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            items.add("عنصر رقم " + i);
        }
        
        // إعداد الـ RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ScrollAdapter(items));
        
        return recyclerView;
    }

    // الـ Adapter للـ RecyclerView
    private static class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {
        private final List<String> data;
        
        ScrollAdapter(List<String> data) { 
            this.data = data; 
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // إنشاء TextView برمجياً
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(32, 32, 32, 32);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            ((TextView)holder.itemView).setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}

// -------------------------------------------------------------------
// ملف SettingsFragment.java - يجب إنشاؤه في ملف منفصل
package com.example.oneuiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // إنشاء التخطيط برمجياً لتجنب مشاكل XML
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // زر تغيير اللغة
        Button langButton = new Button(requireContext());
        langButton.setText("تغيير اللغة");
        langButton.setOnClickListener(v -> {
            // سيتم تنفيذ تغيير اللغة لاحقاً
            Toast.makeText(getContext(), "ميزة تغيير اللغة قيد التطوير", Toast.LENGTH_SHORT).show();
        });

        // زر الوضع الداكن
        Button darkModeButton = new Button(requireContext());
        darkModeButton.setText("تبديل الوضع الداكن");
        darkModeButton.setOnClickListener(v -> {
            int currentMode = AppCompatDelegate.getDefaultNightMode();
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        // زر الإشعارات
        Button notificationButton = new Button(requireContext());
        notificationButton.setText("إعدادات الإشعارات");
        notificationButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "إعدادات الإشعارات", Toast.LENGTH_SHORT).show();
        });

        // إضافة الأزرار للتخطيط
        layout.addView(langButton);
        layout.addView(darkModeButton);
        layout.addView(notificationButton);

        return layout;
    }
                                             }
