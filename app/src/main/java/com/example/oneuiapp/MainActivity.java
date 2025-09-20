package com.example.oneuiapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.Toast;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity مبسطة للغاية لتشخيص مشاكل الإغلاق الإجباري
 * تحتوي فقط على الحد الأدنى المطلوب للعمل
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "بدء تهيئة التطبيق");
        
        try {
            // محاولة تهيئة معالج الأخطاء
            initializeCrashHandler();
            
            // إنشاء واجهة مبسطة برمجياً
            createSimpleInterface();
            
            Log.d(TAG, "تم تهيئة التطبيق بنجاح");
            
        } catch (Exception e) {
            Log.e(TAG, "خطأ فادح في التهيئة", e);
            createEmergencyInterface();
        }
    }

    /**
     * تهيئة معالج الأخطاء مع معالجة الأخطاء
     */
    private void initializeCrashHandler() {
        try {
            CrashHandler.initialize(this);
            CrashHandler.cleanOldLogs(this);
            Log.d(TAG, "تم تهيئة معالج الأخطاء بنجاح");
        } catch (Exception e) {
            Log.w(TAG, "فشل في تهيئة معالج الأخطاء، متابعة بدونه", e);
        }
    }

    /**
     * إنشاء واجهة مبسطة برمجياً
     */
    private void createSimpleInterface() {
        // إنشاء التخطيط الأساسي
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 100, 40, 100);
        mainLayout.setBackgroundColor(Color.WHITE);
        
        // العنوان الرئيسي
        TextView titleView = createTextView("تطبيق OneUI", 24, Color.parseColor("#1976D2"));
        titleView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        titleView.setPadding(0, 0, 0, 40);
        mainLayout.addView(titleView);
        
        // رسالة النجاح
        TextView successView = createTextView("تم تشغيل التطبيق بنجاح!", 18, Color.parseColor("#4CAF50"));
        successView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        successView.setPadding(0, 0, 0, 40);
        mainLayout.addView(successView);
        
        // زر اختبار
        Button testButton = createButton("اختبار العمل");
        testButton.setOnClickListener(v -> {
            Toast.makeText(this, "التطبيق يعمل بشكل طبيعي!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "تم النقر على زر الاختبار");
        });
        mainLayout.addView(testButton);
        
        // زر تجربة XML Layout
        Button xmlTestButton = createButton("تجربة XML Layout");
        xmlTestButton.setOnClickListener(v -> testXmlLayout());
        mainLayout.addView(xmlTestButton);
        
        // تعيين التخطيط كمحتوى رئيسي
        setContentView(mainLayout);
        
        Log.d(TAG, "تم إنشاء الواجهة المبسطة بنجاح");
    }

    /**
     * اختبار تحميل XML Layout
     */
    private void testXmlLayout() {
        try {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "تم تحميل XML Layout بنجاح!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "تم تحميل XML Layout بنجاح");
        } catch (Exception e) {
            Log.e(TAG, "فشل في تحميل XML Layout", e);
            Toast.makeText(this, "فشل في تحميل XML Layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
            
            // العودة للواجهة المبسطة
            createSimpleInterface();
        }
    }

    /**
     * إنشاء TextView مخصص
     */
    private TextView createTextView(String text, int textSize, int textColor) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        return textView;
    }

    /**
     * إنشاء Button مخصص
     */
    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.parseColor("#1976D2"));
        button.setAllCaps(false);
        button.setPadding(32, 24, 32, 24);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        button.setLayoutParams(params);
        
        return button;
    }

    /**
     * إنشاء واجهة طوارئ في حالة الفشل الكامل
     */
    private void createEmergencyInterface() {
        try {
            LinearLayout emergencyLayout = new LinearLayout(this);
            emergencyLayout.setOrientation(LinearLayout.VERTICAL);
            emergencyLayout.setBackgroundColor(Color.WHITE);
            emergencyLayout.setPadding(40, 100, 40, 100);
            
            TextView errorText = new TextView(this);
            errorText.setText("حدث خطأ في التطبيق\n\nيرجى التحقق من السجلات");
            errorText.setTextSize(18);
            errorText.setTextColor(Color.RED);
            errorText.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            
            emergencyLayout.addView(errorText);
            setContentView(emergencyLayout);
            
            Log.d(TAG, "تم إنشاء واجهة الطوارئ");
            
        } catch (Exception e) {
            Log.e(TAG, "فشل حتى في إنشاء واجهة الطوارئ!", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart تم استدعاؤها");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume تم استدعاؤها");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause تم استدعاؤها");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop تم استدعاؤها");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy تم استدعاؤها");
    }
}
