package com.example.permissiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnBrowser, btnSendEmail, btnExternalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBrowser = findViewById(R.id.btnBrowser);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnExternalStorage = findViewById(R.id.btnExternalStorage);

        btnBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));

                startActivity(intent);
            }
        });


        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // List of recipients
                String[] recipients=new String[]{"newclass5a@gmail.com"};

                String subject="Hi, how are you!";

                String content ="This is my test email";

                Intent intentEmail = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intentEmail.putExtra(Intent.EXTRA_EMAIL, recipients);
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                intentEmail.putExtra(Intent.EXTRA_TEXT, content);

                intentEmail.setType("text/plain");

                startActivity(Intent.createChooser(intentEmail, "Choose an email client from"));
            }
        });


        btnExternalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Dexter.withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    // do you work now
                                    Toast.makeText(MainActivity.this, "do you work now", Toast.LENGTH_SHORT).show();
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // permission is denied permanently, navigate user to app settings


                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                    builder.setMessage("Request Permission");
                                    builder.setTitle("This app need that permission");
                                    builder.setCancelable(false);

                                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });
                                    builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this, "Permission Activity",
                                                    Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);

                                        }
                                    });

                                    AlertDialog alert = builder.create();
                                    alert.show();


                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();


            }
        });

    }
}