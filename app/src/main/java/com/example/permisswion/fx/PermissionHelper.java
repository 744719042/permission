package com.example.permisswion.fx;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public final class PermissionHelper {
    private static final PermissionEntity sPermissionEntity = new PermissionEntity();

    public static void requestPermissions(AppCompatActivity activity, int requestCode, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            executePermissionGranted(activity, requestCode);
            return;
        }

        List<String> requestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }

        if (requestPermissions.isEmpty()) {
            executePermissionGranted(activity, requestCode);
            return;
        }

        sPermissionEntity.object = activity;
        sPermissionEntity.requestCode = requestCode;
        ActivityCompat.requestPermissions(activity, requestPermissions.toArray(new String[0]), requestCode);
    }

    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] result) {
        if (requestCode != sPermissionEntity.requestCode) {
            return;
        }

        for (int i = 0; i < result.length; i++) {
            if (result[i] != PackageManager.PERMISSION_GRANTED) {
                executePermissionDenied(sPermissionEntity.object, sPermissionEntity.requestCode);
                return;
            }
        }

        executePermissionGranted(sPermissionEntity.object, sPermissionEntity.requestCode);
    }

    private static void executePermissionDenied(Object obj, int requestCode) {
        ReflectUtils.executeDeniedMethod(obj, requestCode);
        sPermissionEntity.clear();
    }

    private static void executePermissionGranted(Object object, int requestCode) {
        ReflectUtils.executeGrantedMethod(object, requestCode);
        sPermissionEntity.clear();
    }

    public static void requestPermissions(Fragment fragment, int requestCode, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("Permission cannot be null");
        }

        if (!fragment.isAdded()) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            executePermissionGranted(fragment, requestCode);
            return;
        }

        List<String> requestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }

        if (requestPermissions.isEmpty()) {
            executePermissionGranted(fragment, requestCode);
            return;
        }

        sPermissionEntity.object = fragment;
        sPermissionEntity.requestCode = requestCode;
        ActivityCompat.requestPermissions(fragment.getActivity(), requestPermissions.toArray(new String[0]), requestCode);
    }
}