package org.techtown.messagetts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private static final String TAG = "AppAdapter";
    ArrayList<App> items = new ArrayList<App>();

    OnAppItemClickListener listener;

    public void addItem(App item) {
        items.add(item);
    }

    public void setItems(ArrayList<App> items) {
        this.items = items;
    }

    public App getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, App item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnAppItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.app_item, parent, false);

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        App item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        ImageView imageView_Icon;

        Switch switch_allow;

        public ViewHolder(View itemView, final OnAppItemClickListener listener) {
            super(itemView);

            label = itemView.findViewById(R.id.textView_label);
            imageView_Icon = itemView.findViewById(R.id.imageView_Icon);
            switch_allow = itemView.findViewById(R.id.switch_allow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

            switch_allow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick_Toggle(ViewHolder.this, switch_allow, position);
                    }
                }
            });
        }

        public void setItem(App item) {
            label.setText(item.getLabel());
            imageView_Icon.setImageDrawable(item.getIcon());

            if (MainActivity.check.contains(item.getPackageName())) {
                switch_allow.setChecked(true);
                Log.d(TAG, "setChecked 호출됨 : " + item.getPackageName());
            }

            Log.d(TAG, "setItem 호출됨 : " + item.getPackageName());
        }
    }
}
