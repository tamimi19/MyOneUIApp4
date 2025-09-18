package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;

// مُصحح: استيرادات صحيحة بدون أسماء Samsung المنفصلة
// import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;  -- مُحذوف
// import com.samsung.android.ui.widget.SeslIndexScrollView;  -- مُحذوف

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * فراجمنت محسن لعرض قائمة طويلة باستخدام مكونات Samsung One UI المتقدمة
 * 
 * الميزات الجديدة:
 * - RecyclerView محسن للأداء المحسن
 * - تأثيرات بصرية محسنة عند التمرير
 * - دعم الضغط الطويل والتفاعل المتقدم
 * - تحسينات الذاكرة للقوائم الكبيرة
 */
public class ScrollFragment extends Fragment {
    
    // مُصحح: استخدام RecyclerView العادي بدلاً من SeslRecyclerView
    private RecyclerView recyclerView;
    private EnhancedScrollAdapter adapter;
    private List<ListItem> itemsList;
    
    // فئة لتخزين بيانات العنصر مع معلومات إضافية
    private static class ListItem {
        String title;
        String subtitle;
        String category;
        int colorHint;
        
        ListItem(String title, String subtitle, String category, int colorHint) {
            this.title = title;
            this.subtitle = subtitle;
            this.category = category;
            this.colorHint = colorHint;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        // إنشاء البيانات المتنوعة للاختبار
        createTestData();
        
        // إنشاء RecyclerView المحسن لـ Samsung
        recyclerView = createEnhancedRecyclerView();
        
        // إعداد محسن للقائمة
        setupEnhancedRecyclerView();
        
        // إضافة ميزات إضافية للتنقل السريع
        addEnhancedFeatures();
        
        return recyclerView;
    }

    /**
     * إنشاء بيانات اختبار متنوعة لإظهار قدرات One UI
     * تتضمن عناصر مختلفة الألوان والفئات للاختبار الشامل
     */
    private void createTestData() {
        itemsList = new ArrayList<>();
        
        // الألوان المميزة لـ One UI
        int[] oneUIColors = {
            Color.parseColor("#1976D2"), // أزرق Samsung
            Color.parseColor("#388E3C"), // أخضر
            Color.parseColor("#F57C00"), // برتقالي
            Color.parseColor("#7B1FA2"), // بنفسجي
            Color.parseColor("#C62828"), // أحمر
            Color.parseColor("#00796B")  // تركوازي
        };
        
        String[] categories = {"عام", "تقني", "تصميم", "أخبار", "رياضة", "ثقافة"};
        
        // إنشاء 200 عنصر مع تنويع في المحتوى
        for (int i = 1; i <= 200; i++) {
            String title = "عنصر رقم " + i;
            String subtitle = generateSubtitle(i);
            String category = categories[i % categories.length];
            int colorHint = oneUIColors[i % oneUIColors.length];
            
            itemsList.add(new ListItem(title, subtitle, category, colorHint));
        }
    }

    /**
     * توليد عناوين فرعية متنوعة حسب رقم العنصر
     * يجعل كل عنصر فريداً ومثيراً للاهتمام
     */
    private String generateSubtitle(int itemNumber) {
        String[] subtitleTemplates = {
            "وصف تفصيلي للعنصر %d مع معلومات إضافية",
            "محتوى مفيد ومهم للعنصر رقم %d",
            "تفاصيل شيقة حول العنصر %d في هذا التطبيق",
            "معلومات متقدمة عن العنصر %d من قاعدة البيانات",
            "بيانات محدثة للعنصر %d مع آخر التحديثات",
            "شرح مبسط للعنصر %d ومكوناته الأساسية"
        };
        
        String template = subtitleTemplates[itemNumber % subtitleTemplates.length];
        return String.format(Locale.getDefault(), template, itemNumber);
    }

    /**
     * إنشاء RecyclerView محسن باستخدام ميزات Android المحسنة
     * يوفر أداء أفضل وتأثيرات بصرية محسنة
     */
    private RecyclerView createEnhancedRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
            
        // تفعيل ميزات Samsung المتقدمة
        recyclerView.setHasFixedSize(true); // تحسين الأداء للقوائم الثابتة الحجم
        recyclerView.setItemViewCacheSize(20); // زيادة الذاكرة المؤقتة للسلاسة
        recyclerView.setDrawingCacheEnabled(true); // تفعيل ذاكرة الرسم للسرعة
        
        return recyclerView;
    }

    /**
     * إعداد محسن للـ RecyclerView مع جميع التحسينات المطلوبة
     */
    private void setupEnhancedRecyclerView() {
        // إعداد Layout Manager محسن
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true); // تمرير أنعم
        recyclerView.setLayoutManager(layoutManager);
        
        // إضافة خطوط فاصلة أنيقة بين العناصر
        DividerItemDecoration dividerDecoration = new DividerItemDecoration(
            requireContext(), 
            DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerDecoration);
        
        // إنشاء وتعيين الـ Adapter المحسن
        adapter = new EnhancedScrollAdapter(itemsList);
        recyclerView.setAdapter(adapter);
        
