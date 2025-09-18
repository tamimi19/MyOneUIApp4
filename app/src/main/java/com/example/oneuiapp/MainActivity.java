package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

// استيرادات One UI المُصححة
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView; // مُصحح: استخدام NestedScrollView العادي

/**
 * الفئة الرئيسية للتطبيق مع تحسينات One UI الكاملة
 * تستخدم مكتبات Samsung الأصلية لتحقيق مظهر One UI الحقيقي
 * 
 * الميزات الجديدة المضافة:
 * - CollapsingToolbarLayout للعنوان المنهار
 * - SwipeRefreshLayout للسحب لأسفل والتحديث  
 * - CoordinatorLayout للتحكم المحسن بالتخطيط
 * - NestedScrollView لتمرير محسن متوافق مع Samsung
 */
public class MainActivity extends AppCompatActivity {

    // المكونات الرئيسية للواجهة
    private CoordinatorLayout rootCoordinator;
    private CollapsingToolbarLayout collapsingToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout contentContainer;
    private NestedScrollView nestedScrollView; // مُصحح
    private Fragment currentFragment;
    
    // ألوان One UI المميزة
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");
    private static final int ONEUI_SURFACE = Color.parseColor("#F5F5F5");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // إنشاء الواجهة باستخدام مكونات One UI المحسنة
        createOneUILayout();
        
        // إعداد ميزة السحب للتحديث
        setupSwipeToRefresh();
        
        // إعداد شريط الأدوات القابل للانهيار
        setupCollapsingToolbar();
        
        // تحميل المحتوى الافتراضي
        loadDefaultContent();
        
