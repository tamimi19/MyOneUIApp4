// ملف ScrollFragment.java
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // إنشاء RecyclerView برمجياً لتجنب مشاكل XML
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT));

        // إعداد البيانات
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            items.add("عنصر رقم " + i);
        }
        
        RecyclerView.Adapter<ScrollAdapter.VH> adapter = new ScrollAdapter(items);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    // Adapter بسيط لعرض العناصر في list
    private static class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.VH> {
        private final List<String> data;
        
        ScrollAdapter(List<String> data) { 
            this.data = data; 
        }
        
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // إنشاء TextView بسيط
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(32, 32, 32, 32);
            textView.setTextSize(16);
            return new VH(textView);
        }
        
        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            ((TextView)holder.itemView).setText(data.get(position));
        }
        
        @Override 
        public int getItemCount() { 
            return data.size(); 
        }
        
        static class VH extends RecyclerView.ViewHolder {
            VH(@NonNull View v) { 
                super(v); 
            }
        }
    }
}

