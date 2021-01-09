package org.techtown.messagetts;

import android.view.View;
import android.widget.CompoundButton;

public interface OnAppItemClickListener {
    public void onItemClick(AppAdapter.ViewHolder holder, View view, int position);
    public void onItemClick_Toggle(AppAdapter.ViewHolder holder, CompoundButton check_Button, int position);
}
