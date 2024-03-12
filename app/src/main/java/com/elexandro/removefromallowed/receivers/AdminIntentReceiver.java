package com.elexandro.removefromallowed.receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class AdminIntentReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if(intent.getAction().equals(ACTION_DEVICE_ADMIN_DISABLE_REQUESTED)) {
            abortBroadcast();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onLockTaskModeEntering(@NonNull Context context, @NonNull Intent intent, @NonNull String pkg) {
        super.onLockTaskModeEntering(context, intent, pkg);
        System.out.println("Entrou no modo lock task " + pkg);
    }

    @Override
    public void onLockTaskModeExiting(@NonNull Context context, @NonNull Intent intent) {
        super.onLockTaskModeExiting(context, intent);
        System.out.println("Saiu no modo lock task");
    }
}
