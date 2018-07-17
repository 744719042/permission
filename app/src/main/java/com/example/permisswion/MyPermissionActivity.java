package com.example.permisswion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.permisswion.fx.PermissionDenied;
import com.example.permisswion.fx.PermissionGranted;
import com.example.permisswion.fx.PermissionHelper;

import java.io.File;
import java.io.FileOutputStream;

public class MyPermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_WRITE_STORAGE = 200;
    private Button button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_permission);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.image);
    }

    @Override
    public void onClick(View v) {
        PermissionHelper.requestPermissions(this, REQUEST_WRITE_STORAGE,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE });
    }

    @PermissionGranted(requestCode = REQUEST_WRITE_STORAGE)
    private void startTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photo.jpg");
            FileOutputStream fos = null;
            try {
                if (!file.exists()) file.createNewFile();
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(fos);
            }

            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bmp);
        }
    }

    @PermissionDenied(requestCode = REQUEST_WRITE_STORAGE)
    public void showToast() {
        Toast.makeText(getApplicationContext(), R.string.has_no_permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
