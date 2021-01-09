package org.techtown.messagetts;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class App {
    PackageInfo packageInfo;

    String label;
    String packageName;
    Drawable icon;

    public App(PackageInfo packageInfo, String label, String packageName, Drawable icon) {
        this.packageInfo = packageInfo;
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
