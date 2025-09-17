// ملف SettingsFragment.java
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
        // إنشاء التخطيط برمجياً لتجنب مشاكل XML وPreferences
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // زر تغيير اللغة
        Button langButton = createButton("تغيير اللغة");
        langButton.setOnClickListener(v -> {
            // سيتم تطوير ميزة تغيير اللغة لاحقاً
            Toast.makeText(getContext(), "ميزة تغيير اللغة قيد التطوير", Toast.LENGTH_SHORT).show();
        });

        // زر الوضع الداكن
        Button darkModeButton = createButton("تبديل الوضع الداكن");
        darkModeButton.setOnClickListener(v -> {
            int currentMode = AppCompatDelegate.getDefaultNightMode();
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(getContext(), "تم التبديل للوضع الفاتح", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(getContext(), "تم التبديل للوضع الداكن", Toast.LENGTH_SHORT).show();
            }
        });

        // زر الإشعارات
        Button notificationButton = createButton("إعدادات الإشعارات");
        notificationButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "إعدادات الإشعارات", Toast.LENGTH_SHORT).show();
        });

        // إضافة الأزرار للتخطيط
        layout.addView(langButton);
        layout.addView(addSpacing());
        layout.addView(darkModeButton);
        layout.addView(addSpacing());
        layout.addView(notificationButton);

        return layout;
    }

    /**
     * إنشاء زر بتنسيق موحد
     */
    private Button createButton(String text) {
        Button button = new Button(requireContext());
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        return button;
    }

    /**
     * إضافة مساحة بين الأزرار
     */
    private View addSpacing() {
        View spacer = new View(requireContext());
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 24));
        return spacer;
    }

}
