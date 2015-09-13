package com.tatbigy.permissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraPreview();
            }
        });
    }

    private void showCameraPreview() {

        if (isMNC()) {
            // On Android M and above, need to check if permission has been granted at runtime.
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is available, start camera preview
                startCamera();
                Snackbar.make(mLayout, "لديك الصلاحية للوصول ، قم باستخدام الكاميرا",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CAMERA)) {
                    // Explain to the user why we need to read the contacts
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("استخدام الكاميرا");
                    builder.setMessage("هنا قم بالتوضيح للمستخدم سبب الوصول للكاميرا");
                    builder.setPositiveButton("اخفاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    0);
                        }
                    });

                    builder.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            0);
                }

            }
        } else {
            /*
             Below Android M all permissions have already been grated at install time and do not
             need to verified or requested.
             If a permission has been disabled in the system settings, the API will return
             unavailable or empty data instead. */
            Toast.makeText(this,
                    "Requested permissions are granted at install time below M and are always "
                            + "available at runtime.",
                    Toast.LENGTH_SHORT).show();
            startCamera();
        }

    }

    private void startCamera() {
        Intent intent = new Intent(this, CameraPreviewActivity.class);
        startActivity(intent);
    }

    public static boolean isMNC() {
        /*
         TODO: In the Android M Preview release, checking if the platform is M is done through
         the codename, not the version code. Once the API has been finalised, the following check
         should be used: */
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        //  return "MNC".equals(Build.VERSION.CODENAME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 0) {
            // Request for camera permission.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "لديك الصلاحية للوصول ، قم باستخدام الكاميرا",
                        Snackbar.LENGTH_SHORT)
                        .show();
                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "تم رفض الوصول للكاميرا",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }

}
