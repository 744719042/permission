package com.example.permisswion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class EasyPermissionActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private Button dialButton;
    private String[] permissions = new String[]{
            Manifest.permission.CALL_PHONE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_permission);
        dialButton = (Button) findViewById(R.id.dial_button);
        dialButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "确认允许拨号", 100, permissions);
        } else {
            makePhoneCall();
        }
    }

    @SuppressLint("MissingPermission")
    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:14899992222"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            makePhoneCall();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 100) {
            Toast.makeText(this, R.string.has_no_permission, Toast.LENGTH_SHORT).show();
        }
    }
}
