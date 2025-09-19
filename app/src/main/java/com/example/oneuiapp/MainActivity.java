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
 * MainActivity مبسطة لتجنب مشاكل الثيمات والموارد
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout contentContainer;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    
    private Fragment currentFragment;
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // تهيئة معالج الأخطاء
        CrashHandler.initialize(this);
        CrashHandler.cleanOldLogs(this);
        
        try {
            setContentView(R.layout.activity_main);
            initializeViews();
            setupUserInterface();
            loadMainContent();
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تهيئة التطبيق", e);
            createFallbackInterface();
        }
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        contentContainer = findViewById(R.id.main_container);
        nestedScrollView = findViewById(R.id.nested_scroll);
        appBarLayout = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupUserInterface() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle("تطبيق OneUI");
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        }
        
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(ONEUI_BLUE);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.postDelayed(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(this, "تم التحديث", Toast.LENGTH_SHORT).show();
                }, 1500);
            });
        }
    }

    private void loadMainContent() {
        if (contentContainer == null) return;
        
        contentContainer.removeAllViews();
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
        
        // العنوان الرئيسي
        TextView mainTitle = new TextView(this);
        mainTitle.setText("تطبيق OneUI");
        mainTitle.setTextSize(28);
        mainTitle.setTextColor(ONEUI_BLUE);
        mainTitle.setPadding(0, 0, 0, dpToPx(16));
        mainLayout.addView(mainTitle);
        
        // الوصف
        TextView description = new TextView(this);
        description.setText("تطبيق محسن بتقنية Samsung OneUI مع ميزات متقدمة");
        description.setTextSize(16);
        description.setTextColor(Color.DKGRAY);
        description.setPadding(0, 0, 0, dpToPx(24));
        mainLayout.addView(description);
        
        // أزرار التنقل
        Button scrollButton = createActionButton("قائمة التمرير");
        scrollButton.setOnClickListener(v -> navigateToScrollList());
        mainLayout.addView(scrollButton);
        
        addVerticalSpace(mainLayout, 12);
        
        Button settingsButton = createActionButton("الإعدادات");
        settingsButton.setOnClickListener(v -> navigateToSettings());
        mainLayout.addView(settingsButton);
        
        addVerticalSpace(mainLayout, 12);
        
        Button homeButton = createActionButton("الرئيسية");
        homeButton.setOnClickListener(v -> returnToHome());
        mainLayout.addView(homeButton);
        
        contentContainer.addView(mainLayout);
    }
    
    private Button createActionButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(50)));
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(ONEUI_BLUE);
        button.setAllCaps(false);
        return button;
    }
    
    private void addVerticalSpace(LinearLayout parent, int dp) {
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(dp)));
        parent.addView(spacer);
    }
    
    private void navigateToScrollList() {
        updateTitle("قائمة التمرير");
        replaceWithFragment(new ScrollFragment());
        showToast("تم فتح قائمة التمرير");
    }
    
    private void navigateToSettings() {
        updateTitle("الإعدادات");
        replaceWithFragment(new SettingsFragment());
        showToast("تم فتح الإعدادات");
    }
    
    private void returnToHome() {
        updateTitle("تطبيق OneUI");
        clearCurrentFragment();
        loadMainContent();
        showToast("العودة للرئيسية");
    }
    
    private void updateTitle(String title) {
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(title);
        }
    }
    
    private void replaceWithFragment(Fragment fragment) {
        if (contentContainer == null) return;
        
        try {
            currentFragment = fragment;
            contentContainer.removeAllViews();
            
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في تحميل Fragment", e);
            showToast("خطأ في تحميل المحتوى");
            returnToHome();
        }
    }
    
    private void clearCurrentFragment() {
        if (currentFragment != null) {
            try {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(currentFragment);
                transaction.commit();
                currentFragment = null;
            } catch (Exception e) {
                Log.e("MainActivity", "خطأ في إزالة Fragment", e);
            }
        }
    }
    
    private void createFallbackInterface() {
        LinearLayout fallbackLayout = new LinearLayout(this);
        fallbackLayout.setOrientation(LinearLayout.VERTICAL);
        fallbackLayout.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        fallbackLayout.setPadding(dpToPx(40), dpToPx(40), dpToPx(40), dpToPx(40));
        
        TextView errorText = new TextView(this);
        errorText.setText("خطأ في تحميل التطبيق\nتحقق من السجلات للتفاصيل");
        errorText.setTextSize(18);
        errorText.setTextColor(Color.RED);
        errorText.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        fallbackLayout.addView(errorText);
        setContentView(fallbackLayout);
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            returnToHome();
        } else {
            super.onBackPressed();
        }
    }
        }
