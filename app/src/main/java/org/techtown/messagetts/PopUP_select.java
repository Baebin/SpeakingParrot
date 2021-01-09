package org.techtown.messagetts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PopUP_select extends AppCompatActivity {
    private static final String TAG = "PopUP_select";
    AppAdapter appAdapter;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_select);

        Log.d(TAG, "onCreate 호출됨");
        final Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        final BBSoundPlayer bbSoundPlayer = new BBSoundPlayer();
        bbSoundPlayer.initSounds(getApplicationContext());

        recyclerView = findViewById(R.id.recyclewView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "close onClick 호출됨");

                bbSoundPlayer.play(BBSoundPlayer.click_03);
                vib.vibrate(100);

                finish();
            }
        });

        for (String test : MainActivity.check) {
            Log.d(TAG, test);
        }

        appAdapter = new AppAdapter();

        PackageManager packageName = this.getPackageManager();
        List<PackageInfo> installList = packageName.getInstalledPackages(0);

        final ArrayList<App> Applist = new ArrayList<App>();

        final ArrayList<String> list_packageName = new ArrayList<String>();

        for (int i = 0; i < installList.size(); i++) {
            App app = null;
            try {
                app = new App(
                        installList.get(i),
                        getPackageLabel(installList.get(i).packageName),
                        installList.get(i).packageName,
                        getPackageIcon(installList.get(i).packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (!getPackageLabel(installList.get(i).packageName).equals(installList.get(i).packageName) &&
                    !getPackageLabel(installList.get(i).packageName).contains("com.") &&
                    !getPackageLabel(installList.get(i).packageName).contains("android.") &&
                    !getPackageLabel(installList.get(i).packageName).contains("com.") &&
                    !installList.get(i).packageName.equals("org.techtown.messagetts")) {

                if (getPackageManager().getLaunchIntentForPackage(installList.get(i).packageName) != null) {
                    Applist.add(app);

                    Log.d(TAG, "\n new App 호출됨 : " + getPackageLabel(installList.get(i).packageName) +
                            "\n" + installList.get(i).packageName);
                    //                        "\n" + installList.get(i).requestedPermissions);

                    list_packageName.add(installList.get(i).packageName);
                }
            }
        }

        Comparator<App> appComparator = new Comparator<App>() { // 정렬
            @Override
            public int compare(App object1, App object2) {
                return object1.getLabel().compareToIgnoreCase(object2.getLabel());
            }
        };

        Collections.sort(Applist, appComparator);
        appAdapter.setItems(Applist);

        if (!MainActivity.firstStart) {
            Log.d(TAG, "firstStart 호출됨");

            MainActivity.firstStart = true;
            MainActivity.check = list_packageName;
            finish();
        }

        recyclerView.setAdapter(appAdapter);
        Log.d(TAG, "setAdapter 호출됨");

        Button button_sel = findViewById(R.id.button_select_All);
        button_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "select onClick 호출됨");

                MainActivity.check = list_packageName;

                bbSoundPlayer.play(BBSoundPlayer.click_03);
                vib.vibrate(100);

                recyclerView.removeAllViews();
                recyclerView.setAdapter(appAdapter);
            }
        });

        Button button_rem = findViewById(R.id.button_remove_All);
        button_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "remove onClick 호출됨");

                MainActivity.check = new ArrayList<String>();

                bbSoundPlayer.play(BBSoundPlayer.click_03);
                vib.vibrate(100);

                recyclerView.removeAllViews();
                recyclerView.setAdapter(appAdapter);
            }
        });

        appAdapter.setOnItemClickListener(new OnAppItemClickListener() {
            @Override
            public void onItemClick(AppAdapter.ViewHolder holder, View view, int position) {
            }

            @Override
            public void onItemClick_Toggle(AppAdapter.ViewHolder holder, CompoundButton check_button, int position) {
                App item = appAdapter.getItem(position);

                //Switch check = view.findViewById(R.id.switch_allow);

                bbSoundPlayer.play(BBSoundPlayer.click_04);
                vib.vibrate(100);

                if (MainActivity.check.contains(item.getPackageName())) {
                    check_button.setChecked(false);
                    MainActivity.check.remove(item.getPackageName());
                    Log.d(TAG, "appAdapter Selected : Check True -> False");
                } else {
                    check_button.setChecked(true);
                    MainActivity.check.add(item.getPackageName());
                    Log.d(TAG, "appAdapter Selected : Check False -> True");
                }

                Log.d(TAG, "appAdapter Selected : " +
                        "\n id : " + position + "\" +" +
                        "\n packageName : " + item.getPackageName());
                for (String test : MainActivity.check) {
                    Log.d(TAG, test);
                }
            }
        });
    }

    public String getPackageLabel(String packageName) {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {}
        return (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : null);
    }

    public Drawable getPackageIcon(String packageName) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {}
        return getPackageManager().getApplicationIcon(applicationInfo);
    }
}
