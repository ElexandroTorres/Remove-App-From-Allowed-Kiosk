package com.elexandro.removefromallowed;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.elexandro.removefromallowed.receivers.AdminIntentReceiver;
import com.elexandro.removefromallowed.receivers.PackageInstallReceiver;

public class MainActivity extends AppCompatActivity {

    String appKioskPackage = "com.elexandro.removefromallowed";
    String playStorePackage = "com.android.vending";

    private PackageInstallReceiver packageInstallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playStoreButton = findViewById(R.id.open_play_store);
        Button customActionButton = findViewById(R.id.unregister_play_store);
        Button registerPlayStoreButton = findViewById(R.id.register_play_store);
        playStoreButton.setOnClickListener(v -> openPlayStore());
        customActionButton.setOnClickListener(v -> unregisterPlayStore());
        registerPlayStoreButton.setOnClickListener(v -> registerPlayStore());

        packageInstallReceiver = new PackageInstallReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(packageInstallReceiver, filter);

        String[] packagesAllowed = {appKioskPackage, playStorePackage};

        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        if(devicePolicyManager.isDeviceOwnerApp(getPackageName())) {
            ComponentName adminName = new ComponentName(this, AdminIntentReceiver.class);
            devicePolicyManager.setLockTaskPackages(adminName, packagesAllowed);

            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                options = ActivityOptions.makeBasic();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                options.setLockTaskEnabled(true);
            }

            PackageManager packageManager = getPackageManager();
            Intent launchIntent = packageManager.getLaunchIntentForPackage(appKioskPackage);
            if (launchIntent != null) {
                startActivity(launchIntent, options.toBundle());
            }
        }
    }

    private void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=com.king.candycrushsaga");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void unregisterPlayStore() {
        String[] UPDATED_APP_PACKAGES = {appKioskPackage};

        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminName = new ComponentName(this, AdminIntentReceiver.class);
        devicePolicyManager.setLockTaskPackages(adminName, UPDATED_APP_PACKAGES);
        Toast.makeText(getApplicationContext(), "Play Store removida dos Apps permitidos", Toast.LENGTH_LONG).show();
    }

    private void registerPlayStore() {
        String[] UPDATED_APP_PACKAGES = {appKioskPackage, playStorePackage};

        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminName = new ComponentName(this, AdminIntentReceiver.class);
        devicePolicyManager.setLockTaskPackages(adminName, UPDATED_APP_PACKAGES);
        Toast.makeText(getApplicationContext(), "Play Store adicionada aos Apps permitidos", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(packageInstallReceiver);
    }
}