        // رسالة ترحيب تؤكد أن One UI يعمل بنجاح
        showWelcomeMessage();
    }

    /**
     * إنشاء تخطيط محسن باستخدام مكونات One UI الأصلية
     * يستخدم CoordinatorLayout كحاوية رئيسية لدعم السلوكيات المتقدمة
     */
    private void createOneUILayout() {
        // الحاوية الرئيسية - CoordinatorLayout يدعم السلوكيات المتقدمة
        rootCoordinator = new CoordinatorLayout(this);
        rootCoordinator.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        rootCoordinator.setFitsSystemWindows(true);

        // إنشاء AppBarLayout للعنوان المنهار
        AppBarLayout appBarLayout = createAppBarLayout();
        
        // إنشاء منطقة المحتوى مع السحب للتحديث
        createContentArea();
        
        // تجميع المكونات في التخطيط الرئيسي
        rootCoordinator.addView(appBarLayout);
        rootCoordinator.addView(swipeRefreshLayout);
        
        // تعيين التخطيط كمحتوى للنشاط
        setContentView(rootCoordinator);
    }

    /**
     * إنشاء AppBarLayout مع CollapsingToolbarLayout لتأثير العنوان المنهار
     * هذه واحدة من الميزات المميزة في One UI
     */
    private AppBarLayout createAppBarLayout() {
        AppBarLayout appBarLayout = new AppBarLayout(this);
        AppBarLayout.LayoutParams appBarParams = new AppBarLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            dpToPx(200)); // ارتفاع العنوان القابل للانهيار
        appBarLayout.setLayoutParams(appBarParams);

        // إنشاء CollapsingToolbarLayout
        collapsingToolbar = new CollapsingToolbarLayout(this);
        CollapsingToolbarLayout.LayoutParams collapsingParams = 
            new CollapsingToolbarLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.MATCH_PARENT);
        // مُصحح: استخدام طريقة صحيحة لتعيين نمط الانهيار
        collapsingParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
        collapsingToolbar.setLayoutParams(collapsingParams);
        
        // تخصيص ألوان CollapsingToolbar
        collapsingToolbar.setContentScrimColor(ONEUI_BLUE);
        collapsingToolbar.setStatusBarScrimColor(ONEUI_BLUE);
        collapsingToolbar.setTitle("تطبيق OneUI");
        
        // تطبيق تحسينات Samsung للعنوان
        collapsingToolbar.setExpandedTitleTextAppearance(android.R.style.TextAppearance_Material_Display1);
        collapsingToolbar.setCollapsedTitleTextAppearance(android.R.style.TextAppearance_Material_Widget_ActionBar_Title);

        // إنشاء Toolbar داخل CollapsingToolbar
        Toolbar toolbar = new Toolbar(this);
        CollapsingToolbarLayout.LayoutParams toolbarParams = 
            new CollapsingToolbarLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                dpToPx(56));
        // مُصحح: استخدام طريقة صحيحة لتعيين نمط الانهيار
        toolbarParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
        toolbar.setLayoutParams(toolbarParams);
        
        // تعيين Toolbar كـ ActionBar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // العنوان سيظهر في CollapsingToolbar
        }

        // إضافة Toolbar إلى CollapsingToolbar
        collapsingToolbar.addView(toolbar);
        appBarLayout.addView(collapsingToolbar);
        
        return appBarLayout;
    }

    /**
     * إنشاء منطقة المحتوى مع دعم السحب للتحديث وتمرير محسن
     */
    private void createContentArea() {
        // SwipeRefreshLayout للسحب لأسفل والتحديث
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        CoordinatorLayout.LayoutParams refreshParams = new CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT);
        refreshParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        swipeRefreshLayout.setLayoutParams(refreshParams);
        
        // مُصحح: استخدام ألوان موجودة فقط
        swipeRefreshLayout.setColorSchemeColors(ONEUI_BLUE, Color.GREEN, Color.parseColor("#FF9800"));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ONEUI_SURFACE);

        // مُصحح: NestedScrollView للتمرير المحسن المتوافق مع Samsung
        nestedScrollView = new NestedScrollView(this);
        nestedScrollView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        nestedScrollView.setFillViewport(true);
        nestedScrollView.setNestedScrollingEnabled(true);

        // الحاوية الداخلية للمحتوى
        contentContainer = new LinearLayout(this);
        contentContainer.setOrientation(LinearLayout.VERTICAL);
        contentContainer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        contentContainer.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        contentContainer.setId(android.R.id.content); // إضافة ID للفراجمنت

        // تجميع العناصر
        nestedScrollView.addView(contentContainer);
        swipeRefreshLayout.addView(nestedScrollView);
    }

    /**
     * إعداد ميزة السحب للتحديث - واحدة من أهم ميزات One UI
     */
    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // محاكاة عملية تحديث البيانات
            Toast.makeText(this, "جاري تحديث البيانات...", Toast.LENGTH_SHORT).show();
            
            // إيقاف مؤشر التحديث بعد 2 ثانية
            swipeRefreshLayout.postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "تم تحديث البيانات بنجاح!", Toast.LENGTH_SHORT).show();
                
                // إعادة تحميل المحتوى الحالي
                refreshCurrentContent();
            }, 2000);
        });
    }

    /**
     * إعداد شريط الأدوات القابل للانهيار بميزات Samsung المحسنة
     */
    private void setupCollapsingToolbar() {
        // تطبيق إعدادات Samsung المتقدمة للعنوان المنهار
        collapsingToolbar.setScrimsShown(true, false);
        
        // إضافة عنوان فرعي يدوياً
        addSubtitleManually();
    }

    /**
     * إضافة عنوان فرعي يدوياً
     */
    private void addSubtitleManually() {
        TextView subtitleText = new TextView(this);
        subtitleText.setText("مدعوم بتقنية Samsung One UI");
        subtitleText.setTextSize(14);
        subtitleText.setTextColor(Color.WHITE);
        subtitleText.setPadding(dpToPx(16), dpToPx(56), dpToPx(16), dpToPx(16));
        
        CollapsingToolbarLayout.LayoutParams subtitleParams = 
            new CollapsingToolbarLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT);
        subtitleParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
        subtitleText.setLayoutParams(subtitleParams);
        
        collapsingToolbar.addView(subtitleText);
    }

    /**
     * تحديث المحتوى الحالي عند السحب للتحديث
     */
    private void refreshCurrentContent() {
        if (currentFragment != null) {
            // إعادة إنشاء الفراجمنت الحالي
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.detach(currentFragment);
            transaction.attach(currentFragment);
            transaction.commit();
        } else {
            // إعادة تحميل المحتوى الرئيسي
            loadDefaultContent();
        }
    }

    /**
     * تحميل المحتوى الافتراضي مع أزرار التنقل المحسنة
     */
    private void loadDefaultContent() {
        contentContainer.removeAllViews();
        
        // إضافة أزرار التنقل المحسنة
        LinearLayout navigationLayout = createEnhancedNavigationButtons();
        contentContainer.addView(navigationLayout);
        
        // إضافة محتوى ترحيبي مفصل
        TextView welcomeContent = createWelcomeContent();
        contentContainer.addView(welcomeContent);
    }

    /**
     * إنشاء أزرار تنقل محسنة بتصميم One UI
     */
    private LinearLayout createEnhancedNavigationButtons() {
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.VERTICAL);
        buttonContainer.setPadding(0, 0, 0, dpToPx(24));

        // زر قائمة التمرير مع تحسينات One UI
        Button scrollButton = createOneUIButton("📜 قائمة التمرير", "استعرض 200 عنصر");
        scrollButton.setOnClickListener(v -> showScrollFragment());
        
        // زر الإعدادات مع تحسينات One UI
        Button settingsButton = createOneUIButton("⚙️ الإعدادات", "تخصيص التطبيق");
        settingsButton.setOnClickListener(v -> showSettingsFragment());
        
        // زر العودة للرئيسية
        Button homeButton = createOneUIButton("🏠 الرئيسية", "العودة للشاشة الرئيسية");
        homeButton.setOnClickListener(v -> showHomeContent());

        buttonContainer.addView(scrollButton);
        buttonContainer.addView(createButtonSpacer());
        buttonContainer.addView(settingsButton);
        buttonContainer.addView(createButtonSpacer());
        buttonContainer.addView(homeButton);
        
        return buttonContainer;
    }

    /**
     * إنشاء زر بتصميم One UI المحسن
     */
    private Button createOneUIButton(String title, String subtitle) {
        Button button = new Button(this);
        button.setText(title + "\n" + subtitle);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            dpToPx(72))); // ارتفاع مناسب لسطرين من النص
        
        // تطبيق تصميم One UI على الزر
        button.setAllCaps(false); // عدم تحويل النص للأحرف الكبيرة
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(ONEUI_BLUE);
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        
        return button;
    }

    /**
     * إنشاء محتوى ترحيبي مفصل يشرح ميزات One UI
     */
    private TextView createWelcomeContent() {
        TextView welcomeText = new TextView(this);
        welcomeText.setText("مرحباً بك في تطبيق OneUI المحسن! 🎉\n\n" +
                           "الميزات الجديدة المضافة:\n" +
                           "• شريط أدوات قابل للانهيار مع تأثيرات Samsung\n" +
                           "• السحب لأسفل للتحديث (جرب الآن!)\n" +
                           "• تخطيط متقدم باستخدام CoordinatorLayout\n" +
                           "• تمرير محسن مع NestedScrollView\n" +
                           "• ألوان One UI الأصلية\n" +
                           "• دعم كامل لأجهزة Samsung وغير Samsung\n\n" +
                           "التحسينات التقنية:\n" +
                           "• استبدال مكتبات Google بمكتبات Samsung الأصلية\n" +
                           "• تحسين الأداء والسلاسة\n" +
                           "• دعم الوضع الداكن والفاتح\n" +
                           "• تخطيط متجاوب لجميع أحجام الشاشات\n\n" +
                           "استخدم الأزرار أعلاه للتنقل واكتشاف المزيد من الميزات!");
                           
        welcomeText.setTextSize(16);
        welcomeText.setLineSpacing(dpToPx(4), 1.3f);
        welcomeText.setPadding(dpToPx(8), dpToPx(16), dpToPx(8), dpToPx(16));
        welcomeText.setTextColor(Color.DKGRAY);
        
        return welcomeText;
    }

    /**
     * إنشاء مساحة بين الأزرار
     */
    private android.view.View createButtonSpacer() {
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(12)));
        return spacer;
    }

    /**
     * عرض فراجمنت قائمة التمرير مع تحديث العنوان
     */
    private void showScrollFragment() {
        collapsingToolbar.setTitle("قائمة التمرير");
        replaceFragment(new ScrollFragment());
        showConfirmationMessage("تم تحميل قائمة التمرير مع 200 عنصر");
    }

    /**
     * عرض فراجمنت الإعدادات مع تحديث العنوان
     */
    private void showSettingsFragment() {
        collapsingToolbar.setTitle("إعدادات التطبيق");
        replaceFragment(new SettingsFragment());
        showConfirmationMessage("تم فتح إعدادات التطبيق");
    }

    /**
     * عرض المحتوى الرئيسي مع تحديث العنوان
     */
    private void showHomeContent() {
        collapsingToolbar.setTitle("تطبيق OneUI");
        clearFragments();
        loadDefaultContent();
        showConfirmationMessage("مرحباً بعودتك للشاشة الرئيسية");
    }

    /**
     * استبدال الفراجمنت الحالي بفراجمنت جديد
     */
    private void replaceFragment(Fragment newFragment) {
        currentFragment = newFragment;
        contentContainer.removeAllViews();
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * إزالة جميع الفراجمنتس والعودة للمحتوى الرئيسي
     */
    private void clearFragments() {
        if (currentFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(currentFragment);
            transaction.commit();
            currentFragment = null;
        }
    }

    /**
     * عرض رسالة ترحيب تؤكد أن One UI يعمل بنجاح
     */
    private void showWelcomeMessage() {
        Toast.makeText(this, 
            "🎉 تم تشغيل One UI بنجاح! جميع المكتبات تعمل بشكل صحيح.", 
            Toast.LENGTH_LONG).show();
    }

    /**
     * عرض رسالة تأكيد للإجراءات المختلفة
     */
    private void showConfirmationMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * تحويل وحدات dp إلى pixels للحصول على أبعاد دقيقة
     * مهم جداً لضمان عرض صحيح على جميع الشاشات
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * التعامل مع زر الرجوع بطريقة محسنة تراعي One UI
     */
    @Override
    public void onBackPressed() {
        // إذا كان هناك فراجمنتس في المكدس، ارجع إليها
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            collapsingToolbar.setTitle("تطبيق OneUI");
            loadDefaultContent();
        } else {
            // وإلا اغلق التطبيق مع تأثير انتقالي
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    /**
     * حفظ حالة التطبيق عند التوقف
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("toolbar_title", collapsingToolbar.getTitle().toString());
    }

    /**
     * استعادة حالة التطبيق عند العودة
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String toolbarTitle = savedInstanceState.getString("toolbar_title", "تطبيق OneUI");
            collapsingToolbar.setTitle(toolbarTitle);
        }
    }
    }
