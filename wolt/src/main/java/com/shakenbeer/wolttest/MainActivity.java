package com.shakenbeer.wolttest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.shakenbeer.wolttest.convert.Hours2Strings;
import com.shakenbeer.wolttest.convert.Json2Hours;
import com.shakenbeer.wolttest.model.Hour;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHOOSER = 1234;
    private static final int REQUEST_CODE_ACCESS_EXTERNAL_STORAGE = 101;

    private static final String RESULT_TEXT = "result_text";
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resultTextView = (TextView) findViewById(R.id.result_textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getContentIntent = FileUtils.createGetContentIntent();
                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, REQUEST_CHOOSER);
            }
        });

        ensureExternalStoragePermission();
    }

    private boolean ensureExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_request_title)
                        .setMessage(R.string.external_storage_permission_request_rationale)
                        .setPositiveButton(R.string.permission_button_accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ACCESS_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton(R.string.permission_button_deny, null)
                        .show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_TEXT, resultTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultTextView.setText(savedInstanceState.getString(RESULT_TEXT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    File file = FileUtils.getFile(this, uri);

                    if (file != null) {
                        process(file);
                    } else {
                        showError(R.string.file_chooser_error);
                    }

                }
                break;
        }
    }

    private void showError(int resId) {
        resultTextView.setText(resId);
    }

    private void process(File file) {
        String json = null;
        try {
            json = org.apache.commons.io.FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            showError(R.string.error_reading_file);
            return;
        }

        Map<String, Hour[]> week = Json2Hours.getInstance().convert(json);

        if (week.isEmpty()) {
            showError(R.string.bad_file);
            return;
        }

        List<String> days = Hours2Strings.getInstance().convert(week);

        if (days.isEmpty()) {
            showError(R.string.no_data);
            return;
        }

        resultTextView.setText(buildOutput(days));

    }

    private String buildOutput(List<String> days) {
        StringBuilder sb = new StringBuilder(days.get(0));
        if (days.size() > 1) {
            for (int i = 1; i < days.size(); i++) {
                sb.append("\n").append(days.get(i));
            }
        }
        return sb.toString();
    }
}
