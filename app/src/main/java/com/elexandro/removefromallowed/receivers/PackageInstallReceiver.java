package com.elexandro.removefromallowed.receivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;


import com.elexandro.removefromallowed.MainActivity;

import java.util.Objects;

public class PackageInstallReceiver extends BroadcastReceiver {
    private MainActivity mainActivity;
    public PackageInstallReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action != null) {
            if(action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = Objects.requireNonNull(intent.getData()).getEncodedSchemeSpecificPart();

                PackageManager packageManager = mainActivity.getPackageManager();
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if(applicationInfo != null) {
                    String appName = packageManager.getApplicationLabel(applicationInfo).toString();

                    Toast.makeText(context, "Aplicação instalada:  " + appName, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
