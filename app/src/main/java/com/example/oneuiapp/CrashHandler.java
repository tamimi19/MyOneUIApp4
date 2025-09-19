package com.example.oneuiapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * معالج الأخطاء المخصص لحفظ تفاصيل الإغلاق الإجباري
 * يحفظ السجلات في الذاكرة الداخلية للمراجعة اللاحقة
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    
    private static final String TAG = "CrashHandler";
    private static final String CRASH_LOG_DIR = "crash_logs";
    private static final String LOG_FILE_PREFIX = "crash_";
    private static final String LOG_FILE_EXTENSION = ".txt";
    
    private Context context;
    private Thread.UncaughtExceptionHandler defaultHandler;
    
    public CrashHandler(Context context) {
        this.context = context.getApplicationContext();
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    /**
     * تهيئة معالج الأخطاء
     */
    public static void initialize(Context context) {
        CrashHandler crashHandler = new CrashHandler(context);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        Log.d(TAG, "تم تهيئة معالج الأخطاء بنجاح");
    }
    
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        try {
            // حفظ تفاصيل الخطأ
            saveCrashReport(exception);
            
            // عرض الخطأ في LogCat أيضاً
            Log.e(TAG, "خطأ غير محتوى: " + exception.getMessage(), exception);
            
        } catch (Exception e) {
            Log.e(TAG, "فشل في حفظ تقرير الخطأ", e);
        } finally {
            // استدعاء المعالج الافتراضي لإنهاء التطبيق
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, exception);
            } else {
                System.exit(1);
            }
        }
    }
    
    /**
     * حفظ تقرير مفصل عن الخطأ
     */
    private void saveCrashReport(Throwable exception) {
        try {
            // إنشاء مجلد السجلات في مجلد التنزيلات (يمكن الوصول إليه)
            File crashDir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // للإصدارات الحديثة من أندرويد - استخدام مجلد التنزيلات
                crashDir = new File(android.os.Environment.getExternalStoragePublicDirectory(
                    android.os.Environment.DIRECTORY_DOWNLOADS), "OneUI_CrashLogs");
            } else {
                // للإصدارات الأقدم
                crashDir = new File(android.os.Environment.getExternalStorageDirectory(), 
                    "Download/OneUI_CrashLogs");
            }
            
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }
            
            // تنسيق اسم الملف بالتاريخ والوقت
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
            String timestamp = dateFormat.format(new Date());
            String fileName = "OneUI_Crash_" + timestamp + ".txt";
            
            File crashFile = new File(crashDir, fileName);
            
            // كتابة تفاصيل الخطأ
            BufferedWriter writer = new BufferedWriter(new FileWriter(crashFile));
            
            // معلومات أساسية
            writer.write("=== تقرير خطأ تطبيق OneUI ===\n");
            writer.write("الوقت: " + new Date().toString() + "\n");
            writer.write("الخيط: " + Thread.currentThread().getName() + "\n");
            writer.write("مسار الملف: " + crashFile.getAbsolutePath() + "\n");
            writer.write("يمكنك العثور على هذا الملف في مجلد التنزيلات/OneUI_CrashLogs\n\n");
            
            // معلومات التطبيق
            writer.write("=== معلومات التطبيق ===\n");
            try {
                PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
                writer.write("اسم الحزمة: " + packageInfo.packageName + "\n");
                writer.write("رقم الإصدار: " + packageInfo.versionCode + "\n");
                writer.write("اسم الإصدار: " + packageInfo.versionName + "\n");
            } catch (PackageManager.NameNotFoundException e) {
                writer.write("فشل في الحصول على معلومات التطبيق\n");
            }
            writer.write("\n");
            
            // معلومات الجهاز
            writer.write("=== معلومات الجهاز ===\n");
            writer.write("الطراز: " + Build.MODEL + "\n");
            writer.write("الشركة المصنعة: " + Build.MANUFACTURER + "\n");
            writer.write("إصدار أندرويد: " + Build.VERSION.RELEASE + "\n");
            writer.write("مستوى API: " + Build.VERSION.SDK_INT + "\n");
            writer.write("البنية: " + Build.CPU_ABI + "\n");
            writer.write("لوحة الجهاز: " + Build.BOARD + "\n");
            writer.write("معرف البناء: " + Build.ID + "\n");
            writer.write("\n");
            
            // تفاصيل الخطأ
            writer.write("=== تفاصيل الخطأ ===\n");
            writer.write("نوع الاستثناء: " + exception.getClass().getName() + "\n");
            writer.write("رسالة الخطأ: " + (exception.getMessage() != null ? exception.getMessage() : "لا توجد رسالة") + "\n");
            writer.write("السبب الجذري: " + (exception.getCause() != null ? exception.getCause().toString() : "غير محدد") + "\n");
            writer.write("\n");
            
            // Stack Trace مفصل
            writer.write("=== تتبع المكدس (Stack Trace) ===\n");
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            writer.write(stackTrace.toString());
            writer.write("\n");
            
            // معلومات الذاكرة
            writer.write("=== معلومات الذاكرة ===\n");
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            
            writer.write("أقصى ذاكرة متاحة: " + formatBytes(maxMemory) + "\n");
            writer.write("إجمالي الذاكرة المخصصة: " + formatBytes(totalMemory) + "\n");
            writer.write("الذاكرة الحرة: " + formatBytes(freeMemory) + "\n");
            writer.write("الذاكرة المستخدمة: " + formatBytes(totalMemory - freeMemory) + "\n");
            
            // معلومات إضافية مفيدة للتشخيص
            writer.write("\n=== معلومات إضافية ===\n");
            writer.write("معرف العملية: " + android.os.Process.myPid() + "\n");
            writer.write("معرف المستخدم: " + android.os.Process.myUid() + "\n");
            writer.write("اللغة الحالية: " + Locale.getDefault().toString() + "\n");
            writer.write("المنطقة الزمنية: " + java.util.TimeZone.getDefault().getID() + "\n");
            
            writer.write("\n=== تعليمات ===\n");
            writer.write("1. افتح مدير الملفات في هاتفك\n");
            writer.write("2. اذهب إلى مجلد 'التنزيلات' أو 'Downloads'\n");
            writer.write("3. ابحث عن مجلد 'OneUI_CrashLogs'\n");
            writer.write("4. افتح الملف بأي تطبيق عرض نصوص\n");
            writer.write("5. يمكنك نسخ محتوى الملف ومشاركته للحصول على المساعدة\n");
            
            writer.write("\n=== نهاية التقرير ===\n");
            
            writer.close();
            
            Log.i(TAG, "تم حفظ تقرير الخطأ في: " + crashFile.getAbsolutePath());
            Log.i(TAG, "يمكنك الوصول للملف من مدير الملفات -> التنزيلات -> OneUI_CrashLogs");
            
        } catch (IOException e) {
            Log.e(TAG, "فشل في كتابة ملف تقرير الخطأ في التخزين العام", e);
            // محاولة الحفظ في التخزين الداخلي كبديل
            saveCrashReportInternal(exception);
        } catch (SecurityException e) {
            Log.e(TAG, "لا توجد صلاحيات للكتابة في التخزين الخارجي", e);
            saveCrashReportInternal(exception);
        }
    }
    
    /**
     * حفظ احتياطي في التخزين الداخلي في حالة فشل التخزين الخارجي
     */
    private void saveCrashReportInternal(Throwable exception) {
        try {
            File crashDir = new File(context.getFilesDir(), CRASH_LOG_DIR);
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
            String timestamp = dateFormat.format(new Date());
            String fileName = LOG_FILE_PREFIX + timestamp + "_internal" + LOG_FILE_EXTENSION;
            
            File crashFile = new File(crashDir, fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(crashFile));
            
            writer.write("=== تقرير خطأ التطبيق (نسخة احتياطية) ===\n");
            writer.write("الوقت: " + new Date().toString() + "\n");
            writer.write("نوع الخطأ: " + exception.getClass().getName() + "\n");
            writer.write("رسالة الخطأ: " + exception.getMessage() + "\n\n");
            
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            writer.write(stackTrace.toString());
            
            writer.close();
            Log.i(TAG, "تم حفظ النسخة الاحتياطية في: " + crashFile.getAbsolutePath());
            
        } catch (IOException e) {
            Log.e(TAG, "فشل في حفظ النسخة الاحتياطية أيضاً", e);
        }
    }
    
    /**
     * تنسيق حجم البايتات إلى وحدات أكبر
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format(Locale.getDefault(), "%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format(Locale.getDefault(), "%.2f MB", bytes / (1024.0 * 1024.0));
        return String.format(Locale.getDefault(), "%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    /**
     * الحصول على جميع ملفات سجلات الأخطاء المحفوظة
     */
    public static File[] getCrashLogFiles(Context context) {
        File crashDir = new File(context.getFilesDir(), CRASH_LOG_DIR);
        if (crashDir.exists() && crashDir.isDirectory()) {
            return crashDir.listFiles((dir, name) -> 
                name.startsWith(LOG_FILE_PREFIX) && name.endsWith(LOG_FILE_EXTENSION));
        }
        return new File[0];
    }
    
    /**
     * حذف ملفات السجلات القديمة (الأقدم من 7 أيام)
     */
    public static void cleanOldLogs(Context context) {
        File[] logFiles = getCrashLogFiles(context);
        long weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        
        for (File file : logFiles) {
            if (file.lastModified() < weekAgo) {
                file.delete();
                Log.d(TAG, "تم حذف ملف السجل القديم: " + file.getName());
            }
        }
    }
    }
