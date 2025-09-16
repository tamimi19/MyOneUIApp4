package com.example.oneuiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scroll, container, false);
        RecyclerView list = root.findViewById(R.id.recycler_scroll);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            items.add("عنصر رقم " + i);
        }
        RecyclerView.Adapter adapter = new ScrollAdapter(items);
        list.setAdapter(adapter);
        return root;
    }

    // Adapter بسيط لعرض العناصر في list
    private static class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.VH> {
        private final List<String> data;
        ScrollAdapter(List<String> data) { this.data = data; }
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                     .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(v);
        }
        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            ((TextView)holder.itemView).setText(data.get(position));
        }
        @Override public int getItemCount() { return data.size(); }
        static class VH extends RecyclerView.ViewHolder {
            VH(View v) { super(v); }
        }
    }
}
