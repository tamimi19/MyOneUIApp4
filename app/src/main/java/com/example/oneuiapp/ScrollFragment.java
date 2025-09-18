package com.example.oneuiapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;

/**
 * فراجمنت قائمة التمرير مع 200 عنصر
 * يستخدم RecyclerView مع تحسينات OneUI
 */
public class ScrollFragment extends Fragment {

    private RecyclerView recyclerView;
    
    private static final int ONEUI_BLUE = Color.parseColor("#1976D2");
    private static final int ONEUI_SURFACE = Color.parseColor("#F5F5F5");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                           @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        
        LinearLayout rootLayout = new LinearLayout(requireContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        // إنشاء العنوان
        TextView headerText = createHeaderText();
        rootLayout.addView(headerText);

        // إنشاء RecyclerView محسن
        recyclerView = createEnhancedRecyclerView();
        rootLayout.addView(recyclerView);

        // إضافة رسالة في الأسفل
        TextView footerText = createFooterText();
        rootLayout.addView(footerText);

        return rootLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // إعداد البيانات والمحول
        setupRecyclerView();
        
        // إضافة تأثير الانتقال
        addTransitionEffect();
        
        // رسالة ترحيب
        Toast.makeText(requireContext(), 
            "تم تحميل 200 عنصر بنجاح! 📋", 
            Toast.LENGTH_SHORT).show();
    }

    /**
     * إنشاء نص العنوان
     */
    private TextView createHeaderText() {
        TextView headerText = new TextView(requireContext());
        headerText.setText("📋 قائمة التمرير المحسنة");
        headerText.setTextSize(20);
        headerText.setTextColor(ONEUI_BLUE);
        headerText.setPadding(0, 0, 0, dpToPx(16));
        headerText.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        return headerText;
    }

    /**
     * إنشاء RecyclerView محسن مع ميزات OneUI
     */
    private RecyclerView createEnhancedRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        
        LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            0, // height = 0 لاستخدام weight
            1.0f); // weight = 1 ليأخذ المساحة المتبقية
        recyclerView.setLayoutParams(recyclerParams);
        
        // تحسينات الأداء
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setItemViewCacheSize(20);
        
        // إعداد Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        
        // إضافة خطوط الفصل
        DividerItemDecoration dividerDecoration = new DividerItemDecoration(
            requireContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerDecoration);
        
        // تحسين التمرير
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);
        
        return recyclerView;
    }

    /**
     * إنشاء نص الختام
     */
    private TextView createFooterText() {
        TextView footerText = new TextView(requireContext());
        footerText.setText("💡 نصيحة: استخدم ميزة Pull-to-Reach لسهولة الوصول للعناصر العلوية");
        footerText.setTextSize(14);
        footerText.setTextColor(Color.GRAY);
        footerText.setPadding(0, dpToPx(16), 0, 0);
        footerText.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT));
        
        return footerText;
    }

    /**
     * إعداد RecyclerView مع المحول والبيانات
     */
    private void setupRecyclerView() {
        // إنشاء قائمة 200 عنصر
        java.util.List<String> items = new java.util.ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            items.add("العنصر رقم " + i + " - محتوى تجريبي");
        }
        
        // إنشاء المحول
        ScrollAdapter adapter = new ScrollAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    /**
     * إضافة تأثير الانتقال عند فتح الفراجمنت
     */
    private void addTransitionEffect() {
        if (getView() != null) {
            getView().setAlpha(0f);
            getView().animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start();
        }
    }

    /**
     * تحويل dp إلى pixels
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * محول RecyclerView لعرض العناصر
     */
    private static class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {
        
        private final java.util.List<String> items;
        private static final int ONEUI_BLUE = Color.parseColor("#1976D2");

        public ScrollAdapter(java.util.List<String> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout itemLayout = new LinearLayout(parent.getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(32, 24, 32, 24);
            itemLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

            // أيقونة العنصر
            TextView iconText = new TextView(parent.getContext());
            iconText.setText("📄");
            iconText.setTextSize(16);
            iconText.setPadding(0, 0, 24, 0);
            iconText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

            // نص العنصر
            TextView itemText = new TextView(parent.getContext());
            itemText.setTextSize(16);
            itemText.setTextColor(Color.DKGRAY);
            itemText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f));

            itemLayout.addView(iconText);
            itemLayout.addView(itemText);

            return new ViewHolder(itemLayout, itemText);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String item = items.get(position);
            holder.textView.setText(item);
            
            // تأثير لوني متدرج
            if (position % 5 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            
            // إضافة تأثير النقر
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), 
                    "تم النقر على: " + item, 
                    Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView, TextView textView) {
                super(itemView);
                this.textView = textView;
            }
        }
    }
            }
