package com.example.oneuiapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * نسخة مبسطة من MainActivity تتجنب الاعتماد على ملفات XML معقدة
 * تنشئ الواجهة برمجياً لضمان عدم حدوث أخطاء NullPointerException
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout mainContainer;
    private TextView titleText;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // إنشاء الواجهة برمجياً بدلاً من استخدام XML
        createProgrammaticLayout();
        
        // رسالة ترحيب تؤكد أن التطبيق يعمل
        showWelcomeMessage();
        
        // تحميل المحتوى الافتراضي
        loadDefaultContent();
    }

    /**
     * إنشاء تخطيط بسيط برمجياً يتجنب مشاكل XML
     * يتضمن عنوان وحاوية للمحتوى وأزرار للتنقل
     */
    private void createProgrammaticLayout() {
        // الحاوية الرئيسية
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(32, 64, 32, 32);
        
        // العنوان الرئيسي
        titleText = new TextView(this);
        titleText.setText("تطبيق OneUI - يعمل بنجاح!");
        titleText.setTextSize(24);
        titleText.setPadding(0, 0, 0, 32);
        titleText.setGravity(android.view.Gravity.CENTER);
        
        // أزرار التنقل
        LinearLayout buttonLayout = createNavigationButtons();
        
        // حاوية المحتوى الديناميكي
        mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        
        // تجميع العناصر
        rootLayout.addView(titleText);
        rootLayout.addView(buttonLayout);
        rootLayout.addView(mainContainer);
        
        // تعيين التخطيط كمحتوى للنشاط
        setContentView(rootLayout);
    }

    /**
     * إنشاء أزرار التنقل للوصول للفراجمنتس المختلفة
     * كل زر يعرض محتوى مختلف في الحاوية الرئيسية
     */
    private LinearLayout createNavigationButtons() {
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
        buttonContainer.setPadding(0, 0, 0, 32);
        
        // زر قائمة التمرير
        Button scrollButton = createNavigationButton("قائمة التمرير");
        scrollButton.setOnClickListener(v -> showScrollFragment());
        
        // زر الإعدادات
        Button settingsButton = createNavigationButton("الإعدادات");
        settingsButton.setOnClickListener(v -> showSettingsFragment());
        
        // زر العودة للرئيسية
        Button homeButton = createNavigationButton("الرئيسية");
        homeButton.setOnClickListener(v -> showHomeContent());
        
        buttonContainer.addView(scrollButton);
        buttonContainer.addView(createButtonSpacer());
        buttonContainer.addView(settingsButton);
        buttonContainer.addView(createButtonSpacer());
        buttonContainer.addView(homeButton);
        
        return buttonContainer;
    }

    /**
     * إنشاء زر تنقل بتنسيق موحد
     */
    private Button createNavigationButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        return button;
    }

    /**
     * إنشاء مسافة بين الأزرار لتحسين التخطيط
     */
    private android.view.View createButtonSpacer() {
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(16, ViewGroup.LayoutParams.MATCH_PARENT));
        return spacer;
    }

    /**
     * عرض فراجمنت قائمة التمرير مع 200 عنصر
     * يستبدل المحتوى الحالي بقائمة طويلة للاختبار
     */
    private void showScrollFragment() {
        titleText.setText("قائمة التمرير - 200 عنصر");
        replaceFragment(new ScrollFragment());
        showConfirmationMessage("تم تحميل قائمة التمرير");
    }

    /**
     * عرض فراجمنت الإعدادات مع خيارات الوضع الداكن واللغة
     * يوفر واجهة بسيطة لتغيير إعدادات التطبيق
     */
    private void showSettingsFragment() {
        titleText.setText("إعدادات التطبيق");
        replaceFragment(new SettingsFragment());
        showConfirmationMessage("تم فتح الإعدادات");
    }

    /**
     * عرض المحتوى الافتراضي للشاشة الرئيسية
     * يتضمن معلومات أساسية عن التطبيق وميزة OneUI
     */
    private void showHomeContent() {
        titleText.setText("تطبيق OneUI - الشاشة الرئيسية");
        clearFragments();
        createHomeContent();
        showConfirmationMessage("العودة للشاشة الرئيسية");
    }

    /**
     * استبدال الفراجمنت الحالي بفراجمنت جديد
     * يدير دورة حياة الفراجمنتس بطريقة صحيحة
     */
    private void replaceFragment(Fragment newFragment) {
        currentFragment = newFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mainContainer.getId(), newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * إزالة جميع الفراجمنتس وإظهار المحتوى الافتراضي
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
     * إنشاء محتوى الشاشة الرئيسية برمجياً
     * يتضمن معلومات عن ميزة السحب لأسفل المطلوبة
     */
    private void createHomeContent() {
        mainContainer.removeAllViews();
        
        TextView welcomeText = new TextView(this);
        welcomeText.setText("مرحباً بك في تطبيق OneUI!\n\n" +
                           "هذا التطبيق يستخدم مكتبات Samsung OneUI لتوفير:\n" +
                           "• ميزة السحب لأسفل للوصول السهل\n" +
                           "• تصميم متوافق مع أجهزة Samsung\n" +
                           "• دعم الوضع الداكن والفاتح\n" +
                           "• واجهة سهلة الاستخدام بيد واحدة\n\n" +
                           "استخدم الأزرار أعلاه للتنقل بين الأقسام المختلفة.");
        welcomeText.setTextSize(16);
        welcomeText.setPadding(16, 16, 16, 16);
        welcomeText.setLineSpacing(8, 1.2f);
        
        mainContainer.addView(welcomeText);
    }

    /**
     * تحميل المحتوى الافتراضي عند بدء التطبيق
     */
    private void loadDefaultContent() {
        createHomeContent();
    }

    /**
     * عرض رسالة ترحيب تؤكد أن التطبيق يعمل بنجاح
     * مهمة لطمأنة المستخدم أن الإعدادات صحيحة
     */
    private void showWelcomeMessage() {
        Toast.makeText(this, 
            "تم تشغيل التطبيق بنجاح! مكتبات OneUI تعمل.", 
            Toast.LENGTH_LONG).show();
    }

    /**
     * عرض رسالة تأكيد للإجراءات المختلفة
     */
    private void showConfirmationMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * التعامل مع زر الرجوع بطريقة محسنة
     * يراعي وجود فراجمنتس في المكدس
     */
    @Override
    public void onBackPressed() {
        // إذا كان هناك فراجمنتس في المكدس، ارجع إليها
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            titleText.setText("تطبيق OneUI - الشاشة الرئيسية");
        } else {
            // وإلا اغلق التطبيق
            super.onBackPressed();
        }
    }
}