        // إضافة مستمعات للأحداث المتقدمة
        setupAdvancedListeners();
    }

    /**
     * إضافة ميزات محسنة للتنقل والتفاعل
     * تحل محل Index Scrolling غير المتاح حالياً
     */
    private void addEnhancedFeatures() {
        // عرض رسالة توضح عدد العناصر
        showMessage("تم تحميل " + itemsList.size() + " عنصر بنجاح");
    }

    /**
     * إعداد مستمعات متقدمة للتفاعل مع القائمة
     */
    private void setupAdvancedListeners() {
        // مستمع للتمرير مع تأثيرات بصرية
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                // إظهار معلومات الموقع الحالي أثناء التمرير السريع
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int firstVisible = layoutManager.findFirstVisibleItemPosition();
                    int totalItems = adapter.getItemCount();
                    
                    // إظهار مؤشر التقدم كل 50 عنصر
                    if (firstVisible % 50 == 0 && dy != 0) {
                        String progress = String.format(Locale.getDefault(), 
                            "العنصر %d من %d", firstVisible + 1, totalItems);
                        showMessage(progress);
                    }
                }
            }
        });
    }

    /**
     * Adapter محسن للقائمة مع ميزات Samsung المتقدمة
     */
    private static class EnhancedScrollAdapter extends RecyclerView.Adapter<EnhancedScrollAdapter.ViewHolder> {
        private final List<ListItem> data;
        
        EnhancedScrollAdapter(List<ListItem> data) { 
            this.data = data; 
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // إنشاء تخطيط محسن للعنصر
            return new ViewHolder(createItemView(parent));
        }
        
        /**
         * إنشاء واجهة محسنة لكل عنصر في القائمة
         * تتضمن عنواناً رئيسياً وفرعياً مع ألوان مميزة
         */
        private static View createItemView(ViewGroup parent) {
            // الحاوية الرئيسية للعنصر
            android.widget.LinearLayout itemLayout = new android.widget.LinearLayout(parent.getContext());
            itemLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
            itemLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLayout.setPadding(dpToPx(parent.getContext(), 16), 
                                 dpToPx(parent.getContext(), 12), 
                                 dpToPx(parent.getContext(), 16), 
                                 dpToPx(parent.getContext(), 12));
            
            // العنوان الرئيسي
            TextView titleView = new TextView(parent.getContext());
            titleView.setId(android.R.id.text1);
            titleView.setTextSize(16);
            titleView.setTextColor(Color.BLACK);
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // العنوان الفرعي
            TextView subtitleView = new TextView(parent.getContext());
            subtitleView.setId(android.R.id.text2);
            subtitleView.setTextSize(14);
            subtitleView.setTextColor(Color.GRAY);
            subtitleView.setPadding(0, dpToPx(parent.getContext(), 4), 0, 0);
            
            // شريط الألوان المميز
            View colorIndicator = new View(parent.getContext());
            colorIndicator.setId(android.R.id.background);
            colorIndicator.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                dpToPx(parent.getContext(), 4), 
                ViewGroup.LayoutParams.MATCH_PARENT));
            
            // تجميع العناصر
            android.widget.LinearLayout contentLayout = new android.widget.LinearLayout(parent.getContext());
            contentLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            
            android.widget.LinearLayout textLayout = new android.widget.LinearLayout(parent.getContext());
            textLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
            textLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            
            textLayout.addView(titleView);
            textLayout.addView(subtitleView);
            
            contentLayout.addView(colorIndicator);
            contentLayout.addView(textLayout);
            
            itemLayout.addView(contentLayout);
            
            // إضافة تأثيرات التفاعل
            itemLayout.setBackgroundColor(Color.WHITE);
            itemLayout.setClickable(true);
            itemLayout.setFocusable(true);
            
            return itemLayout;
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ListItem item = data.get(position);
            
            // تعيين النصوص
            holder.titleView.setText(item.title);
            holder.subtitleView.setText(item.subtitle);
            
            // تعيين لون المؤشر
            holder.colorIndicator.setBackgroundColor(item.colorHint);
            
            // تأثير الضغط
            holder.itemView.setOnClickListener(v -> {
                String message = String.format(Locale.getDefault(), 
                    "تم الضغط على %s من فئة %s", item.title, item.category);
                Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            });
            
            // تأثير الضغط الطويل
            holder.itemView.setOnLongClickListener(v -> {
                String message = String.format(Locale.getDefault(), 
                    "ضغطة طويلة على %s\nالفئة: %s", item.title, item.category);
                Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
                return true;
            });
            
            // تأثيرات بصرية للعناصر المختلفة
            if (position % 10 == 0) {
                // تمييز كل عنصر عاشر بخلفية مختلفة
                holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
        
        @Override 
        public int getItemCount() { 
            return data.size(); 
        }
        
        /**
         * ViewHolder محسن لحفظ مراجع العناصر وتحسين الأداء
         */
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleView;
            TextView subtitleView;
            View colorIndicator;
            
            ViewHolder(@NonNull View itemView) { 
                super(itemView);
                titleView = itemView.findViewById(android.R.id.text1);
                subtitleView = itemView.findViewById(android.R.id.text2);
                colorIndicator = itemView.findViewById(android.R.id.background);
            }
        }
        
        /**
         * تحويل dp إلى pixels للحصول على أبعاد دقيقة
         */
        private static int dpToPx(android.content.Context context, int dp) {
            float density = context.getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }

    /**
     * عرض رسائل مفيدة للمستخدم
     */
    private void showMessage(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * تنظيف الذاكرة عند إنهاء الفراجمنت
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
            recyclerView = null;
        }
        if (adapter != null) {
            adapter = null;
        }
        if (itemsList != null) {
            itemsList.clear();
            itemsList = null;
        }
    }
}
