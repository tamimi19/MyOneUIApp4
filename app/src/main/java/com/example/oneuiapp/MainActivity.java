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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * MainActivity محسنة لـ OneUI مع ميزة Pull-to-Reach
 * تستخدم المكتبات الصحيحة من OneUI Project مع دعم SESL CollapsingToolbarLayout
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
        
        // تهيئة معالج الأخطاء قبل أي شيء آخر
        CrashHandler.initialize(this);
        
        // تنظيف السجلات القديمة
        CrashHandler.cleanOldLogs(this);
        
        setContentView(R.layout.activity_main);
        
        try {
            initViews();
            setupToolbar();
            setupPullToReach();
            setupSwipeToRefresh();
            loadDefaultContent();
        } catch (Exception e) {
            Log.e("MainActivity", "خطأ في onCreate", e);
            // في حالة حدوث خطأ، اعرض رسالة وأغلق بأمان
            Toast.makeText(this, "خطأ في تهيئة التطبيق، تحقق من السجلات", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        contentContainer = findViewById(R.id.main_container);
        nestedScrollView = findViewById(R.id.nested_scroll);
        appBarLayout = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        // التأكد من وجود CollapsingToolbarLayout قبل استخدام طرق SESL
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle("تطبيق OneUI");
            try {
                collapsingToolbar.seslSetSubtitle("مدعوم بتقنية Samsung");
                collapsingToolbar.seslEnableFadeToolbarTitle(true);
            } catch (NoSuchMethodError e) {
                // إذا لم تكن طرق SESL متاحة، استخدم العادية فقط
                android.util.Log.w("MainActivity", "SESL methods not available");
            }
            
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        }
    }

    private void setupPullToReach() {
        CoordinatorLayout.LayoutParams params = 
            (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return true;
            }
        });
        
        params.setBehavior(behavior);
        appBarLayout.setLayoutParams(params);
        
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            float percentage = Math.abs(verticalOffset) / (float) appBarLayout1.getTotalScrollRange();
            
            if (percentage == 1.0f) {
                toolbar.setAlpha(1.0f);
            } else if (percentage == 0.0f) {
                toolbar.setAlpha(0.7f);
            } else {
                toolbar.setAlpha(0.8f + (percentage * 0.2f));
            }
        });
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setColorSchemeColors(
            ONEUI_BLUE, 
            Color.parseColor("#4CAF50"), 
            Color.parseColor("#FF9800")
        );
        
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(this, "جاري تحديث المحتوى...", Toast.LENGTH_SHORT).show();
            
            swipeRefreshLayout.postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                refreshCurrentContent();
                Toast.makeText(this, "تم التحديث بنجاح!", Toast.LENGTH_SHORT).show();
            }, 2000);
        });
    }

    private void loadDefaultContent() {
        contentContainer.removeAllViews();
        
        LinearLayout mainContent = new LinearLayout(this);
        mainContent.setOrientation(LinearLayout.VERTICAL);
        mainContent.setLayoutParams(new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        mainContent.addView(createNavigationButtons());
        mainContent.addView(createWelcomeText());
        
        contentContainer.addView(mainContent);
    }

    private LinearLayout createNavigationButtons() {
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.VERTICAL);
        buttonContainer.setPadding(0, 0, 0, dpToPx(24));
        
        Button scrollButton = createStyledButton("قائمة التمرير", "200 عنصر مع تمرير محسن");
        scrollButton.setOnClickListener(v -> showScrollFragment());
        
        Button settingsButton = createStyledButton("الإعدادات", "تخصيص واجهة التطبيق");
        settingsButton.setOnClickListener(v -> showSettingsFragment());
        
        Button homeButton = createStyledButton("الصفحة الرئيسية", "العودة للشاشة الرئيسية");
        homeButton.setOnClickListener(v -> showHomeContent());
        
        buttonContainer.addView(scrollButton);
        buttonContainer.addView(createSpacer(12));
        buttonContainer.addView(settingsButton);
        buttonContainer.addView(createSpacer(12));
        buttonContainer.addView(homeButton);
        
        return buttonContainer;
    }

    private Button createStyledButton(String title, String subtitle) {
        Button button = new Button(this);
        button.setText(title + "\n" + subtitle);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(70)));
        
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(ONEUI_BLUE);
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        
        return button;
    }

    private TextView createWelcomeText() {
        TextView welcomeText = new TextView(this);
        welcomeText.setText(
            "مرحباً بك في تطبيق OneUI المحسن مع SESL!\n\n" +
            
            "ميزة Pull-to-Reach الجديدة:\n" +
            "اسحب الشاشة لأسفل للوصول السهل للعناصر العلوية\n" +
            "مثالية للاستخدام بيد واحدة\n" +
            "تعمل تلقائياً مع الشريط العلوي القابل للطي\n\n" +
            
            "الميزات المحسنة مع SESL:\n" +
            "CollapsingToolbarLayout مع دعم العناوين الفرعية\n" +
            "تأثير الاختفاء المحسن للعنوان (Fade Effect)\n" +
            "دعم العناوين المخصصة والموسعة\n" +
            "تصميم One UI الأصلي من Samsung\n\n" +
            
            "الميزات الأخرى:\n" +
            "السحب لأسفل للتحديث\n" +
            "تخطيط متقدم باستخدام CoordinatorLayout\n" +
            "تمرير محسن مع NestedScrollView\n" +
            "ألوان One UI الأصلية\n" +
            "دعم كامل لأجهزة Samsung وغير Samsung\n\n" +
            
            "استخدم الأزرار أعلاه للتنقل واكتشاف المزيد من الميزات!"
        );
                           
        welcomeText.setTextSize(16);
        welcomeText.setLineSpacing(dpToPx(4), 1.3f);
        welcomeText.setPadding(dpToPx(8), dpToPx(16), dpToPx(8), dpToPx(16));
        welcomeText.setTextColor(Color.DKGRAY);
        
        return welcomeText;
    }

    private android.view.View createSpacer(int dp) {
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(dp)));
        return spacer;
    }

    private void showScrollFragment() {
        collapsingToolbar.setTitle("قائمة التمرير");
        collapsingToolbar.seslSetSubtitle("عرض محسن للبيانات");
        replaceFragment(new ScrollFragment());
        Toast.makeText(this, "تم تحميل قائمة التمرير", Toast.LENGTH_SHORT).show();
    }

    private void showSettingsFragment() {
        collapsingToolbar.setTitle("إعدادات التطبيق");
        collapsingToolbar.seslSetSubtitle("تخصيص التطبيق");
        replaceFragment(new SettingsFragment());
        Toast.makeText(this, "تم فتح إعدادات التطبيق", Toast.LENGTH_SHORT).show();
    }

    private void showHomeContent() {
        collapsingToolbar.setTitle("تطبيق OneUI");
        collapsingToolbar.seslSetSubtitle("مدعوم بتقنية Samsung");
        clearFragments();
        loadDefaultContent();
        Toast.makeText(this, "مرحباً بعودتك للشاشة الرئيسية", Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment newFragment) {
        currentFragment = newFragment;
        contentContainer.removeAllViews();
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void clearFragments() {
        if (currentFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(currentFragment);
            transaction.commit();
            currentFragment = null;
        }
    }

    private void refreshCurrentContent() {
        if (currentFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.detach(currentFragment);
            transaction.attach(currentFragment);
            transaction.commit();
        } else {
            loadDefaultContent();
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            collapsingToolbar.setTitle("تطبيق OneUI");
            collapsingToolbar.seslSetSubtitle("مدعوم بتقنية Samsung");
            loadDefaultContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("toolbar_title", collapsingToolbar.getTitle().toString());
        if (collapsingToolbar.getSubTitle() != null) {
            outState.putString("toolbar_subtitle", collapsingToolbar.getSubTitle().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String toolbarTitle = savedInstanceState.getString("toolbar_title", "تطبيق OneUI");
            String toolbarSubtitle = savedInstanceState.getString("toolbar_subtitle", "مدعوم بتقنية Samsung");
            collapsingToolbar.setTitle(toolbarTitle);
            collapsingToolbar.seslSetSubtitle(toolbarSubtitle);
        }
    }
                               }
